package edu.uiowa.slis.GitHubTagLib.follows;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class FollowsFollowing extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(FollowsFollowing.class);


	public int doStartTag() throws JspException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			if (!theFollows.commitNeeded) {
				pageContext.getOut().print(theFollows.getFollowing());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Follows for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing Follows for following tag ");
		}
		return SKIP_BODY;
	}

	public int getFollowing() throws JspTagException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			return theFollows.getFollowing();
		} catch (Exception e) {
			log.error(" Can't find enclosing Follows for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing Follows for following tag ");
		}
	}

	public void setFollowing(int following) throws JspTagException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			theFollows.setFollowing(following);
		} catch (Exception e) {
			log.error("Can't find enclosing Follows for following tag ", e);
			throw new JspTagException("Error: Can't find enclosing Follows for following tag ");
		}
	}

}
