package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationFollowers extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationFollowers.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getFollowers());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for followers tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for followers tag ");
		}
		return SKIP_BODY;
	}

	public int getFollowers() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getFollowers();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for followers tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for followers tag ");
		}
	}

	public void setFollowers(int followers) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setFollowers(followers);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for followers tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for followers tag ");
		}
	}

}
