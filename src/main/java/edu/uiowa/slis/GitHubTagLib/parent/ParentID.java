package edu.uiowa.slis.GitHubTagLib.parent;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ParentID extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(ParentID.class);


	public int doStartTag() throws JspException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			if (!theParent.commitNeeded) {
				pageContext.getOut().print(theParent.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			return theParent.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing Parent for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			Parent theParent = (Parent)findAncestorWithClass(this, Parent.class);
			theParent.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Parent for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Parent for ID tag ");
		}
	}

}
