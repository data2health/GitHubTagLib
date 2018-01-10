package edu.uiowa.slis.GitHubTagLib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Base64.Decoder;
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
    static enum modes {SEARCH, FULL, ORGS, REFRESH, README};
    static modes mode = modes.README;
    static int queryCount = 0;
    static int apiCount = 0;
    
    static Decoder decoder = Base64.getMimeDecoder();
    
    static String client_id = "";
    static String client_secret = "";
    // curl 'https://api.github.com/users/whatever?client_id=xxxx&client_secret=yyyy'

    public static void main(String[] args) throws Exception {
	PropertyConfigurator.configure(args[0]);
	prop_file = PropertyLoader.loadProperties("github");
	client_id = prop_file.getProperty("client_id");
	client_secret = prop_file.getProperty("client_secret");
	getConnection();
	
	if (args.length > 1)
	    switch(args[1]) {
	    case "search":
		mode = modes.SEARCH;
		break;
	    case "full":
		mode = modes.FULL;
		break;
	    case "orgs":
		mode = modes.ORGS;
		break;
	    case "refresh":
		mode = modes.REFRESH;
		break;
	    case "readme":
		mode = modes.README;
		break;
	    }

	// truncate();

	boolean exceptionRaised = true;
	while (exceptionRaised) {
	    exceptionRaised = false;
	    try {
		switch (mode) {
		case SEARCH:
		    searchScan();
		    break;
		case FULL:
		    fullScan();
		    break;
		case ORGS:
		    scanUserOrgs();
		    break;
		case REFRESH:
		    refresh();
		    break;
		case README:
		    readmeScan();
		    break;
		}
	    } catch (Exception e) {
		logger.error("Exception raised:", e);
		exceptionRaised = true;
		logger.info("exception raised! Sleeping for an hour...");
		Thread.sleep(60 * 60 * 1000);
	    }
	}

//	refresh();
	logger.info("done");
    }
    
    static void getConnection() throws ClassNotFoundException, SQLException {
	logger.info("connecting to database...");
	Class.forName("org.postgresql.Driver");
	Properties props = new Properties();
	props.setProperty("user", prop_file.getProperty("jdbc.user"));
	props.setProperty("password", prop_file.getProperty("jdbc.password"));
	conn = DriverManager.getConnection("jdbc:postgresql://"+prop_file.getProperty("jdbc.host")+"/"+prop_file.getProperty("jdbc.database"), props);
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
//	conn.prepareStatement("refresh materialized view github.users_jsonb").execute();
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
	PreparedStatement stmt = conn.prepareStatement("select id,term from github.search_term");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String term = rs.getString(2);
	    searchScanUsers(id, term);
	    searchScanRepositories(id, term);
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
	    URL theURL = new URL("https://api.github.com/search/users?q=" + term + "&page=" + pageCount + "&per_page=100"
		    			+ "&client_id=" + client_id + "&client_secret=" + client_secret);
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
		retrievedCount++;
		storeUserSearchHit(sid,id,retrievedCount);
		if (newUser(login)) {
		    logger.info("\tuser: " + id + " : " + login);
		    scanUser(login);
		    scanRepos(login);
		}
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
	    stmt.setInt(3, sid);
	    stmt.execute();
	    stmt.close();
	} catch (SQLException e) {
	    conn.rollback();;
	}
    }
    
    static void searchScanRepositories(int sid, String term) throws IOException, SQLException {
	int hitCount = Integer.MAX_VALUE;
	int retrievedCount = 0;
	int pageCount = 1;
	while (retrievedCount < hitCount && retrievedCount < 1000) {
	    rateLimitSearch();
	    logger.info("scanning repos for search term: " + term + "\tpage: " + pageCount);
	    URL theURL = new URL("https://api.github.com/search/repositories?q=" + term + "&page=" + pageCount + "&per_page=100"
		    			+ "&client_id=" + client_id + "&client_secret=" + client_secret);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	    JSONObject results = new JSONObject(new JSONTokener(reader));
	    hitCount = results.getInt("total_count");
	    if (retrievedCount == 0)
		logger.info("hitCount: " + hitCount);
	    
	    JSONArray users = results.getJSONArray("items");
	    for (int i = 0; i < users.length(); i++) {
		JSONObject user = users.getJSONObject(i);
		String full_name = user.getString("full_name");
		String login = full_name.substring(0, full_name.indexOf('/'));
		int id = user.getInt("id");
		retrievedCount++;
		storeRepositorySearchHit(sid,id,retrievedCount);
		if (newUser(login)) {
		    logger.info("\trepo: " + full_name + " : " + login);
		    scanUser(login);
		    scanRepos(login);
		}
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
	    conn.rollback();;
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
	    URL theURL = since == 0 ? new URL("https://api.github.com/users"
							+ "?client_id=" + client_id + "&client_secret=" + client_secret)
					: new URL("https://api.github.com/users?page=1&per_page=100&since=" + since
						+ "&client_id=" + client_id + "&client_secret=" + client_secret);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	    JSONArray users = new JSONArray(new JSONTokener(reader));
	    // logger.info("users:" + users);
	    foundUser = false;
	    for (int i = 0; i < users.length(); i++) {
		JSONObject user = users.getJSONObject(i);
		String login = user.getString("login");
		int id = user.getInt("id");
		logger.info("user: " + id + " : " + login);
		foundUser = true;
		PreparedStatement stmt = conn.prepareStatement("insert into github.users_json(id,login,json) values(?,?,?)");
		stmt.setInt(1, id);
		stmt.setString(2, login);
		stmt.setString(3, user.toString());
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
	URL theURL = new URL("https://api.github.com/users/" + login
		+ "?client_id=" + client_id + "&client_secret=" + client_secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONObject user = new JSONObject(new JSONTokener(reader));
	int id = user.getInt("id");
	logger.info("user: " + id + " : " + login);
	PreparedStatement stmt = conn.prepareStatement("insert into github.user_json(id,login,json) values(?,?,?)");
	stmt.setInt(1, id);
	stmt.setString(2, login);
	stmt.setString(3, user.toString());
	stmt.execute();
	stmt.close();
    }

    static void scanRepos() throws IOException, SQLException {
	PreparedStatement stmt = conn.prepareStatement("select login from github.users_json where login not in (select login from github.repos_json) order by id");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String user = rs.getString(1);
	    logger.info("querying for: " + user);
	    scanRepos(user);
	}
	stmt.close();
    }

    static void scanRepos(String login) throws IOException, SQLException {
	URL theURL = new URL("https://api.github.com/users/" + login + "/repos"
		+ "?client_id=" + client_id + "&client_secret=" + client_secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONArray repos = new JSONArray(new JSONTokener(reader));
//	logger.info("repos:" + repos);
	for (int i = 0; i < repos.length(); i++) {
	    JSONObject repo = repos.getJSONObject(i);
	    String name = repo.getString("name");
	    int id = repo.getInt("id");
	    if (newRepo(login, name)) {
		logger.info("\t\trepo: " + id + " : " + name);
		PreparedStatement stmt = conn.prepareStatement("insert into github.repos_json(id,login,name,json) values(?,?,?,?)");
		stmt.setInt(1, id);
		stmt.setString(2, login);
		stmt.setString(3, name);
		stmt.setString(4, repo.toString());
		stmt.execute();
		stmt.close();
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
    }

    static void scanUserOrgs() throws IOException, SQLException {
	PreparedStatement stmt = conn.prepareStatement("select id,login from github.user_json where login not in (select login from github.orgs_json) order by id desc");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String user = rs.getString(2);
	    logger.info("querying for: " + user);
	    try {
		scanUserOrgs(id,user);
	    } catch (java.io.FileNotFoundException e) {
		logger.error("error scannon orgs for " + user, e);
	    }
	}
	stmt.close();
    }

    static void scanUserOrgs(int user_id, String user_login) throws IOException, SQLException {
	rateLimitAPI();
	URL theURL = new URL("https://api.github.com/users/" + user_login + "/orgs"
		+ "?client_id=" + client_id + "&client_secret=" + client_secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONArray orgs = new JSONArray(new JSONTokener(reader));

	PreparedStatement stmt = conn.prepareStatement("insert into github.orgs_json(id,login,json) values(?,?,?)");
	stmt.setInt(1, user_id);
	stmt.setString(2, user_login);
	stmt.setString(3, orgs.toString());
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
	}
    }

    static void scanOrg(String login) throws IOException, SQLException {
	rateLimitAPI();
	URL theURL = new URL("https://api.github.com/orgs/" + login
		+ "?client_id=" + client_id + "&client_secret=" + client_secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONObject org = new JSONObject(new JSONTokener(reader));
	int id = org.getInt("id");
	logger.info("org: " + id + " : " + login);
	PreparedStatement stmt = conn.prepareStatement("insert into github.org_json(id,login,json) values(?,?,?)");
	stmt.setInt(1, id);
	stmt.setString(2, login);
	stmt.setString(3, org.toString());
	stmt.execute();
	stmt.close();
    }

    static void readmeScan() throws SQLException, IOException {
	PreparedStatement stmt = conn.prepareStatement("select id,full_name from github.repository where id > 0 and id not in (select id from github.readme) order by id desc");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String repo = rs.getString(2);
	    rateLimitAPI();
	    logger.info("querying for: " + repo);
	    readmeScan(id, repo);
	}
	stmt.close();
    }

    static void readmeScan(int id, String name) throws IOException, SQLException {
	String readme = null;
	JSONObject container;
	try {
	    URL theURL = new URL("https://api.github.com/repos/" + name + "/contents/README.md"
	    	+ "?client_id=" + client_id + "&client_secret=" + client_secret);
	    BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	    container = new JSONObject(new JSONTokener(reader));
	    readme = decode(container.getString("content"));
	} catch (Exception e) {
	}
	if (readme != null)
	    readme = readme.replace((char)0x00, ' ');
	logger.info("repo: " + id + " : " + readme);
	PreparedStatement stmt = conn.prepareStatement("insert into github.readme(id,readme) values(?,?)");
	stmt.setInt(1, id);
	stmt.setString(2, readme);
	stmt.execute();
	stmt.close();
    }

    static void scanCommits(String owner, String name) throws IOException, SQLException {
	URL theURL = new URL("https://api.github.com/users");
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONArray commits = new JSONArray(new JSONTokener(reader));
//	logger.info("commits:" + commits);
	for (int i = 0; i < commits.length(); i++) {
	    JSONObject commit = commits.getJSONObject(i);
	    logger.info("commit: " + owner + " : " + name);
		PreparedStatement stmt = conn.prepareStatement("insert into github.commits_json(owner,name,json) values(?,?,?)");
		stmt.setString(1, owner);
		stmt.setString(2, name);
		stmt.setString(3, commit.toString());
		stmt.execute();
		stmt.close();
	}
    }
    
    static String decode(String base64string) {
	return new String(decoder.decode(base64string));
    }

}
