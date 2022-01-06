package edu.uiowa.slis.GitHubTagLib.parent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ParentParentId extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ParentParentId.class);

	public int doStartTag() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			if (!theParent.commitNeeded) {
				pageContext.getOut().print(theParent.getParentId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for parentId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for parentId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getParentId() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			return theParent.getParentId();
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for parentId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for parentId tag ");
			}
		}
	}

	public void setParentId(int parentId) throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			theParent.setParentId(parentId);
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for parentId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for parentId tag ");
			}
		}
	}

}
