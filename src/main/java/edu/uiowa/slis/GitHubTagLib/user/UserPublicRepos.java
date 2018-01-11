package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserPublicRepos extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserPublicRepos.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getPublicRepos());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for publicRepos tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for publicRepos tag ");
		}
		return SKIP_BODY;
	}

	public int getPublicRepos() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getPublicRepos();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for publicRepos tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for publicRepos tag ");
		}
	}

	public void setPublicRepos(int publicRepos) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setPublicRepos(publicRepos);
		} catch (Exception e) {
			log.error("Can't find enclosing User for publicRepos tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for publicRepos tag ");
		}
	}

}
