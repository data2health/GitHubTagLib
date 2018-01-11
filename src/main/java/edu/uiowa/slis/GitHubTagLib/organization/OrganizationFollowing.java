package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationFollowing extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationFollowing.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getFollowing());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for following tag ");
		}
		return SKIP_BODY;
	}

	public int getFollowing() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getFollowing();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for following tag ");
		}
	}

	public void setFollowing(int following) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setFollowing(following);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for following tag ");
		}
	}

}
