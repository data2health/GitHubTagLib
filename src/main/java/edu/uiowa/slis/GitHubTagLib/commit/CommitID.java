package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitID extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(CommitID.class);

	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Commit for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for ID tag ");
			}
		}
	}

}
