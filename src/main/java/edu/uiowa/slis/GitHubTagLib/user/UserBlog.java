package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserBlog extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserBlog.class);

	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getBlog());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for blog tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for blog tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for blog tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getBlog() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getBlog();
		} catch (Exception e) {
			log.error("Can't find enclosing User for blog tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for blog tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for blog tag ");
			}
		}
	}

	public void setBlog(String blog) throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setBlog(blog);
		} catch (Exception e) {
			log.error("Can't find enclosing User for blog tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing User for blog tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for blog tag ");
			}
		}
	}

}
