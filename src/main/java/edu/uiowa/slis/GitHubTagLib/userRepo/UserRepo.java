package edu.uiowa.slis.GitHubTagLib.userRepo;

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
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class UserRepo extends GitHubTagLibTagSupport {

	static UserRepo currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(UserRepo.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int userId = 0;
	int repositoryId = 0;

	private String var = null;

	private UserRepo cachedUserRepo = null;

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
				userId = theUser.getID();
			}
			if (theRepository == null) {
			} else {
				repositoryId = theRepository.getID();
			}

			UserRepoIterator theUserRepoIterator = (UserRepoIterator)findAncestorWithClass(this, UserRepoIterator.class);

			if (theUserRepoIterator != null) {
				userId = theUserRepoIterator.getUserId();
				repositoryId = theUserRepoIterator.getRepositoryId();
			}

			if (theUserRepoIterator == null && theUser == null && theRepository == null && userId == 0) {
				// no userId was provided - the default is to assume that it is a new UserRepo and to generate a new userId
				userId = Sequence.generateID();
				insertEntity();
			} else if (theUserRepoIterator == null && theUser != null && theRepository == null) {
				// an userId was provided as an attribute - we need to load a UserRepo from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select user_id,repository_id from github.user_repo where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (userId == 0)
						userId = rs.getInt(1);
					if (repositoryId == 0)
						repositoryId = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theUserRepoIterator == null && theUser == null && theRepository != null) {
				// an userId was provided as an attribute - we need to load a UserRepo from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select user_id,repository_id from github.user_repo where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (userId == 0)
						userId = rs.getInt(1);
					if (repositoryId == 0)
						repositoryId = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or userId was provided as an attribute - we need to load a UserRepo from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from github.user_repo where user_id = ? and repository_id = ?");
				stmt.setInt(1,userId);
				stmt.setInt(2,repositoryId);
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
			log.error("JDBC error retrieving userId " + userId, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving userId " + userId);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving userId " + userId,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			UserRepo currentUserRepo = (UserRepo) pageContext.getAttribute("tag_userRepo");
			if(currentUserRepo != null){
				cachedUserRepo = currentUserRepo;
			}
			currentUserRepo = this;
			pageContext.setAttribute((var == null ? "tag_userRepo" : var), currentUserRepo);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedUserRepo != null){
				pageContext.setAttribute((var == null ? "tag_userRepo" : var), this.cachedUserRepo);
			}else{
				pageContext.removeAttribute((var == null ? "tag_userRepo" : var));
				this.cachedUserRepo = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.user_repo set where user_id = ?  and repository_id = ? ");
				stmt.setInt(1,userId);
				stmt.setInt(2,repositoryId);
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
		if (userId == 0) {
			userId = Sequence.generateID();
			log.debug("generating new UserRepo " + userId);
		}

		if (repositoryId == 0) {
			repositoryId = Sequence.generateID();
			log.debug("generating new UserRepo " + repositoryId);
		}

		PreparedStatement stmt = getConnection().prepareStatement("insert into github.user_repo(user_id,repository_id) values (?,?)");
		stmt.setInt(1,userId);
		stmt.setInt(2,repositoryId);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
	}

	public int getUserId () {
		return userId;
	}

	public void setUserId (int userId) {
		this.userId = userId;
	}

	public int getActualUserId () {
		return userId;
	}

	public int getRepositoryId () {
		return repositoryId;
	}

	public void setRepositoryId (int repositoryId) {
		this.repositoryId = repositoryId;
	}

	public int getActualRepositoryId () {
		return repositoryId;
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

	public static Integer userIdValue() throws JspException {
		try {
			return currentInstance.getUserId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function userIdValue()");
		}
	}

	public static Integer repositoryIdValue() throws JspException {
		try {
			return currentInstance.getRepositoryId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function repositoryIdValue()");
		}
	}

	private void clearServiceState () {
		userId = 0;
		repositoryId = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
