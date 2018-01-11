package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserLogin extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserLogin.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getLogin());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for login tag ");
		}
		return SKIP_BODY;
	}

	public String getLogin() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getLogin();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for login tag ");
		}
	}

	public void setLogin(String login) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setLogin(login);
		} catch (Exception e) {
			log.error("Can't find enclosing User for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for login tag ");
		}
	}

}
