package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserPublicGists extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserPublicGists.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getPublicGists());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for publicGists tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for publicGists tag ");
		}
		return SKIP_BODY;
	}

	public int getPublicGists() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getPublicGists();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for publicGists tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for publicGists tag ");
		}
	}

	public void setPublicGists(int publicGists) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setPublicGists(publicGists);
		} catch (Exception e) {
			log.error("Can't find enclosing User for publicGists tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for publicGists tag ");
		}
	}

}
