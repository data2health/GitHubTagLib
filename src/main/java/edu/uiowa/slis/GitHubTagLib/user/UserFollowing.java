package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserFollowing extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserFollowing.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getFollowing());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for following tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for following tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getFollowing() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getFollowing();
		} catch (Exception e) {
			log.error("Can't find enclosing User for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for following tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for following tag ");
			}
		}
	}

	public void setFollowing(int following) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setFollowing(following);
		} catch (Exception e) {
			log.error("Can't find enclosing User for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for following tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for following tag ");
			}
		}
	}

}
