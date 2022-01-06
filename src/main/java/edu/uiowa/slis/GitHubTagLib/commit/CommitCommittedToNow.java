package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitCommittedToNow extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(CommitCommittedToNow.class);


	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setCommittedToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Commit for committed tag ", e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Commit for committed tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for committed tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getCommitted() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getCommitted();
		} catch (Exception e) {

			log.error("Can't find enclosing Commit for committed tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Commit for committed tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for committed tag ");
			}

		}
	}

	public void setCommitted() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setCommittedToNow( );
		} catch (Exception e) {

			log.error("Can't find enclosing Commit for committed tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Commit for committed tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Commit for committed tag ");
			}

		}
	}
}