package edu.uiowa.slis.GitHubTagLib.orgRepo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.organization.Organization;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class OrgRepo extends GitHubTagLibTagSupport {

	static OrgRepo currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(OrgRepo.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int organizationId = 0;
	int repositoryId = 0;

	private String var = null;

	private OrgRepo cachedOrgRepo = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (theOrganization!= null)
				parentEntities.addElement(theOrganization);
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (theRepository!= null)
				parentEntities.addElement(theRepository);

			if (theOrganization == null) {
			} else {
				organizationId = theOrganization.getID();
			}
			if (theRepository == null) {
			} else {
				repositoryId = theRepository.getID();
			}

			OrgRepoIterator theOrgRepoIterator = (OrgRepoIterator)findAncestorWithClass(this, OrgRepoIterator.class);

			if (theOrgRepoIterator != null) {
				organizationId = theOrgRepoIterator.getOrganizationId();
				repositoryId = theOrgRepoIterator.getRepositoryId();
			}

			if (theOrgRepoIterator == null && theOrganization == null && theRepository == null && organizationId == 0) {
				// no organizationId was provided - the default is to assume that it is a new OrgRepo and to generate a new organizationId
				organizationId = Sequence.generateID();
				insertEntity();
			} else if (theOrgRepoIterator == null && theOrganization != null && theRepository == null) {
				// an organizationId was provided as an attribute - we need to load a OrgRepo from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select organization_id,repository_id from github.org_repo where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (organizationId == 0)
						organizationId = rs.getInt(1);
					if (repositoryId == 0)
						repositoryId = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theOrgRepoIterator == null && theOrganization == null && theRepository != null) {
				// an organizationId was provided as an attribute - we need to load a OrgRepo from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select organization_id,repository_id from github.org_repo where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (organizationId == 0)
						organizationId = rs.getInt(1);
					if (repositoryId == 0)
						repositoryId = rs.getInt(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or organizationId was provided as an attribute - we need to load a OrgRepo from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from github.org_repo where organization_id = ? and repository_id = ?");
				stmt.setInt(1,organizationId);
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
			log.error("JDBC error retrieving organizationId " + organizationId, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving organizationId " + organizationId);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving organizationId " + organizationId,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			OrgRepo currentOrgRepo = (OrgRepo) pageContext.getAttribute("tag_orgRepo");
			if(currentOrgRepo != null){
				cachedOrgRepo = currentOrgRepo;
			}
			currentOrgRepo = this;
			pageContext.setAttribute((var == null ? "tag_orgRepo" : var), currentOrgRepo);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedOrgRepo != null){
				pageContext.setAttribute((var == null ? "tag_orgRepo" : var), this.cachedOrgRepo);
			}else{
				pageContext.removeAttribute((var == null ? "tag_orgRepo" : var));
				this.cachedOrgRepo = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.org_repo set where organization_id = ?  and repository_id = ? ");
				stmt.setInt(1,organizationId);
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
		if (organizationId == 0) {
			organizationId = Sequence.generateID();
			log.debug("generating new OrgRepo " + organizationId);
		}

		if (repositoryId == 0) {
			repositoryId = Sequence.generateID();
			log.debug("generating new OrgRepo " + repositoryId);
		}

		PreparedStatement stmt = getConnection().prepareStatement("insert into github.org_repo(organization_id,repository_id) values (?,?)");
		stmt.setInt(1,organizationId);
		stmt.setInt(2,repositoryId);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
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

	public static Integer organizationIdValue() throws JspException {
		try {
			return currentInstance.getOrganizationId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function organizationIdValue()");
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
		organizationId = 0;
		repositoryId = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
