package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationHasOrganizationProjects extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationHasOrganizationProjects.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getHasOrganizationProjects());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasOrganizationProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
		}
		return SKIP_BODY;
	}

	public boolean getHasOrganizationProjects() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getHasOrganizationProjects();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for hasOrganizationProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
		}
	}

	public void setHasOrganizationProjects(boolean hasOrganizationProjects) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setHasOrganizationProjects(hasOrganizationProjects);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasOrganizationProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
		}
	}

}
