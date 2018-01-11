package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserUpdatedAtToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserUpdatedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setUpdatedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing User for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for updatedAt tag ");
		}
		return SKIP_BODY;
	}

	public Date getUpdatedAt() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getUpdatedAt();
		} catch (Exception e) {
			log.error("Can't find enclosing User for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for updatedAt tag ");
		}
	}

	public void setUpdatedAt( ) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setUpdatedAtToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing User for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for updatedAt tag ");
		}
	}

}
