package edu.uiowa.slis.GitHubTagLib.follows;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class FollowsFollower extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(FollowsFollower.class);


	public int doStartTag() throws JspException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			if (!theFollows.commitNeeded) {
				pageContext.getOut().print(theFollows.getFollower());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Follows for follower tag ", e);
			throw new JspTagException("Error: Can't find enclosing Follows for follower tag ");
		}
		return SKIP_BODY;
	}

	public int getFollower() throws JspTagException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			return theFollows.getFollower();
		} catch (Exception e) {
			log.error(" Can't find enclosing Follows for follower tag ", e);
			throw new JspTagException("Error: Can't find enclosing Follows for follower tag ");
		}
	}

	public void setFollower(int follower) throws JspTagException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			theFollows.setFollower(follower);
		} catch (Exception e) {
			log.error("Can't find enclosing Follows for follower tag ", e);
			throw new JspTagException("Error: Can't find enclosing Follows for follower tag ");
		}
	}

}
