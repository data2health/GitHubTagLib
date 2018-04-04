package edu.uiowa.slis.GitHubTagLib.committer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitterUid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitterUid.class);


	public int doStartTag() throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			if (!theCommitter.commitNeeded) {
				pageContext.getOut().print(theCommitter.getUid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for uid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for uid tag ");
		}
		return SKIP_BODY;
	}

	public int getUid() throws JspTagException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			return theCommitter.getUid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Committer for uid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for uid tag ");
		}
	}

	public void setUid(int uid) throws JspTagException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			theCommitter.setUid(uid);
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for uid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for uid tag ");
		}
	}

}
