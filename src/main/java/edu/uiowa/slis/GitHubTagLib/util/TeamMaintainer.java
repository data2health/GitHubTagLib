package edu.uiowa.slis.GitHubTagLib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class TeamMaintainer {
    static Logger logger = Logger.getLogger(TeamMaintainer.class);
    protected static LocalProperties prop_file = null;
    static Connection conn = null;

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
	boolean inPrefix = true;
	boolean inList = true;
	boolean databaseCalled = false;
	prop_file = PropertyLoader.loadProperties("github");
	getConnection();
	
	String originalContent = Content.read("data2health", "roadmap", "team.md");
	StringBuffer newContent = new StringBuffer();
	String buffer = null;
	BufferedReader reader = new BufferedReader(new StringReader(originalContent));
	while ((buffer = reader.readLine()) != null) {
	    logger.info("buffer: " + buffer);
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
		    logger.info("calling database..,");
		    // call the database for repo list
		    injectProjects(newContent);
		    databaseCalled = true;
		}
	    }
	    if (!inPrefix && !inList) {
		newContent.append(buffer + "\n");
	    }
	}
	logger.info("final result:\n" + newContent);
	Content.replace("data2health", "roadmap", "README.md", newContent.toString(), "refreshed by RoadmapMaintainer");
    }
    
    static void injectProjects(StringBuffer buffer) throws SQLException {
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

    static void getConnection() throws ClassNotFoundException, SQLException {
	logger.info("connecting to database...");
	Class.forName("org.postgresql.Driver");
	Properties props = new Properties();
	props.setProperty("user", prop_file.getProperty("jdbc.user"));
	props.setProperty("password", prop_file.getProperty("jdbc.password"));
	conn = DriverManager.getConnection("jdbc:postgresql://" + prop_file.getProperty("jdbc.host") + "/" + prop_file.getProperty("jdbc.database"), props);
	conn.setAutoCommit(true);
    }

}
