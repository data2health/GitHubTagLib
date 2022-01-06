package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserName extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserName.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for name tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for name tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getName() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getName();
		} catch (Exception e) {
			log.error("Can't find enclosing User for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for name tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for name tag ");
			}
		}
	}

	public void setName(String name) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing User for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for name tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for name tag ");
			}
		}
	}

}
