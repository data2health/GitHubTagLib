package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class OtherCommitter extends GitHubTagLibTagSupport {

	static OtherCommitter currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(OtherCommitter.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int rid = 0;
	String name = null;
	String email = null;
	Date mostRecent = null;
	int count = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (theRepository!= null)
				parentEntities.addElement(theRepository);

			if (theRepository == null) {
			} else {
				rid = theRepository.getID();
			}

			OtherCommitterIterator theOtherCommitterIterator = (OtherCommitterIterator)findAncestorWithClass(this, OtherCommitterIterator.class);

			if (theOtherCommitterIterator != null) {
				rid = theOtherCommitterIterator.getRid();
				email = theOtherCommitterIterator.getEmail();
			}

			if (theOtherCommitterIterator == null && theRepository == null && rid == 0) {
				// no rid was provided - the default is to assume that it is a new OtherCommitter and to generate a new rid
				rid = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or rid was provided as an attribute - we need to load a OtherCommitter from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select name,most_recent,count from github.other_committer where rid = ? and email = ?");
				stmt.setInt(1,rid);
				stmt.setString(2,email);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (name == null)
						name = rs.getString(1);
					if (mostRecent == null)
						mostRecent = rs.getTimestamp(2);
					if (count == 0)
						count = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving rid " + rid, e);
			throw new JspTagException("Error: JDBC error retrieving rid " + rid);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update github.other_committer set name = ?, most_recent = ?, count = ? where rid = ? and email = ?");
				stmt.setString(1,name);
				stmt.setTimestamp(2,mostRecent == null ? null : new java.sql.Timestamp(mostRecent.getTime()));
				stmt.setInt(3,count);
				stmt.setInt(4,rid);
				stmt.setString(5,email);
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
			if (rid == 0) {
				rid = Sequence.generateID();
				log.debug("generating new OtherCommitter " + rid);
			}

			if (name == null)
				name = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into github.other_committer(rid,name,email,most_recent,count) values (?,?,?,?,?)");
			stmt.setInt(1,rid);
			stmt.setString(2,name);
			stmt.setString(3,email);
			stmt.setTimestamp(4,mostRecent == null ? null : new java.sql.Timestamp(mostRecent.getTime()));
			stmt.setInt(5,count);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	public String getName () {
		if (commitNeeded)
			return "";
		else
			return name;
	}

	public void setName (String name) {
		this.name = name;
		commitNeeded = true;
	}

	public String getActualName () {
		return name;
	}

	public String getEmail () {
		if (commitNeeded)
			return "";
		else
			return email;
	}

	public void setEmail (String email) {
		this.email = email;
	}

	public String getActualEmail () {
		return email;
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

	public static Integer ridValue() throws JspException {
		try {
			return currentInstance.getRid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function ridValue()");
		}
	}

	public static String nameValue() throws JspException {
		try {
			return currentInstance.getName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameValue()");
		}
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
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
		rid = 0;
		name = null;
		email = null;
		mostRecent = null;
		count = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();

	}

}
