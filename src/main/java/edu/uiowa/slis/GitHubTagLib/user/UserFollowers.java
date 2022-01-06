package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserFollowers extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserFollowers.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getFollowers());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for followers tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for followers tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for followers tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getFollowers() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getFollowers();
		} catch (Exception e) {
			log.error("Can't find enclosing User for followers tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for followers tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for followers tag ");
			}
		}
	}

	public void setFollowers(int followers) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setFollowers(followers);
		} catch (Exception e) {
			log.error("Can't find enclosing User for followers tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for followers tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for followers tag ");
			}
		}
	}

}
