package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitMessage extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(CommitMessage.class);

	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getMessage());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for message tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for message tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for message tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getMessage() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getMessage();
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for message tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for message tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for message tag ");
			}
		}
	}

	public void setMessage(String message) throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setMessage(message);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for message tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for message tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for message tag ");
			}
		}
	}

}
