package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserSiteAdmin extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserSiteAdmin.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getSiteAdmin());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for siteAdmin tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for siteAdmin tag ");
		}
		return SKIP_BODY;
	}

	public boolean getSiteAdmin() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getSiteAdmin();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for siteAdmin tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for siteAdmin tag ");
		}
	}

	public void setSiteAdmin(boolean siteAdmin) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setSiteAdmin(siteAdmin);
		} catch (Exception e) {
			log.error("Can't find enclosing User for siteAdmin tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for siteAdmin tag ");
		}
	}

}
