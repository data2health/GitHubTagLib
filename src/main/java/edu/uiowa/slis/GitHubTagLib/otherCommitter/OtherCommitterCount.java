package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterCount extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherCommitterCount.class);


	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			if (!theOtherCommitter.commitNeeded) {
				pageContext.getOut().print(theOtherCommitter.getCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for count tag ");
		}
		return SKIP_BODY;
	}

	public int getCount() throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherCommitter for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for count tag ");
		}
	}

	public void setCount(int count) throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setCount(count);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for count tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for count tag ");
		}
	}

}
