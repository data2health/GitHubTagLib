package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserEmail extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserEmail.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getEmail());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for email tag ");
		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getEmail();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for email tag ");
		}
	}

	public void setEmail(String email) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setEmail(email);
		} catch (Exception e) {
			log.error("Can't find enclosing User for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for email tag ");
		}
	}

}
