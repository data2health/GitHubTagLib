package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationLogin extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationLogin.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getLogin());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for login tag ");
		}
		return SKIP_BODY;
	}

	public String getLogin() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getLogin();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for login tag ");
		}
	}

	public void setLogin(String login) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setLogin(login);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for login tag ");
		}
	}

}
