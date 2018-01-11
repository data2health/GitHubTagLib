package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserBio extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserBio.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getBio());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for bio tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for bio tag ");
		}
		return SKIP_BODY;
	}

	public String getBio() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getBio();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for bio tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for bio tag ");
		}
	}

	public void setBio(String bio) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setBio(bio);
		} catch (Exception e) {
			log.error("Can't find enclosing User for bio tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for bio tag ");
		}
	}

}
