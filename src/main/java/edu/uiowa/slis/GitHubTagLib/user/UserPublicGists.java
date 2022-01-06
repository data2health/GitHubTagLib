package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserPublicGists extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserPublicGists.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getPublicGists());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for publicGists tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for publicGists tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for publicGists tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getPublicGists() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getPublicGists();
		} catch (Exception e) {
			log.error("Can't find enclosing User for publicGists tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for publicGists tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for publicGists tag ");
			}
		}
	}

	public void setPublicGists(int publicGists) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setPublicGists(publicGists);
		} catch (Exception e) {
			log.error("Can't find enclosing User for publicGists tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for publicGists tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for publicGists tag ");
			}
		}
	}

}
