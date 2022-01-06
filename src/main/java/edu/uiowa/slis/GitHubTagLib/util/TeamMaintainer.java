package edu.uiowa.slis.GitHubTagLib.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cd2h.JSONTagLib.GraphQL.GitHubAPI;
import org.json.JSONArray;
import org.json.JSONObject;

public class TeamMaintainer {
	static Logger logger = LogManager.getLogger(TeamMaintainer.class);
	protected static LocalProperties prop_file = null;
	static Connection conn = null;
	static Hashtable<String, String> membershipHash = new Hashtable<String, String>();

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		prop_file = PropertyLoader.loadProperties("github");
		getConnection();

		updateOrgMembership();
		updateTeamMembership();
	}

	static void updateOrgMembership() throws IOException, SQLException {
		refreshMembershipHash();
		PreparedStatement stmt = conn
				.prepareStatement("select github_handle_url from drive.person where github_handle_url is not null");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String githubURL = rs.getString(1);
			String github = githubURL.substring(githubURL.lastIndexOf('/') + 1);
			if (github.endsWith(".com") || github.endsWith(".edu") || github.endsWith(".org")
					|| membershipHash.containsKey(github))
				continue;
			logger.info("user: " + github);
			try {
				Content.addMember("data2health", github);
			} catch (Exception e) {
				logger.error("\t*** invalid user ID: " + github);
			}
		}
		stmt.close();
	}

	static void refreshMembershipHash() throws IOException {
		GitHubAPI theAPI = new GitHubAPI();
		logger.info("results:\n" + theAPI.submitQuery(theAPI.getStatement("memberList")).toString(3));
		JSONArray results = theAPI.submitQuery(theAPI.getStatement("memberList")).getJSONObject("data")
				.getJSONObject("organization").getJSONObject("membersWithRole").getJSONArray("nodes");
		for (int i = 0; i < results.length(); i++) {
			JSONObject current = results.getJSONObject(i);
			logger.debug("caching: " + current.getString("login"));
			membershipHash.put(current.getString("login"), current.getString("login"));
		}
	}

	static void updateTeamMembership() throws SQLException, IOException {
		PreparedStatement stmt = conn.prepareStatement(
				"select project.project_id,repo_name,title from dashboard.dashboard,google.project where title=updated_title");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			int id = rs.getInt(1);
			String repo = rs.getString(2);
			String title = rs.getString(2);
			logger.info("repo: " + repo + " - " + title);
			processProject(id, repo);
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
		injectParticipants(newContent, id, "Mailing list only");
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
		PreparedStatement stmt = conn.prepareStatement(
				"select preferred_first_name,last_name,institution,github_handle_url from google.role natural join google.person where project_id=? and role=? order by last_name,preferred_first_name");
		stmt.setInt(1, id);
		stmt.setString(2, targetRole);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			String firstName = rs.getString(1);
			String lastName = rs.getString(2);
			String institution = rs.getString(3);
			String githubURL = rs.getString(4);
			String githubHandle = githubURL.substring(githubURL.lastIndexOf('/') + 1);
			buffer.append(firstName + " " + lastName + " | [" + githubHandle + "](" + githubURL + ") | " + institution
					+ "\n");
		}
		stmt.close();
	}

	static void getConnection() throws ClassNotFoundException, SQLException {
		logger.info("connecting to database...");
		Class.forName("org.postgresql.Driver");
		Properties props = new Properties();
		props.setProperty("user", prop_file.getProperty("jdbc.user"));
		props.setProperty("password", prop_file.getProperty("jdbc.password"));
		conn = DriverManager.getConnection("jdbc:postgresql://" + prop_file.getProperty("jdbc.host") + "/"
				+ prop_file.getProperty("jdbc.database"), props);
		conn.setAutoCommit(true);
	}

}
