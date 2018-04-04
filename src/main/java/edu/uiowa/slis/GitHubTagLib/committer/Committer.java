package edu.uiowa.slis.GitHubTagLib.committer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.GitHubTagLib.user.User;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class Committer extends GitHubTagLibTagSupport {

	static Committer currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Committer.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int uid = 0;
	int rid = 0;
	Date mostRecent = null;
	int count = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (theUser!= null)
				parentEntities.addElement(theUser);
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (theRepository!= null)
				parentEntities.addElement(theRepository);

			if (theUser == null) {
			} else {
				uid = theUser.getID();
			}
			if (theRepository == null) {
			} else {
				rid = theRepository.getID();
			}

			CommitterIterator theCommitterIterator = (CommitterIterator)findAncestorWithClass(this, CommitterIterator.class);

			if (theCommitterIterator != null) {
				uid = theCommitterIterator.getUid();
				rid = theCommitterIterator.getRid();
			}

			if (theCommitterIterator == null && theUser == null && theRepository == null && uid == 0) {
				// no uid was provided - the default is to assume that it is a new Committer and to generate a new uid
				uid = Sequence.generateID();
				insertEntity();
			} else if (theCommitterIterator == null && theUser != null && theRepository == null) {
				// an uid was provided as an attribute - we need to load a Committer from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select uid,rid,most_recent,count from github.committer where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (uid == 0)
						uid = rs.getInt(1);
					if (rid == 0)
						rid = rs.getInt(2);
					if (mostRecent == null)
						mostRecent = rs.getTimestamp(3);
					if (count == 0)
						count = rs.getInt(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theCommitterIterator == null && theUser == null && theRepository != null) {
				// an uid was provided as an attribute - we need to load a Committer from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select uid,rid,most_recent,count from github.committer where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (uid == 0)
						uid = rs.getInt(1);
					if (rid == 0)
						rid = rs.getInt(2);
					if (mostRecent == null)
						mostRecent = rs.getTimestamp(3);
					if (count == 0)
						count = rs.getInt(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or uid was provided as an attribute - we need to load a Committer from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select most_recent,count from github.committer where uid = ? and rid = ?");
				stmt.setInt(1,uid);
				stmt.setInt(2,rid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (mostRecent == null)
						mostRecent = rs.getTimestamp(1);
					if (count == 0)
						count = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving uid " + uid, e);
			throw new JspTagException("Error: JDBC error retrieving uid " + uid);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update github.committer set most_recent = ?, count = ? where uid = ? and rid = ?");
				stmt.setTimestamp(1,mostRecent == null ? null : new java.sql.Timestamp(mostRecent.getTime()));
				stmt.setInt(2,count);
				stmt.setInt(3,uid);
				stmt.setInt(4,rid);
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
			if (uid == 0) {
				uid = Sequence.generateID();
				log.debug("generating new Committer " + uid);
			}

			if (rid == 0) {
				rid = Sequence.generateID();
				log.debug("generating new Committer " + rid);
			}

			PreparedStatement stmt = getConnection().prepareStatement("insert into github.committer(uid,rid,most_recent,count) values (?,?,?,?)");
			stmt.setInt(1,uid);
			stmt.setInt(2,rid);
			stmt.setTimestamp(3,mostRecent == null ? null : new java.sql.Timestamp(mostRecent.getTime()));
			stmt.setInt(4,count);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	public int getRid () {
		return rid;
	}

	public void setRid (int rid) {
		this.rid = rid;
	}

	public int getActualRid () {
		return rid;
	}

	public Date getMostRecent () {
		return mostRecent;
	}

	public void setMostRecent (Date mostRecent) {
		this.mostRecent = mostRecent;
		commitNeeded = true;
	}

	public Date getActualMostRecent () {
		return mostRecent;
	}

	public void setMostRecentToNow ( ) {
		this.mostRecent = new java.util.Date();
		commitNeeded = true;
	}

	public int getCount () {
		return count;
	}

	public void setCount (int count) {
		this.count = count;
		commitNeeded = true;
	}

	public int getActualCount () {
		return count;
	}

	public static Integer uidValue() throws JspException {
		try {
			return currentInstance.getUid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function uidValue()");
		}
	}

	public static Integer ridValue() throws JspException {
		try {
			return currentInstance.getRid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function ridValue()");
		}
	}

	public static Date mostRecentValue() throws JspException {
		try {
			return currentInstance.getMostRecent();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function mostRecentValue()");
		}
	}

	public static Integer countValue() throws JspException {
		try {
			return currentInstance.getCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function countValue()");
		}
	}

	private void clearServiceState () {
		uid = 0;
		rid = 0;
		mostRecent = null;
		count = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();

	}

}
