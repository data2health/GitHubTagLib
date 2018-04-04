package edu.uiowa.slis.GitHubTagLib.otherCommitter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OtherCommitterRid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OtherCommitterRid.class);


	public int doStartTag() throws JspException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			if (!theOtherCommitter.commitNeeded) {
				pageContext.getOut().print(theOtherCommitter.getRid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for rid tag ");
		}
		return SKIP_BODY;
	}

	public int getRid() throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			return theOtherCommitter.getRid();
		} catch (Exception e) {
			log.error(" Can't find enclosing OtherCommitter for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for rid tag ");
		}
	}

	public void setRid(int rid) throws JspTagException {
		try {
			OtherCommitter theOtherCommitter = (OtherCommitter)findAncestorWithClass(this, OtherCommitter.class);
			theOtherCommitter.setRid(rid);
		} catch (Exception e) {
			log.error("Can't find enclosing OtherCommitter for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing OtherCommitter for rid tag ");
		}
	}

}
