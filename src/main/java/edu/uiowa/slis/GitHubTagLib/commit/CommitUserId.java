package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitUserId extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitUserId.class);


	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getUserId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for userId tag ");
		}
		return SKIP_BODY;
	}

	public int getUserId() throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getUserId();
		} catch (Exception e) {
			log.error(" Can't find enclosing Commit for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for userId tag ");
		}
	}

	public void setUserId(int userId) throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setUserId(userId);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for userId tag ");
		}
	}

}
