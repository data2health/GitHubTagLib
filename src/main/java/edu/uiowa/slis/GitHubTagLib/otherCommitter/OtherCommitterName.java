package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterName extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherCommitterName.class);


	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			if (!theOtherCommitter.commitNeeded) {
				pageContext.getOut().print(theOtherCommitter.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getName();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherCommitter for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for name tag ");
		}
	}

}
