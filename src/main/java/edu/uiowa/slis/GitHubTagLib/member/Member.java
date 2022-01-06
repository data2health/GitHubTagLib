package edu.uiowa.slis.GitHubTagLib.member;

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
import edu.uiowa.slis.GitHubTagLib.organization.Organization;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class Member extends GitHubTagLibTagSupport {

	static Member currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Member.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int userId = 0;
	int organizationId = 0;

	private String var = null;

	private Member cachedMember = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (theUser!= null)
				parentEntities.addElement(theUser);
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (theOrganization!= null)
				parentEntities.addElement(theOrganization);

			if (theUser == null) {
			} else {
				userId = theUser.getID();
			}
			if (theOrganization == null) {
			} else {
				organizationId = theOrganization.getID();
			}

			MemberIterator theMemberIterator = (MemberIterator)findAncestorWithClass(this, MemberIterator.class);

			if (theMemberIterator != null) {
				userId = theMemberIterator.getUserId();
				organizationId = theMemberIterator.getOrganizationId();
			}

			if (theMemberIterator == null && theUser == null && theOrganization == null && userId == 0) {
				// no userId was provided - the default is to assume that it is a new Member and to generate a new userId
				userId = Sequence.generateID();
				insertEntity();
			} else if (theMemberIterator == null && theUser != null && theOrganization == null) {
				// an userId was provided as an attribute - we need to load a Member from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select user_id,organization_id from github.member where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (userId == 0)
						userId = rs.getInt(1);
					if (organizationId == 0)
						organizationId = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theMemberIterator == null && theUser == null && theOrganization != null) {
				// an userId was provided as an attribute - we need to load a Member from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select user_id,organization_id from github.member where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (userId == 0)
						userId = rs.getInt(1);
					if (organizationId == 0)
						organizationId = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or userId was provided as an attribute - we need to load a Member from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from github.member where user_id = ? and organization_id = ?");
				stmt.setInt(1,userId);
				stmt.setInt(2,organizationId);
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
			Member currentMember = (Member) pageContext.getAttribute("tag_member");
			if(currentMember != null){
				cachedMember = currentMember;
			}
			currentMember = this;
			pageContext.setAttribute((var == null ? "tag_member" : var), currentMember);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedMember != null){
				pageContext.setAttribute((var == null ? "tag_member" : var), this.cachedMember);
			}else{
				pageContext.removeAttribute((var == null ? "tag_member" : var));
				this.cachedMember = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.member set where user_id = ?  and organization_id = ? ");
				stmt.setInt(1,userId);
				stmt.setInt(2,organizationId);
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
			log.debug("generating new Member " + userId);
		}

		if (organizationId == 0) {
			organizationId = Sequence.generateID();
			log.debug("generating new Member " + organizationId);
		}

		PreparedStatement stmt = getConnection().prepareStatement("insert into github.member(user_id,organization_id) values (?,?)");
		stmt.setInt(1,userId);
		stmt.setInt(2,organizationId);
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

	public int getOrganizationId () {
		return organizationId;
	}

	public void setOrganizationId (int organizationId) {
		this.organizationId = organizationId;
	}

	public int getActualOrganizationId () {
		return organizationId;
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

	public static Integer organizationIdValue() throws JspException {
		try {
			return currentInstance.getOrganizationId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function organizationIdValue()");
		}
	}

	private void clearServiceState () {
		userId = 0;
		organizationId = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
