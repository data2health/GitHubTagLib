package edu.uiowa.slis.GitHubTagLib.parent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ParentParentId extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(ParentParentId.class);


	public int doStartTag() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			if (!theParent.commitNeeded) {
				pageContext.getOut().print(theParent.getParentId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for parentId tag ");
		}
		return SKIP_BODY;
	}

	public int getParentId() throws JspTagException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			return theParent.getParentId();
		} catch (Exception e) {
			log.error(" Can't find enclosing Parent for parentId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for parentId tag ");
		}
	}

	public void setParentId(int parentId) throws JspTagException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			theParent.setParentId(parentId);
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for parentId tag ");
		}
	}

}
