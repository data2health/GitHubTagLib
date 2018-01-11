package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserName extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserName.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getName();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing User for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for name tag ");
		}
	}

}
