package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitEmail extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitEmail.class);


	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getEmail());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for email tag ");
		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getEmail();
		} catch (Exception e) {
			log.error(" Can't find enclosing Commit for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for email tag ");
		}
	}

	public void setEmail(String email) throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setEmail(email);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for email tag ");
		}
	}

}
