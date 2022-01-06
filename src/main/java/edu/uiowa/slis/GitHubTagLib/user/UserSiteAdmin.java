package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserSiteAdmin extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserSiteAdmin.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getSiteAdmin());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for siteAdmin tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for siteAdmin tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for siteAdmin tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getSiteAdmin() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getSiteAdmin();
		} catch (Exception e) {
			log.error("Can't find enclosing User for siteAdmin tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for siteAdmin tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for siteAdmin tag ");
			}
		}
	}

	public void setSiteAdmin(boolean siteAdmin) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setSiteAdmin(siteAdmin);
		} catch (Exception e) {
			log.error("Can't find enclosing User for siteAdmin tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for siteAdmin tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for siteAdmin tag ");
			}
		}
	}

}
