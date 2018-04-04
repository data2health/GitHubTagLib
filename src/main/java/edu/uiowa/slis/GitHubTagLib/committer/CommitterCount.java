package edu.uiowa.slis.GitHubTagLib.committer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitterCount extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitterCount.class);


	public int doStartTag() throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			if (!theCommitter.commitNeeded) {
				pageContext.getOut().print(theCommitter.getCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for count tag ");
		}
		return SKIP_BODY;
	}

	public int getCount() throws JspTagException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			return theCommitter.getCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing Committer for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for count tag ");
		}
	}

	public void setCount(int count) throws JspTagException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			theCommitter.setCount(count);
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing Committer for count tag ");
		}
	}

}
