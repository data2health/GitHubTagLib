package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationDescription extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationDescription.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getDescription());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for description tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for description tag ");
		}
		return SKIP_BODY;
	}

	public String getDescription() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getDescription();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for description tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for description tag ");
		}
	}

	public void setDescription(String description) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setDescription(description);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for description tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for description tag ");
		}
	}

}
