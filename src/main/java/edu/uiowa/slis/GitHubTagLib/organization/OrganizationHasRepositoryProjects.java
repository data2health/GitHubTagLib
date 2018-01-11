package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationHasRepositoryProjects extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationHasRepositoryProjects.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getHasRepositoryProjects());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasRepositoryProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
		}
		return SKIP_BODY;
	}

	public boolean getHasRepositoryProjects() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getHasRepositoryProjects();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for hasRepositoryProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
		}
	}

	public void setHasRepositoryProjects(boolean hasRepositoryProjects) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setHasRepositoryProjects(hasRepositoryProjects);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasRepositoryProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
		}
	}

}
