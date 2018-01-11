package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserCreatedAtToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserCreatedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setCreatedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing User for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for createdAt tag ");
		}
		return SKIP_BODY;
	}

	public Date getCreatedAt() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getCreatedAt();
		} catch (Exception e) {
			log.error("Can't find enclosing User for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for createdAt tag ");
		}
	}

	public void setCreatedAt( ) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setCreatedAtToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing User for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for createdAt tag ");
		}
	}

}
