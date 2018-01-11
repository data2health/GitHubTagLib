package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationLocation extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationLocation.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getLocation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for location tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for location tag ");
		}
		return SKIP_BODY;
	}

	public String getLocation() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getLocation();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for location tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for location tag ");
		}
	}

	public void setLocation(String location) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setLocation(location);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for location tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for location tag ");
		}
	}

}
