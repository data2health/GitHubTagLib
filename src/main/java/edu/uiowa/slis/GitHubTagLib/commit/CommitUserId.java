package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitUserId extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(CommitUserId.class);

	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getUserId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for userId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for userId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getUserId() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getUserId();
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for userId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for userId tag ");
			}
		}
	}

	public void setUserId(int userId) throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setUserId(userId);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for userId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for userId tag ");
			}
		}
	}

}
