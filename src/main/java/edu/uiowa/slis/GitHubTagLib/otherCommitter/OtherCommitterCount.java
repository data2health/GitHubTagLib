package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterCount extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OtherCommitterCount.class);

	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			if (!theOtherCommitter.commitNeeded) {
				pageContext.getOut().print(theOtherCommitter.getCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for count tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for count tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getCount() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getCount();
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for count tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for count tag ");
			}
		}
	}

	public void setCount(int count) throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setCount(count);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for count tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for count tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for count tag ");
			}
		}
	}

}
