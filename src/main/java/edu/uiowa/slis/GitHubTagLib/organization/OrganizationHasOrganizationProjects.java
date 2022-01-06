package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationHasOrganizationProjects extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OrganizationHasOrganizationProjects.class);

	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getHasOrganizationProjects());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasOrganizationProjects tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getHasOrganizationProjects() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getHasOrganizationProjects();
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasOrganizationProjects tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
			}
		}
	}

	public void setHasOrganizationProjects(boolean hasOrganizationProjects) throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setHasOrganizationProjects(hasOrganizationProjects);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasOrganizationProjects tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for hasOrganizationProjects tag ");
			}
		}
	}

}
