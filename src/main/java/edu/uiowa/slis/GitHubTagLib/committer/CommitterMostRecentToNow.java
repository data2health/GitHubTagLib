package edu.uiowa.slis.GitHubTagLib.committer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitterMostRecentToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitterMostRecentToNow.class);


	public int doStartTag() throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			theCommitter.setMostRecentToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Committer for mostRecent tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for mostRecent tag ");
		}
		return SKIP_BODY;
	}

	public Date getMostRecent() throws JspTagException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			return theCommitter.getMostRecent();
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for mostRecent tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for mostRecent tag ");
		}
	}

	public void setMostRecent( ) throws JspTagException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			theCommitter.setMostRecentToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for mostRecent tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for mostRecent tag ");
		}
	}

}
