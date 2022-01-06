package edu.uiowa.slis.GitHubTagLib.committer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import java.sql.Timestamp;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitterMostRecent extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(CommitterMostRecent.class);

	public int doStartTag() throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			if (!theCommitter.commitNeeded) {
				pageContext.getOut().print(theCommitter.getMostRecent());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for mostRecent tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Committer for mostRecent tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Committer for mostRecent tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getMostRecent() throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			return theCommitter.getMostRecent();
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for mostRecent tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Committer for mostRecent tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Committer for mostRecent tag ");
			}
		}
	}

	public void setMostRecent(Timestamp mostRecent) throws JspException {
		try {
			Committer theCommitter = (Committer)findAncestorWithClass(this, Committer.class);
			theCommitter.setMostRecent(mostRecent);
		} catch (Exception e) {
			log.error("Can't find enclosing Committer for mostRecent tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Committer for mostRecent tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Committer for mostRecent tag ");
			}
		}
	}

}
