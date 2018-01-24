package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitCommittedToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitCommittedToNow.class);


	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setCommittedToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Commit for committed tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for committed tag ");
		}
		return SKIP_BODY;
	}

	public Date getCommitted() throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getCommitted();
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for committed tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for committed tag ");
		}
	}

	public void setCommitted( ) throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setCommittedToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for committed tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for committed tag ");
		}
	}

}
