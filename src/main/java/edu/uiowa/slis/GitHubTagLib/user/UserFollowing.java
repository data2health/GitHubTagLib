package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserFollowing extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserFollowing.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getFollowing());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for following tag ");
		}
		return SKIP_BODY;
	}

	public int getFollowing() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getFollowing();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for following tag ");
		}
	}

	public void setFollowing(int following) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setFollowing(following);
		} catch (Exception e) {
			log.error("Can't find enclosing User for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for following tag ");
		}
	}

}
