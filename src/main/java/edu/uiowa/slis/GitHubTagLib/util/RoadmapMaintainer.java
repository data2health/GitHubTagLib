package edu.uiowa.slis.GitHubTagLib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.cd2h.JSONTagLib.GraphQL.GitHubAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoadmapMaintainer {
    static Logger logger = Logger.getLogger(RoadmapMaintainer.class);
    protected static LocalProperties prop_file = null;
    static Connection conn = null;
    static boolean useAPI = true;

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
	prop_file = PropertyLoader.loadProperties("github");
	getConnection();
	
	regenerateRoadmap();
    }
	
    static void regenerateRoadmap() throws IOException, SQLException {
	boolean inPrefix = true;
	boolean inList = true;
	boolean databaseCalled = false;
	String originalContent = Content.read("data2health", "roadmap", "README.md");
	StringBuffer newContent = new StringBuffer();
	String buffer = null;
	BufferedReader reader = new BufferedReader(new StringReader(originalContent));
	while ((buffer = reader.readLine()) != null) {
	    logger.debug("buffer: " + buffer);
	    if (inPrefix) {
		newContent.append(buffer + "\n");
		if (buffer.equals("# Phase 2 Projects"))
		    inPrefix = false;
	    }
	    if (!inPrefix && inList) {
		if (!buffer.equals("# Phase 2 Projects") && buffer.startsWith("# ")) {
		    inList = false;
		    newContent.append("\n");
		}
		if (!databaseCalled) {
		    logger.info("calling API/database..,");
		    // call the API or the database for repo list
		    if (useAPI)
			injectProjectsFromAPI(newContent);
		    else
			injectProjectsFromDatabase(newContent);
		    databaseCalled = true;
		}
	    }
	    if (!inPrefix && !inList) {
		newContent.append(buffer + "\n");
	    }
	}
	logger.debug("final result:\n" + newContent);
	Content.replace("data2health", "roadmap", "README.md", newContent.toString(), "refreshed by RoadmapMaintainer");
    }
    
    static void injectProjectsFromDatabase(StringBuffer buffer) throws SQLException {
	PreparedStatement stmt = conn.prepareStatement("select repo_name,updated_title,actual_github_repo_link from dashboard.dashboard where actual_github_repo_link is not null order by lower(repo_name)");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String repo = rs.getString(1);
	    String title = rs.getString(2);
	    String url = rs.getString(3);
	    buffer.append("## [" + repo + "](" + url + ")\n");
	    buffer.append(title + "\n");
	}
	stmt.close();
    }

    static void injectProjectsFromAPI(StringBuffer buffer) throws SQLException, IOException {
	GitHubAPI theAPI = new GitHubAPI();
	JSONObject results =theAPI.submitSearch(theAPI.getStatement("roadmapList"));
	JSONArray array = results.getJSONObject("data").getJSONObject("search").getJSONArray("nodes");
	sort(array);
	logger.debug("result:\n" + array.toString(3));
	for (int i = 0; i < array.length(); i++) {
	    JSONObject repo = array.getJSONObject(i);
	    buffer.append("## [" + repo.getString("name") + "](" + repo.getString("url") + ")\n");
	    buffer.append(repo.optString("description") + "\n");
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
    
    static void sort(JSONArray jsonArr) {

	List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	while (jsonArr.length() > 0)
	    jsonValues.add((JSONObject)jsonArr.remove(0));

	Collections.sort(jsonValues, new Comparator<JSONObject>() {
	    // You can change "Name" with "ID" if you want to sort by ID
	    private static final String KEY_NAME = "name";

	    @Override
	    public int compare(JSONObject a, JSONObject b) {
		String valA = new String();
		String valB = new String();

		try {
		    valA = (String) a.get(KEY_NAME);
		    valB = (String) b.get(KEY_NAME);
		} catch (JSONException e) {
		    // do something
		}

		return valA.compareToIgnoreCase(valB);
		// if you want to change the sort order, simply use the
		// following:
		// return -valA.compareTo(valB);
	    }
	});

	for (int i = 0; i < jsonValues.size(); i++) {
	    jsonArr.put(jsonValues.get(i));
	}
    }

}
