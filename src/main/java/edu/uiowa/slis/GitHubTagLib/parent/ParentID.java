package edu.uiowa.slis.GitHubTagLib.parent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ParentID extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ParentID.class);

	public int doStartTag() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			if (!theParent.commitNeeded) {
				pageContext.getOut().print(theParent.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			return theParent.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			theParent.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Parent for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Parent for ID tag ");
			}
		}
	}

}
