package edu.uiowa.slis.GitHubTagLib.commit;

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
public class Commit extends GitHubTagLibTagSupport {

	static Commit currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Commit.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int ID = 0;
	Date committed = null;
	String name = null;
	String email = null;
	int userId = 0;
	String login = null;
	String message = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (theRepository!= null)
				parentEntities.addElement(theRepository);

			if (theRepository == null) {
			} else {
				ID = theRepository.getID();
			}

			CommitIterator theCommitIterator = (CommitIterator)findAncestorWithClass(this, CommitIterator.class);

			if (theCommitIterator != null) {
				ID = theCommitIterator.getID();
				committed = theCommitIterator.getCommitted();
			}

			if (theCommitIterator == null && theRepository == null && committed == null) {
				// no committed was provided - the default is to assume that it is a new Commit and to generate a new committed
				committed = new Date();
				insertEntity();
			} else {
				// an iterator or committed was provided as an attribute - we need to load a Commit from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select name,email,user_id,login,message from github.commit where id = ? and committed = ?");
				stmt.setInt(1,ID);
				stmt.setTimestamp(2,committed == null ? null : new java.sql.Timestamp(committed.getTime()));
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (name == null)
						name = rs.getString(1);
					if (email == null)
						email = rs.getString(2);
					if (userId == 0)
						userId = rs.getInt(3);
					if (login == null)
						login = rs.getString(4);
					if (message == null)
						message = rs.getString(5);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving committed " + committed, e);
			throw new JspTagException("Error: JDBC error retrieving committed " + committed);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update github.commit set name = ?, email = ?, user_id = ?, login = ?, message = ? where id = ? and committed = ?");
				stmt.setString(1,name);
				stmt.setString(2,email);
				stmt.setInt(3,userId);
				stmt.setString(4,login);
				stmt.setString(5,message);
				stmt.setInt(6,ID);
				stmt.setTimestamp(7,committed == null ? null : new java.sql.Timestamp(committed.getTime()));
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
			if (name == null)
				name = "";
			if (email == null)
				email = "";
			if (login == null)
				login = "";
			if (message == null)
				message = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into github.commit(id,committed,name,email,user_id,login,message) values (?,?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setTimestamp(2,committed == null ? null : new java.sql.Timestamp(committed.getTime()));
			stmt.setString(3,name);
			stmt.setString(4,email);
			stmt.setInt(5,userId);
			stmt.setString(6,login);
			stmt.setString(7,message);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
	}

	public Date getCommitted () {
		return committed;
	}

	public void setCommitted (Date committed) {
		this.committed = committed;
	}

	public Date getActualCommitted () {
		return committed;
	}

	public void setCommittedToNow ( ) {
		this.committed = new java.util.Date();
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
		commitNeeded = true;
	}

	public String getActualEmail () {
		return email;
	}

	public int getUserId () {
		return userId;
	}

	public void setUserId (int userId) {
		this.userId = userId;
		commitNeeded = true;
	}

	public int getActualUserId () {
		return userId;
	}

	public String getLogin () {
		if (commitNeeded)
			return "";
		else
			return login;
	}

	public void setLogin (String login) {
		this.login = login;
		commitNeeded = true;
	}

	public String getActualLogin () {
		return login;
	}

	public String getMessage () {
		if (commitNeeded)
			return "";
		else
			return message;
	}

	public void setMessage (String message) {
		this.message = message;
		commitNeeded = true;
	}

	public String getActualMessage () {
		return message;
	}

	public static Integer IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static Date committedValue() throws JspException {
		try {
			return currentInstance.getCommitted();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function committedValue()");
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

	public static Integer userIdValue() throws JspException {
		try {
			return currentInstance.getUserId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function userIdValue()");
		}
	}

	public static String loginValue() throws JspException {
		try {
			return currentInstance.getLogin();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function loginValue()");
		}
	}

	public static String messageValue() throws JspException {
		try {
			return currentInstance.getMessage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function messageValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		committed = null;
		name = null;
		email = null;
		userId = 0;
		login = null;
		message = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();

	}

}
