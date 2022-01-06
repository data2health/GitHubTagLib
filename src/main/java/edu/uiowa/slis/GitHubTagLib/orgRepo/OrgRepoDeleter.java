package edu.uiowa.slis.GitHubTagLib.orgRepo;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.GitHubTagLibBodyTagSupport;
import edu.uiowa.slis.GitHubTagLib.organization.Organization;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

@SuppressWarnings("serial")
public class OrgRepoDeleter extends GitHubTagLibBodyTagSupport {
    int organizationId = 0;
    int repositoryId = 0;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(OrgRepoDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
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


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from github.org_repo where 1=1"
                                                        + (organizationId == 0 ? "" : " and organization_id = ? ")
                                                        + (repositoryId == 0 ? "" : " and repository_id = ? ")
                                                        + (organizationId == 0 ? "" : " and organization_id = ? ")
                                                        + (repositoryId == 0 ? "" : " and repository_id = ? "));
            if (organizationId != 0) stat.setInt(webapp_keySeq++, organizationId);
            if (repositoryId != 0) stat.setInt(webapp_keySeq++, repositoryId);
			if (organizationId != 0) stat.setInt(webapp_keySeq++, organizationId);
			if (repositoryId != 0) stat.setInt(webapp_keySeq++, repositoryId);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating OrgRepo deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating OrgRepo deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating OrgRepo deleter",e);
			}

        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {

		clearServiceState();
		Boolean error = (Boolean) pageContext.getAttribute("tagError");
		if(error != null && error){

			freeConnection();

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
		return super.doEndTag();
	}

    private void clearServiceState() {
        organizationId = 0;
        repositoryId = 0;
        parentEntities = new Vector<GitHubTagLibTagSupport>();

        this.rs = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
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
}
