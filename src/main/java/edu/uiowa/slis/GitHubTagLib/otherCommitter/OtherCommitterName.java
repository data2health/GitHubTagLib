package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterName extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OtherCommitterName.class);

	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			if (!theOtherCommitter.commitNeeded) {
				pageContext.getOut().print(theOtherCommitter.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for name tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for name tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getName() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getName();
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for name tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for name tag ");
			}
		}
	}

	public void setName(String name) throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for name tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for name tag ");
			}
		}
	}

}
