package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterEmail extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OtherCommitterEmail.class);

	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			if (!theOtherCommitter.commitNeeded) {
				pageContext.getOut().print(theOtherCommitter.getEmail());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for email tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for email tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getEmail();
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for email tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for email tag ");
			}
		}
	}

	public void setEmail(String email) throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setEmail(email);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for email tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for email tag ");
			}
		}
	}

}
