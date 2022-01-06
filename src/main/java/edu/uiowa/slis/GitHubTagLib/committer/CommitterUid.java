package edu.uiowa.slis.GitHubTagLib.committer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitterUid extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(CommitterUid.class);

	public int doStartTag() throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			if (!theCommitter.commitNeeded) {
				pageContext.getOut().print(theCommitter.getUid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for uid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Committer for uid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Committer for uid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getUid() throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			return theCommitter.getUid();
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for uid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Committer for uid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Committer for uid tag ");
			}
		}
	}

	public void setUid(int uid) throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			theCommitter.setUid(uid);
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for uid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Committer for uid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Committer for uid tag ");
			}
		}
	}

}
