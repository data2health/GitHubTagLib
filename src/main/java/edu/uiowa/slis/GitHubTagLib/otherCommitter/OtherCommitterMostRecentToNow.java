package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterMostRecentToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherCommitterMostRecentToNow.class);


	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setMostRecentToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherCommitter for mostRecent tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for mostRecent tag ");
		}
		return SKIP_BODY;
	}

	public Date getMostRecent() throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getMostRecent();
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for mostRecent tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for mostRecent tag ");
		}
	}

	public void setMostRecent( ) throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setMostRecentToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for mostRecent tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for mostRecent tag ");
		}
	}

}
