package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserLocation extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserLocation.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getLocation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for location tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for location tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLocation() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getLocation();
		} catch (Exception e) {
			log.error("Can't find enclosing User for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for location tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for location tag ");
			}
		}
	}

	public void setLocation(String location) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setLocation(location);
		} catch (Exception e) {
			log.error("Can't find enclosing User for location tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for location tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for location tag ");
			}
		}
	}

}
