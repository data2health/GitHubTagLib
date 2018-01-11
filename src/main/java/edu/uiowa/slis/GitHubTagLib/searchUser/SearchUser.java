package edu.uiowa.slis.GitHubTagLib.searchUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.GitHubTagLib.searchTerm.SearchTerm;
import edu.uiowa.slis.GitHubTagLib.user.User;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class SearchUser extends GitHubTagLibTagSupport {

	static SearchUser currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(SearchUser.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int sid = 0;
	int uid = 0;
	int rank = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			if (theSearchTerm!= null)
				parentEntities.addElement(theSearchTerm);
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (theUser!= null)
				parentEntities.addElement(theUser);

			if (theSearchTerm == null) {
			} else {
				sid = theSearchTerm.getID();
			}
			if (theUser == null) {
			} else {
				uid = theUser.getID();
			}

			SearchUserIterator theSearchUserIterator = (SearchUserIterator)findAncestorWithClass(this, SearchUserIterator.class);

			if (theSearchUserIterator != null) {
				sid = theSearchUserIterator.getSid();
				uid = theSearchUserIterator.getUid();
			}

			if (theSearchUserIterator == null && theSearchTerm == null && theUser == null && sid == 0) {
				// no sid was provided - the default is to assume that it is a new SearchUser and to generate a new sid
				sid = Sequence.generateID();
				insertEntity();
			} else if (theSearchUserIterator == null && theSearchTerm != null && theUser == null) {
				// an sid was provided as an attribute - we need to load a SearchUser from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sid,uid,rank from github.search_user where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sid == 0)
						sid = rs.getInt(1);
					if (uid == 0)
						uid = rs.getInt(2);
					if (rank == 0)
						rank = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theSearchUserIterator == null && theSearchTerm == null && theUser != null) {
				// an sid was provided as an attribute - we need to load a SearchUser from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sid,uid,rank from github.search_user where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sid == 0)
						sid = rs.getInt(1);
					if (uid == 0)
						uid = rs.getInt(2);
					if (rank == 0)
						rank = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or sid was provided as an attribute - we need to load a SearchUser from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select rank from github.search_user where sid = ? and uid = ?");
				stmt.setInt(1,sid);
				stmt.setInt(2,uid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (rank == 0)
						rank = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving sid " + sid, e);
			throw new JspTagException("Error: JDBC error retrieving sid " + sid);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update github.search_user set rank = ? where sid = ? and uid = ?");
				stmt.setInt(1,rank);
				stmt.setInt(2,sid);
				stmt.setInt(3,uid);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			if (sid == 0) {
				sid = Sequence.generateID();
				log.debug("generating new SearchUser " + sid);
			}

			if (uid == 0) {
				uid = Sequence.generateID();
				log.debug("generating new SearchUser " + uid);
			}

			PreparedStatement stmt = getConnection().prepareStatement("insert into github.search_user(sid,uid,rank) values (?,?,?)");
			stmt.setInt(1,sid);
			stmt.setInt(2,uid);
			stmt.setInt(3,rank);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public int getSid () {
		return sid;
	}

	public void setSid (int sid) {
		this.sid = sid;
	}

	public int getActualSid () {
		return sid;
	}

	public int getUid () {
		return uid;
	}

	public void setUid (int uid) {
		this.uid = uid;
	}

	public int getActualUid () {
		return uid;
	}

	public int getRank () {
		return rank;
	}

	public void setRank (int rank) {
		this.rank = rank;
		commitNeeded = true;
	}

	public int getActualRank () {
		return rank;
	}

	public static Integer sidValue() throws JspException {
		try {
			return currentInstance.getSid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sidValue()");
		}
	}

	public static Integer uidValue() throws JspException {
		try {
			return currentInstance.getUid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function uidValue()");
		}
	}

	public static Integer rankValue() throws JspException {
		try {
			return currentInstance.getRank();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function rankValue()");
		}
	}

	private void clearServiceState () {
		sid = 0;
		uid = 0;
		rank = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();

	}

}
