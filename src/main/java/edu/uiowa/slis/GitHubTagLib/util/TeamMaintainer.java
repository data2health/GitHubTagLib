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
	prop_file = PropertyLoader.loadProperties("github");
	getConnection();
	PreparedStatement stmt = conn.prepareStatement("select id,repo_name,title from dashboard.dashboard,google.project where title=updated_title");
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    int id = rs.getInt(1);
	    String repo = rs.getString(2);
	    String title = rs.getString(2);
	    logger.info("repo: " + repo + " - " + title);
	    processProject(id,repo);
	}
    }
	
    static void processProject(int id, String repo) throws SQLException, IOException {
	StringBuffer newContent = new StringBuffer();
	newContent.append("# Team Members\n");
	newContent.append("\n");
	newContent.append("## Lead(s)\n");
	newContent.append("Name | GitHub Handle | Site\n");
	newContent.append("-- | -- | --\n");
	injectParticipants(newContent, id, "Lead");
	newContent.append("\n");
	newContent.append("## Contributor(s)\n");
	newContent.append("Name | GitHub Handle | Site\n");
	newContent.append("-- | -- | --\n");
	injectParticipants(newContent, id, "Contributor");
	newContent.append("\n");
	newContent.append("## Mailing list only\n");
	newContent.append("Name | GitHub Handle | Site\n");
	newContent.append("-- | -- | --\n");
	injectParticipants(newContent, 14, "Mailing list only");
	newContent.append("\n");

	logger.info("final result:\n" + newContent);
	try {
	    Content.replace("data2health", repo, "team.md", newContent.toString(), "refreshed by TeamMaintainer");
	} catch (Exception e) {
	    Content.write("data2health", repo, "team.md", newContent.toString(), "refreshed by TeamMaintainer");
	}
    }
    
    // Name | GitHub Handle | Site
    // -- | -- | --
    static void injectParticipants(StringBuffer buffer, int id, String targetRole) throws SQLException {
	PreparedStatement stmt = conn.prepareStatement("select * from google.role natural join google.person where id=? and role=? order by last_name,preferred_first_name");
	stmt.setInt(1, id);
	stmt.setString(2, targetRole);
	ResultSet rs = stmt.executeQuery();
	while (rs.next()) {
	    String email = rs.getString(1);
	    int tableID = rs.getInt(2);
	    String role = rs.getString(3);
	    String firstName = rs.getString(4);
	    String lastName = rs.getString(5);
	    String projectRole = rs.getString(6);
	    String institution = rs.getString(7);
	    String githubURL = rs.getString(8);
	    String githubHandle = githubURL.substring(githubURL.lastIndexOf('/')+ 1);
	    buffer.append(firstName + " " + lastName + " | [" + githubHandle + "](" + githubURL + ") | " + institution + "\n");
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
