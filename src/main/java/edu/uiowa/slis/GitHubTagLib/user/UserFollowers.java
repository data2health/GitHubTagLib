package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserFollowers extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserFollowers.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getFollowers());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for followers tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for followers tag ");
		}
		return SKIP_BODY;
	}

	public int getFollowers() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getFollowers();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for followers tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for followers tag ");
		}
	}

	public void setFollowers(int followers) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setFollowers(followers);
		} catch (Exception e) {
			log.error("Can't find enclosing User for followers tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for followers tag ");
		}
	}

}
