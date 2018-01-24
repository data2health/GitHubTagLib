package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitMessage extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitMessage.class);


	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getMessage());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for message tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for message tag ");
		}
		return SKIP_BODY;
	}

	public String getMessage() throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getMessage();
		} catch (Exception e) {
			log.error(" Can't find enclosing Commit for message tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for message tag ");
		}
	}

	public void setMessage(String message) throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setMessage(message);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for message tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for message tag ");
		}
	}

}
