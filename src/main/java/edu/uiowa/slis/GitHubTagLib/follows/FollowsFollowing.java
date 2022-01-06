package edu.uiowa.slis.GitHubTagLib.follows;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class FollowsFollowing extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(FollowsFollowing.class);

	public int doStartTag() throws JspException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			if (!theFollows.commitNeeded) {
				pageContext.getOut().print(theFollows.getFollowing());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Follows for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Follows for following tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Follows for following tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getFollowing() throws JspException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			return theFollows.getFollowing();
		} catch (Exception e) {
			log.error("Can't find enclosing Follows for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Follows for following tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Follows for following tag ");
			}
		}
	}

	public void setFollowing(int following) throws JspException {
		try {
			Follows theFollows = (Follows)findAncestorWithClass(this, Follows.class);
			theFollows.setFollowing(following);
		} catch (Exception e) {
			log.error("Can't find enclosing Follows for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Follows for following tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Follows for following tag ");
			}
		}
	}

}
