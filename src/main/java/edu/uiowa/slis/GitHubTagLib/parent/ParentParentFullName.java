package edu.uiowa.slis.GitHubTagLib.parent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ParentParentFullName extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(ParentParentFullName.class);


	public int doStartTag() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			if (!theParent.commitNeeded) {
				pageContext.getOut().print(theParent.getParentFullName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentFullName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for parentFullName tag ");
		}
		return SKIP_BODY;
	}

	public String getParentFullName() throws JspTagException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			return theParent.getParentFullName();
		} catch (Exception e) {
			log.error(" Can't find enclosing Parent for parentFullName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for parentFullName tag ");
		}
	}

	public void setParentFullName(String parentFullName) throws JspTagException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			theParent.setParentFullName(parentFullName);
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentFullName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for parentFullName tag ");
		}
	}

}
