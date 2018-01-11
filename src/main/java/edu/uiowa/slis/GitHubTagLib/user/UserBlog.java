package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserBlog extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserBlog.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getBlog());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for blog tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for blog tag ");
		}
		return SKIP_BODY;
	}

	public String getBlog() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getBlog();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for blog tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for blog tag ");
		}
	}

	public void setBlog(String blog) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setBlog(blog);
		} catch (Exception e) {
			log.error("Can't find enclosing User for blog tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for blog tag ");
		}
	}

}
