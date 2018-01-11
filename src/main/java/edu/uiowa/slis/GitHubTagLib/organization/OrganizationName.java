package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationName extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationName.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getName();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for name tag ");
		}
	}

}
