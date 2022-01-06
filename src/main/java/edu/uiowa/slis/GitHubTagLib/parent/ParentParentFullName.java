package edu.uiowa.slis.GitHubTagLib.parent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ParentParentFullName extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ParentParentFullName.class);

	public int doStartTag() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			if (!theParent.commitNeeded) {
				pageContext.getOut().print(theParent.getParentFullName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentFullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for parentFullName tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for parentFullName tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getParentFullName() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			return theParent.getParentFullName();
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentFullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for parentFullName tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for parentFullName tag ");
			}
		}
	}

	public void setParentFullName(String parentFullName) throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			theParent.setParentFullName(parentFullName);
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for parentFullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for parentFullName tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for parentFullName tag ");
			}
		}
	}

}
