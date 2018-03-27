package edu.uiowa.slis.GitHubTagLib.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.uiowa.extraction.LocalProperties;
import edu.uiowa.extraction.PropertyLoader;

public class JSONLoader {
    static Logger logger = Logger.getLogger(JSONLoader.class);
    protected static LocalProperties prop_file = null;
    static Connection conn = null;

    static enum modes {
	SEARCH, FULL, MEMBERS, REFRESH, README, NEW_SEARCH, COMMITS, PARENT
    };

    static modes mode = modes.README;
    static int queryCount = 0;
    static int apiCount = 0;
    static Hashtable<String, String> loginHash = new Hashtable<String, String>();

    static Decoder decoder = Base64.getMimeDecoder();

    static String client_id = "";
    static String client_secret = "";
    // curl
    // 'https://api.github.com/users/whatever?client_id=xxxx&client_secret=yyyy'

    static int searchID = 0;

    public static void main(String[] args) throws Exception {
	PropertyConfigurator.configure(args[0]);
	prop_file = PropertyLoader.loadProperties("github");
	client_id = prop_file.getProperty("client_id");
	client_secret = prop_file.getProperty("client_secret");
	getConnection();
	loadLoginHash();

	if (args.length > 1)
	    switch (args[1]) {
	    case "search":
		mode = modes.SEARCH;
		break;
	    case "full":
		mode = modes.FULL;
		break;
	    case "members":
		mode = modes.MEMBERS;
		break;
	    case "refresh":
		mode = modes.REFRESH;
		break;
	    case "readme":
		mode = modes.README;
		break;
	    case "new_search":
		mode = modes.NEW_SEARCH;
		break;
	    case "commits":
		mode = modes.COMMITS;
		break;
	    case "parent":
		mode = modes.PARENT;
		break;
	    }

	if (args.length > 2) {
	    if (args[2].equals("scan")) {
		PreparedStatement stmt = conn.prepareStatement("select id from github.search_term order by id");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
		    searchID = rs.getInt(1);
		    logger.info("searchScan search id " + searchID);
		    searchScan();
		}
		rs = stmt.executeQuery();
		while (rs.next()) {
		    searchID = rs.getInt(1);
		    logger.info("readmeScan search id " + searchID);
		    readmeScan();
		}
		rs = stmt.executeQuery();
		while (rs.next()) {
		    searchID = rs.getInt(1);
		    logger.info("scanOrgMembers search id " + searchID);
		    scanOrgMembers();
		}
		rs = stmt.executeQuery();
		while (rs.next()) {
		    searchID = rs.getInt(1);
		    logger.info("scanUserOrgs search id " + searchID);
		    scanUserOrgs();
		}
		refresh();
		rs = stmt.executeQuery();
		while (rs.next()) {
		    searchID = rs.getInt(1);
		    logger.info("parentScan search id " + searchID);
		    parentScan();
		}
		rs = stmt.executeQuery();
		while (rs.next()) {
		    searchID = rs.getInt(1);
		    logger.info("commitScan search id " + searchID);
		    commitScan();
		}
		stmt.close();
	    } else {
		searchID = Integer.parseInt(args[2]);
		logger.info("limiting task to search id " + searchID);
		process();
	    }
	} else {
	    process();
	}

	logger.info("done");
    }

    static void process() throws InterruptedException {
	boolean exceptionRaised = true;
	while (exceptionRaised) {
	    exceptionRaised = false;
	    try {
		switch (mode) {
		case SEARCH:
		    searchScan();
		    break;
		case FULL:
		    searchScan();
		    readmeScan();
		    scanOrgMembers();
		    scanUserOrgs();
		    refresh();
		    parentScan();
		    commitScan();
		    break;
		case MEMBERS:
		    scanUserOrgs();
		    conn.prepareStatement("refresh materialized view github.org_jsonb").execute();
		    scanOrgMembers();
		    break;
		case REFRESH:
		    refresh();
		    break;
		case README:
		    readmeScan();
		    break;
		case NEW_SEARCH:
		    newSearchScan();
		    break;
		case COMMITS:
		    commitScan();
		    break;
		case PARENT:
		    parentScan();
		    break;
		}
	    } catch (Exception e) {
		logger.error("Exception raised:", e);
		exceptionRaised = true;
		logger.info("exception raised! Sleeping for an hour...");
		Thread.sleep(60 * 60 * 1000);
	    }
	}
    }

    static void getConnection() throws ClassNotFoundException, SQLException {
	logger.info("connecting to database...");
	Class.forName("org.postgresql.Driver");
	Properties props = new Properties();
	props.setProperty("user", prop_file.getProperty("jdbc.user"));
	props.setProperty("password", prop_file.getProperty("jdbc.password"));
	conn = DriverManager.getConnection("jdbc:postgresql://" + prop_file.getProperty("jdbc.host") + "/" + prop_file.getProperty("jdbc.database"), props);
	conn.setAutoCommit(true);
    }

    public static void truncate() throws SQLException {
	logger.info("truncating tables...");
	conn.prepareStatement("truncate github.users_json cascade").execute();
	conn.prepareStatement("truncate github.user_json cascade").execute();

	conn.prepareStatement("truncate github.repos_json cascade").execute();

	conn.prepareStatement("truncate github.commits_json cascade").execute();
    }

    public static void refresh() throws SQLException {
	logger.info("refreshing materialized views...");
	logger.info("\tuser views...");
	conn.prepareStatement("refresh materialized view github.user_suppress").execute();
	conn.prepareStatement("refresh materialized view github.user_jsonb").execute();
	conn.prepareStatement("refresh materialized view github.user").execute();

	logger.info("\trepository views...");
	conn.prepareStatement("refresh materialized view github.repos_jsonb").execute();
	conn.prepareStatement("refresh materialized view github.repository").execute();

	logger.info("\torganization views...");
	conn.prepareStatement("refresh materialized view github.org_jsonb").execute();
	conn.prepareStatement("refresh materialized view github.organization").execute();

	logger.info("\tcommit views...");
	conn.prepareStatement("refresh materialized view github.commits_jsonb").execute();

	logger.info("\trelationship views...");
	conn.prepareStatement("refresh materialized view github.user_repo").execute();
	conn.prepareStatement("refresh materialized view github.org_repo").execute();
    }

    static void fullScan() throws IOException, SQLException {
	// this scans for existence of users
	scanUsers();
	// this scans for user profiles not yet retrieved
	scanUser();
	// this scans for users' repo profiles not yet retrieved
	scanRepos();
    }

    static void searchScan() throws SQLException, IOException {
	PreparedStatement stmt = conn
		.prepareStatement("select id,term from github.search_term" + (searchID == 0 ? "" : " where id = " + searchID) + " order by id");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String term = URLEncoder.encode(rs.getString(2), "UTF-8");
	    searchScanUsers(id, term);
	    searchScanRepositories(id, term);
	    PreparedStatement updateStmt = conn.prepareStatement("update github.search_term set last_run = now() where id = ?");
	    updateStmt.setInt(1, id);
	    updateStmt.execute();
	    updateStmt.close();
	}
	stmt.close();
    }

    static void newSearchScan() throws SQLException, IOException {
	PreparedStatement stmt = conn.prepareStatement("select id,term from github.search_term" + " where last_run is null");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String term = URLEncoder.encode(rs.getString(2), "UTF-8");
	    searchScanUsers(id, term);
	    searchScanRepositories(id, term);
	    PreparedStatement updateStmt = conn.prepareStatement("update github.search_term set last_run = now() where id = ?");
	    updateStmt.setInt(1, id);
	    updateStmt.execute();
	    updateStmt.close();
	}
	stmt.close();
    }

    static void loadLoginHash() throws SQLException {
	PreparedStatement stmt = conn.prepareStatement("select login,count(*) from github.repos_json group by 1 having count(*) > 1000 order by 2 desc");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String login = rs.getString(1);
	    int count = rs.getInt(2);
	    logger.info("suppressing " + login + " (" + count + ")");
	    loginHash.put(login, login);
	}
	stmt.close();
    }

    static void rateLimitSearch() {
	if (++queryCount < 25) {
	    return;
	}
	logger.info("quiescing search for a minute...");
	try {
	    Thread.sleep(1 * 60 * 1000);
	} catch (InterruptedException e) {
	}
	queryCount = 0;
    }

    static void rateLimitAPI() {
	if (++apiCount < 4950) {
	    return;
	}
	logger.info("quiescing api for an hour...");
	try {
	    Thread.sleep(60 * 60 * 1000);
	} catch (InterruptedException e) {
	}
	apiCount = 0;
    }

    static void searchScanUsers(int sid, String term) throws IOException, SQLException {
	int hitCount = Integer.MAX_VALUE;
	int retrievedCount = 0;
	int pageCount = 1;
	while (retrievedCount < hitCount && retrievedCount < 1000) {
	    rateLimitSearch();
	    logger.info("scanning users for search term: " + term + "\tpage: " + pageCount);
	    URL theURL = new URL("https://api.github.com/search/users?q=" + term + "&page=" + pageCount + "&per_page=100" + "&client_id=" + client_id
		    + "&client_secret=" + client_secret);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	    JSONObject results = new JSONObject(new JSONTokener(reader));
	    hitCount = results.getInt("total_count");
	    if (retrievedCount == 0)
		logger.info("hitCount: " + hitCount);

	    JSONArray users = results.getJSONArray("items");
	    for (int i = 0; i < users.length(); i++) {
		JSONObject user = users.getJSONObject(i);
		String login = user.getString("login");
		int id = user.getInt("id");
		String type = user.getString("type");
		logger.debug("login: " + login + "\ttype: " + type);
		retrievedCount++;
		if (!loginHash.containsKey(login)) {
		    if (type.equals("User"))
			scanUser(login);
		    else
			scanOrg(login);
		    scanRepos(login);
		    loginHash.put(login, login);
		}
		if (type.equals("User"))
		    storeUserSearchHit(sid, id, retrievedCount);
		else
		    storeOrganizationSearchHit(sid, id, retrievedCount);
	    }

	    pageCount++;
	}
    }

    static void storeUserSearchHit(int sid, int uid, int rank) throws SQLException {
	PreparedStatement stmt;
	try {
	    stmt = conn.prepareStatement("insert into github.search_user values(?,?,?)");
	    stmt.setInt(1, sid);
	    stmt.setInt(2, uid);
	    stmt.setInt(3, rank);
	    stmt.execute();
	    stmt.close();
	} catch (SQLException e) {
	    try {
		stmt = conn.prepareStatement("update github.search_user set rank = ? where sid = ? and uid = ?");
		stmt.setInt(1, rank);
		stmt.setInt(2, sid);
		stmt.setInt(3, uid);
		stmt.execute();
		stmt.close();
	    } catch (SQLException e2) {
	    }
	}
    }

    static void storeOrganizationSearchHit(int sid, int uid, int rank) throws SQLException {
	PreparedStatement stmt;
	try {
	    stmt = conn.prepareStatement("insert into github.search_organization values(?,?,?)");
	    stmt.setInt(1, sid);
	    stmt.setInt(2, uid);
	    stmt.setInt(3, rank);
	    stmt.execute();
	    stmt.close();
	} catch (SQLException e) {
	    try {
		stmt = conn.prepareStatement("update github.search_organization set rank = ? where sid = ? and uid = ?");
		stmt.setInt(1, rank);
		stmt.setInt(2, sid);
		stmt.setInt(3, uid);
		stmt.execute();
		stmt.close();
	    } catch (SQLException e2) {
	    }
	}
    }

    static void searchScanRepositories(int sid, String term) throws IOException, SQLException {
	int hitCount = Integer.MAX_VALUE;
	int retrievedCount = 0;
	int pageCount = 1;
	while (retrievedCount < hitCount && retrievedCount < 1000) {
	    rateLimitSearch();
	    logger.info("scanning repos for search term: " + term + "\tpage: " + pageCount);
	    URL theURL = new URL("https://api.github.com/search/repositories?q=" + term + "&page=" + pageCount + "&per_page=100" + "&client_id=" + client_id
		    + "&client_secret=" + client_secret);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	    JSONObject results = new JSONObject(new JSONTokener(reader));
	    hitCount = results.getInt("total_count");
	    if (retrievedCount == 0)
		logger.info("hitCount: " + hitCount);

	    JSONArray repos = results.getJSONArray("items");
	    for (int i = 0; i < repos.length(); i++) {
		JSONObject repo = repos.getJSONObject(i);
		JSONObject owner = repo.getJSONObject("owner");
		String login = owner.getString("login");
		String type = owner.getString("type");
		String full_name = repo.getString("full_name");
		int id = repo.getInt("id");
		logger.debug("full: " + full_name + "\towner: " + login + "\ttype: " + type);
		retrievedCount++;
		if (!loginHash.containsKey(login)) {
		    if (type.equals("User"))
			scanUser(login);
		    else
			scanOrg(login);
		    scanRepos(login);
		    loginHash.put(login, login);
		}
		storeRepositorySearchHit(sid, id, retrievedCount);
	    }

	    pageCount++;
	}
    }

    static void storeRepositorySearchHit(int sid, int rid, int rank) throws SQLException {
	PreparedStatement stmt;
	try {
	    stmt = conn.prepareStatement("insert into github.search_repository values(?,?,?)");
	    stmt.setInt(1, sid);
	    stmt.setInt(2, rid);
	    stmt.setInt(3, rank);
	    stmt.execute();
	    stmt.close();
	} catch (SQLException e) {
	    try {
		stmt = conn.prepareStatement("update github.search_repository set rank = ? where sid = ? and uid = ?");
		stmt.setInt(1, rank);
		stmt.setInt(2, sid);
		stmt.setInt(3, rid);
		stmt.execute();
		stmt.close();
	    } catch (SQLException e2) {
	    }
	}
    }

    static int initializeUserSince() throws SQLException {
	int since = 0;

	PreparedStatement stmt = conn.prepareStatement("select max(id) from github.users_json");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    since = rs.getInt(1);
	}
	stmt.close();

	return since;
    }

    static boolean newUser(String login) throws SQLException {
	boolean flag = true;
	PreparedStatement stmt = conn.prepareStatement("select id from github.user_json where login = ?");
	stmt.setString(1, login);
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    flag = false;
	}
	stmt.close();

	return flag;
    }

    static boolean newRepo(String login, String name) throws SQLException {
	boolean flag = true;
	PreparedStatement stmt = conn.prepareStatement("select id from github.repos_json where login = ? and name = ?");
	stmt.setString(1, login);
	stmt.setString(2, name);
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    flag = false;
	}
	stmt.close();
	logger.debug("new repo: " + login + " - " + name + " : " + flag);
	return flag;
    }

    static boolean newOrg(String login) throws SQLException {
	boolean flag = true;
	PreparedStatement stmt = conn.prepareStatement("select id from github.org_json where login = ?");
	stmt.setString(1, login);
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    flag = false;
	}
	stmt.close();

	return flag;
    }

    static void scanUsers() throws IOException, SQLException {
	int since = initializeUserSince();
	boolean foundUser = true;
	while (foundUser) {
	    logger.info("since: " + since);
	    rateLimitAPI();
	    URL theURL = since == 0 ? new URL("https://api.github.com/users" + "?client_id=" + client_id + "&client_secret=" + client_secret)
		    : new URL(
			    "https://api.github.com/users?page=1&per_page=100&since=" + since + "&client_id=" + client_id + "&client_secret=" + client_secret);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	    JSONArray users = new JSONArray(new JSONTokener(reader));
	    // logger.info("users:" + users);
	    foundUser = false;
	    for (int i = 0; i < users.length(); i++) {
		JSONObject user = users.getJSONObject(i);
		String login = user.getString("login");
		int id = user.getInt("id");
		if (!newUser(login))
		    continue;
		logger.info("user: " + id + " : " + login);
		foundUser = true;
		PreparedStatement stmt = conn.prepareStatement("insert into github.users_json(id,login,json) values(?,?,?)");
		stmt.setInt(1, id);
		stmt.setString(2, login);
		stmt.setString(3, user.toString().replace('\u0000', ' '));
		stmt.execute();
		stmt.close();
		since = id;
	    }
	}
    }

    static void scanUser() throws SQLException, IOException {
	PreparedStatement stmt = conn.prepareStatement("select login from github.users_json where id not in (select id from github.user_json) order by id");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String user = rs.getString(1);
	    logger.info("querying for: " + user);
	    scanUser(user);
	}
	stmt.close();
    }

    static void scanUser(String login) throws IOException, SQLException {
	if (!newUser(login))
	    return;

	rateLimitAPI();
	URL theURL = new URL("https://api.github.com/users/" + login + "?client_id=" + client_id + "&client_secret=" + client_secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONObject user = new JSONObject(new JSONTokener(reader));
	int id = user.getInt("id");
	logger.info("user: " + id + " : " + login);
	PreparedStatement stmt = conn.prepareStatement("insert into github.user_json(id,login,json) values(?,?,?)");
	stmt.setInt(1, id);
	stmt.setString(2, login);
	stmt.setString(3, user.toString().replace('\u0000', ' '));
	stmt.execute();
	stmt.close();
    }

    static void scanRepos() throws IOException, SQLException {
	PreparedStatement stmt = conn
		.prepareStatement("select login from github.users_json where login not in (select login from github.repos_json) order by id");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String user = rs.getString(1);
	    logger.info("querying for: " + user);
	    scanRepos(user);
	}
	stmt.close();
    }

    static void scanRepos(String login) throws IOException, SQLException {
	int retrievedCount = 100;
	int pageCount = 1;
	while (retrievedCount == 100) {
	    rateLimitAPI();
	    URL theURL = new URL("https://api.github.com/users/" + login + "/repos" + "?client_id=" + client_id + "&client_secret=" + client_secret + "&page="
		    + pageCount + "&per_page=100");
	    BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	    JSONArray repos = new JSONArray(new JSONTokener(reader));
	    // logger.info("repos:" + repos);
	    retrievedCount = 0;
	    for (int i = 0; i < repos.length(); i++) {
		JSONObject repo = repos.getJSONObject(i);
		String name = repo.getString("name");
		int id = repo.getInt("id");
		retrievedCount++;
		if (newRepo(login, name)) {
		    logger.info("\t\trepo: " + id + " : " + name);
		    PreparedStatement stmt = conn.prepareStatement("insert into github.repos_json(id,login,name,json) values(?,?,?,?)");
		    stmt.setInt(1, id);
		    stmt.setString(2, login);
		    stmt.setString(3, name);
		    stmt.setString(4, repo.toString().replace('\u0000', ' '));
		    stmt.execute();
		    stmt.close();
		} else {
		    logger.debug("\t\texists...");
		}
	    }

	    if (repos.length() == 0 && newRepo(login, null)) {
		PreparedStatement stmt = conn.prepareStatement("insert into github.repos_json(id,login,name,json) values(?,?,?,?)");
		stmt.setInt(1, 0);
		stmt.setString(2, login);
		stmt.setString(3, null);
		stmt.setString(4, null);
		stmt.execute();
		stmt.close();
	    }

	    pageCount++;
	}
    }

    static void scanUserOrgs() throws IOException, SQLException {
	PreparedStatement stmt = conn.prepareStatement("select id,login from github.user_json where"
		+ (searchID == 0 ? "" : " id in (select uid from github.search_user where sid=" + searchID + ") and")
		+ " login not in (select login from github.orgs_json) order by id desc");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String user = rs.getString(2);
	    logger.info("querying for: " + user);
	    try {
		scanUserOrgs(id, user);
	    } catch (java.io.FileNotFoundException e) {
		logger.error("error scannon orgs for " + user, e);
	    }
	}
	stmt.close();
    }

    static void scanUserOrgs(int user_id, String user_login) throws IOException, SQLException {
	rateLimitAPI();
	URL theURL = new URL("https://api.github.com/users/" + user_login + "/orgs" + "?client_id=" + client_id + "&client_secret=" + client_secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONArray orgs = new JSONArray(new JSONTokener(reader));

	PreparedStatement stmt = conn.prepareStatement("insert into github.orgs_json(id,login,json) values(?,?,?)");
	stmt.setInt(1, user_id);
	stmt.setString(2, user_login);
	stmt.setString(3, orgs.toString().replace('\u0000', ' '));
	stmt.execute();
	stmt.close();

	for (int i = 0; i < orgs.length(); i++) {
	    JSONObject org = orgs.getJSONObject(i);
	    String login = org.getString("login");
	    int id = org.getInt("id");
	    if (newOrg(login)) {
		logger.info("\t\torg: " + id + " : " + login + " : " + org);
		scanOrg(login);
	    }
	    storeMember(user_id, id);
	}
    }

    static void scanOrgMembers() throws IOException, SQLException {
	PreparedStatement stmt = conn.prepareStatement("select id,login from github.org_json where"
		+ (searchID == 0 ? "" : " id in (select orgid from github.search_organization where sid=" + searchID + ") and")
		+ " login not in (select login from github.members_json) order by id desc");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String org = rs.getString(2);
	    logger.info("querying for: " + org);
	    try {
		scanOrgMembers(id, org);
	    } catch (java.io.FileNotFoundException e) {
		logger.error("error scanning members for " + org, e);
	    }
	}
	stmt.close();
    }

    static void scanOrgMembers(int org_id, String org_login) throws IOException, SQLException {
	int retrievedCount = 100;
	int pageCount = 1;
	while (retrievedCount == 100) {
	    rateLimitAPI();
	    BufferedReader reader = null;
	    try {
		URL theURL = new URL("https://api.github.com/orgs/" + org_login + "/members" + "?client_id=" + client_id + "&client_secret=" + client_secret
			+ "&page=" + pageCount + "&per_page=100");
		reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));
	    } catch (Exception e) {
		return;
	    }

	    JSONArray orgs = new JSONArray(new JSONTokener(reader));
	    retrievedCount = 0;

	    PreparedStatement stmt = conn.prepareStatement("insert into github.members_json(id,login,json) values(?,?,?)");
	    stmt.setInt(1, org_id);
	    stmt.setString(2, org_login);
	    stmt.setString(3, orgs.toString().replace('\u0000', ' '));
	    stmt.execute();
	    stmt.close();

	    for (int i = 0; i < orgs.length(); i++) {
		JSONObject user = orgs.getJSONObject(i);
		String login = user.getString("login");
		int id = user.getInt("id");
		retrievedCount++;
		if (newUser(login)) {
		    logger.info("\t\tuser: " + id + " : " + login + " : " + user);
		    scanUser(login);
		}
		storeMember(id, org_id);
	    }

	    pageCount++;
	}
    }

    static void storeMember(int uid, int orgid) throws SQLException {
	PreparedStatement stmt;
	try {
	    stmt = conn.prepareStatement("insert into github.member values(?,?)");
	    stmt.setInt(1, uid);
	    stmt.setInt(2, orgid);
	    stmt.execute();
	    stmt.close();
	} catch (SQLException e) {
	}
    }

    static void scanOrg(String login) throws IOException, SQLException {
	if (!newOrg(login))
	    return;

	rateLimitAPI();
	URL theURL = new URL("https://api.github.com/orgs/" + login + "?client_id=" + client_id + "&client_secret=" + client_secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONObject org = new JSONObject(new JSONTokener(reader));
	int id = org.getInt("id");
	logger.info("org: " + id + " : " + login);
	PreparedStatement stmt = conn.prepareStatement("insert into github.org_json(id,login,json) values(?,?,?)");
	stmt.setInt(1, id);
	stmt.setString(2, login);
	stmt.setString(3, org.toString().replace('\u0000', ' '));
	stmt.execute();
	stmt.close();
    }

    static void readmeScan() throws SQLException, IOException {
	// readmeScan("select id,full_name from github.repos_json where id > 0
	// and not exists (select rid from github.search_repository where
	// rid=id) and id not in (select id from github.readme) order by
	// updated_at desc");
	readmeScan("select id,login,name from github.repos_json where" + " id > 0 and" + " id not in (select id from github.readme)"
		+ (searchID == 0 ? "" : " and id in (select rid from github.search_repository where sid=" + searchID + ")") + " order by id desc");
    }

    static void readmeScan(String queryString) throws SQLException, IOException {
	PreparedStatement stmt = conn.prepareStatement(queryString);
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String login = rs.getString(2);
	    String repo = login + "/" + rs.getString(3);
	    logger.info("querying for: " + repo);
	    readmeScan(id, repo);
	}
	stmt.close();
    }

    static void readmeScan(int id, String name) throws IOException, SQLException {
	String readme = null;
	JSONObject container;
	rateLimitAPI();
	try {
	    URL theURL = new URL(
		    "https://api.github.com/repos/" + name + "/contents/README.md" + "?client_id=" + client_id + "&client_secret=" + client_secret);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	    container = new JSONObject(new JSONTokener(reader));
	    readme = decode(container.getString("content"));
	} catch (Exception e) {
	}
	if (readme != null)
	    readme = readme.replace('\u0000', ' ');
	logger.info("repo: " + id + " : " + readme);
	PreparedStatement stmt = conn.prepareStatement("insert into github.readme(id,readme) values(?,?)");
	stmt.setInt(1, id);
	stmt.setString(2, readme);
	stmt.execute();
	stmt.close();
    }

    static void commitScan() throws SQLException, IOException {
	conn.setAutoCommit(false);
	PreparedStatement stmt = conn.prepareStatement("select id,login,name from github.repos_json"
		+ (searchID == 0 ? "" : " where id in (select rid from github.search_repository where sid=" + searchID + ")") + " order by id desc");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String login = rs.getString(2);
	    String name = login + "/" + rs.getString(3);
	    rateLimitAPI();
	    logger.info("querying for: " + name);
	    commitScan(id, name);
	    conn.commit();
	}
	stmt.close();
    }

    static void commitScan(int id, String repo_name) throws IOException, SQLException {
	int retrievedCount = 100;
	int pageCount = 1;
	while (retrievedCount == 100) {
	    rateLimitAPI();
	    BufferedReader reader;
	    try {
		URL theURL = new URL("https://api.github.com/repos/" + repo_name + "/commits" + "?client_id=" + client_id + "&client_secret=" + client_secret
			+ "&page=" + pageCount + "&per_page=100");
		reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));
	    } catch (Exception e1) {
		return;
	    }

	    JSONArray commits = new JSONArray(new JSONTokener(reader));
	    retrievedCount = 0;

	    for (int i = 0; i < commits.length(); i++) {
		JSONObject commit = commits.getJSONObject(i);
		JSONObject commit_author = commit.getJSONObject("commit").getJSONObject("author");
		String name = null;
		String email = null;
		String date = null;
		if (commit_author != null) {
		    name = commit_author.getString("name");
		    email = commit_author.getString("email");
		    date = commit_author.getString("date");
		}
		String message = commit.getJSONObject("commit").getString("message");
		JSONObject author = commit.optJSONObject("author");
		int uid = 0;
		String login = null;
		if (author != null) {
		    uid = author.optInt("id");
		    login = author.optString("login");
		}
		retrievedCount++;

		try {
		    PreparedStatement stmt = conn
			    .prepareStatement("insert into github.commit(id,committed,name,email,user_id,login,message) values(?,?::timestamp,?,?,?,?,?)");
		    stmt.setInt(1, id);
		    stmt.setString(2, date);
		    stmt.setString(3, name);
		    stmt.setString(4, email);
		    if (uid == 0)
			stmt.setNull(5, Types.INTEGER);
		    else
			stmt.setInt(5, uid);
		    stmt.setString(6, login);
		    stmt.setString(7, message);
		    stmt.execute();
		    stmt.close();
		} catch (SQLException e) {
		    logger.info("\tcommits: " + i);
		    return; // we've reached already logged commits
		}

	    }

	    pageCount++;
	}
    }

    static void parentScan() throws SQLException, IOException {
	PreparedStatement stmt = conn.prepareStatement("select id,full_name from github.repository where fork" + " and id not in (select id from github.parent)"
		+ (searchID == 0 ? "" : " and id in (select rid from github.search_repository where sid=" + searchID + ")") + " order by id desc");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String repo = rs.getString(2);
	    logger.info("querying for: " + repo);
	    parentScan(id, repo);
	}
	stmt.close();
    }

    static void parentScan(int id, String name) throws IOException, SQLException {
	rateLimitAPI();
	HttpURLConnection connection = null;
	BufferedReader reader = null;
	try {
	    URL theURL = new URL("https://api.github.com/repos/" + name + "?client_id=" + client_id + "&client_secret=" + client_secret);
	    connection = (HttpURLConnection) theURL.openConnection();
	    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	} catch (FileNotFoundException e1) {
	    PreparedStatement stmt = conn.prepareStatement("insert into github.parent(id,parent_id,parent_full_name) values(?,?,?)");
	    stmt.setInt(1, id);
	    stmt.setNull(2, Types.INTEGER);
	    stmt.setString(3, null);
	    stmt.execute();
	    stmt.close();
	    return;
	} catch (IOException ioe) {
	    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
	    JSONObject error = new JSONObject(new JSONTokener(reader));
	    logger.error("error message: " + error);
	    if (!error.has("block"))
		throw (ioe);
	    PreparedStatement stmt = conn.prepareStatement("insert into github.parent(id,parent_id,parent_full_name) values(?,?,?)");
	    stmt.setInt(1, id);
	    stmt.setNull(2, Types.INTEGER);
	    stmt.setString(3, null);
	    stmt.execute();
	    stmt.close();
	    return;
	}

	JSONObject parent = new JSONObject(new JSONTokener(reader)).optJSONObject("parent");

	if (parent == null)
	    return;

	int parent_id = parent.getInt("id");
	String owner = parent.getJSONObject("owner").getString("login");
	String type = parent.getJSONObject("owner").getString("type");
	String parent_full_name = parent.getString("full_name");
	logger.debug("parent: " + parent_id + " : " + owner + " : " + type + " : " + parent_full_name);

	try {
	    PreparedStatement stmt = conn.prepareStatement("insert into github.parent(id,parent_id,parent_full_name) values(?,?,?)");
	    stmt.setInt(1, id);
	    stmt.setInt(2, parent_id);
	    stmt.setString(3, parent_full_name);
	    stmt.execute();
	    stmt.close();
	} catch (SQLException e) {
	}

	if (!loginHash.containsKey(owner)) {
	    if (type.equals("User"))
		scanUser(owner);
	    else
		scanOrg(owner);
	    scanRepos(owner);
	    loginHash.put(owner, owner);
	}
    }

    static String decode(String base64string) {
	return new String(decoder.decode(base64string));
    }

}
