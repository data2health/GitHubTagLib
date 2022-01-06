package edu.uiowa.slis.GitHubTagLib.committer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.user.User;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class Committer extends GitHubTagLibTagSupport {

	static Committer currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Committer.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int uid = 0;
	int rid = 0;
	Timestamp mostRecent = null;
	int count = 0;

	private String var = null;

	private Committer cachedCommitter = null;

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

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving uid " + uid);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving uid " + uid,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Committer currentCommitter = (Committer) pageContext.getAttribute("tag_committer");
			if(currentCommitter != null){
				cachedCommitter = currentCommitter;
			}
			currentCommitter = this;
			pageContext.setAttribute((var == null ? "tag_committer" : var), currentCommitter);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedCommitter != null){
				pageContext.setAttribute((var == null ? "tag_committer" : var), this.cachedCommitter);
			}else{
				pageContext.removeAttribute((var == null ? "tag_committer" : var));
				this.cachedCommitter = null;
			}
		}

		try {
			Boolean error = null; // (Boolean) pageContext.getAttribute("tagError");
			if(pageContext != null){
				error = (Boolean) pageContext.getAttribute("tagError");
			}

			if(error != null && error){

				freeConnection();
				clearServiceState();

				Exception e = (Exception) pageContext.getAttribute("tagErrorException");
				String message = (String) pageContext.getAttribute("tagErrorMessage");

				Tag parent = getParent();
				if(parent != null){
					return parent.doEndTag();
				}else if(e != null && message != null){
					throw new JspException(message,e);
				}else if(parent == null){
					pageContext.removeAttribute("tagError");
					pageContext.removeAttribute("tagErrorException");
					pageContext.removeAttribute("tagErrorMessage");
				}
			}
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update github.committer set most_recent = ?, count = ? where uid = ?  and rid = ? ");
				stmt.setTimestamp( 1, mostRecent );
				stmt.setInt( 2, count );
				stmt.setInt(3,uid);
				stmt.setInt(4,rid);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: IOException while writing to the user");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: IOException while writing to the user");
			}

		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException, SQLException {
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
		stmt.setTimestamp(3,mostRecent);
		stmt.setInt(4,count);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
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

	public Timestamp getMostRecent () {
		return mostRecent;
	}

	public void setMostRecent (Timestamp mostRecent) {
		this.mostRecent = mostRecent;
		commitNeeded = true;
	}

	public Timestamp getActualMostRecent () {
		return mostRecent;
	}

	public void setMostRecentToNow ( ) {
		this.mostRecent = new java.sql.Timestamp(new java.util.Date().getTime());
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

	public String getVar () {
		return var;
	}

	public void setVar (String var) {
		this.var = var;
	}

	public String getActualVar () {
		return var;
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

	public static Timestamp mostRecentValue() throws JspException {
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
		this.var = null;

	}

}
