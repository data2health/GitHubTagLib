package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterEmail extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherCommitterEmail.class);


	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			if (!theOtherCommitter.commitNeeded) {
				pageContext.getOut().print(theOtherCommitter.getEmail());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for email tag ");
		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getEmail();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherCommitter for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for email tag ");
		}
	}

	public void setEmail(String email) throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setEmail(email);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for email tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for email tag ");
		}
	}

}
