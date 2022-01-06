package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import java.sql.Timestamp;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterMostRecent extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OtherCommitterMostRecent.class);

	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			if (!theOtherCommitter.commitNeeded) {
				pageContext.getOut().print(theOtherCommitter.getMostRecent());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for mostRecent tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for mostRecent tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for mostRecent tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getMostRecent() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getMostRecent();
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for mostRecent tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for mostRecent tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for mostRecent tag ");
			}
		}
	}

	public void setMostRecent(Timestamp mostRecent) throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setMostRecent(mostRecent);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for mostRecent tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OtherCommitter for mostRecent tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OtherCommitter for mostRecent tag ");
			}
		}
	}

}
