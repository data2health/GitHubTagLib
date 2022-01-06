package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationHasRepositoryProjects extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OrganizationHasRepositoryProjects.class);

	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getHasRepositoryProjects());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasRepositoryProjects tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getHasRepositoryProjects() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getHasRepositoryProjects();
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasRepositoryProjects tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
			}
		}
	}

	public void setHasRepositoryProjects(boolean hasRepositoryProjects) throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setHasRepositoryProjects(hasRepositoryProjects);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for hasRepositoryProjects tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for hasRepositoryProjects tag ");
			}
		}
	}

}
