package edu.uiowa.slis.GitHubTagLib.follows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.user.User;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class Follows extends GitHubTagLibTagSupport {

	static Follows currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Follows.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int follower = 0;
	int following = 0;

	private String var = null;

	private Follows cachedFollows = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (theUser!= null)
				parentEntities.addElement(theUser);

			if (theUser == null) {
			} else {
				follower = theUser.getID();
			}

			FollowsIterator theFollowsIterator = (FollowsIterator)findAncestorWithClass(this, FollowsIterator.class);

			if (theFollowsIterator != null) {
				follower = theFollowsIterator.getFollower();
				following = theFollowsIterator.getFollowing();
			}

			if (theFollowsIterator == null && theUser == null && theUser == null && follower == 0) {
				// no follower was provided - the default is to assume that it is a new Follows and to generate a new follower
				follower = Sequence.generateID();
				insertEntity();
			} else if (theFollowsIterator == null && theUser != null && theUser == null) {
				// an follower was provided as an attribute - we need to load a Follows from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select follower,following from github.follows where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (follower == 0)
						follower = rs.getInt(1);
					if (following == 0)
						following = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theFollowsIterator == null && theUser == null && theUser != null) {
				// an follower was provided as an attribute - we need to load a Follows from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select follower,following from github.follows where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (follower == 0)
						follower = rs.getInt(1);
					if (following == 0)
						following = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or follower was provided as an attribute - we need to load a Follows from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from github.follows where follower = ? and following = ?");
				stmt.setInt(1,follower);
				stmt.setInt(2,following);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving follower " + follower, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving follower " + follower);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving follower " + follower,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Follows currentFollows = (Follows) pageContext.getAttribute("tag_follows");
			if(currentFollows != null){
				cachedFollows = currentFollows;
			}
			currentFollows = this;
			pageContext.setAttribute((var == null ? "tag_follows" : var), currentFollows);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedFollows != null){
				pageContext.setAttribute((var == null ? "tag_follows" : var), this.cachedFollows);
			}else{
				pageContext.removeAttribute((var == null ? "tag_follows" : var));
				this.cachedFollows = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.follows set where follower = ?  and following = ? ");
				stmt.setInt(1,follower);
				stmt.setInt(2,following);
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
		if (follower == 0) {
			follower = Sequence.generateID();
			log.debug("generating new Follows " + follower);
		}

		if (following == 0) {
			following = Sequence.generateID();
			log.debug("generating new Follows " + following);
		}

		PreparedStatement stmt = getConnection().prepareStatement("insert into github.follows(follower,following) values (?,?)");
		stmt.setInt(1,follower);
		stmt.setInt(2,following);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
	}

	public int getFollower () {
		return follower;
	}

	public void setFollower (int follower) {
		this.follower = follower;
	}

	public int getActualFollower () {
		return follower;
	}

	public int getFollowing () {
		return following;
	}

	public void setFollowing (int following) {
		this.following = following;
	}

	public int getActualFollowing () {
		return following;
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

	public static Integer followerValue() throws JspException {
		try {
			return currentInstance.getFollower();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function followerValue()");
		}
	}

	public static Integer followingValue() throws JspException {
		try {
			return currentInstance.getFollowing();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function followingValue()");
		}
	}

	private void clearServiceState () {
		follower = 0;
		following = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
