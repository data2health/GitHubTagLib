package edu.uiowa.slis.GitHubTagLib.committer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitterRid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitterRid.class);


	public int doStartTag() throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			if (!theCommitter.commitNeeded) {
				pageContext.getOut().print(theCommitter.getRid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for rid tag ");
		}
		return SKIP_BODY;
	}

	public int getRid() throws JspTagException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			return theCommitter.getRid();
		} catch (Exception e) {
			log.error(" Can't find enclosing Committer for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for rid tag ");
		}
	}

	public void setRid(int rid) throws JspTagException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			theCommitter.setRid(rid);
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for rid tag ");
		}
	}

}
