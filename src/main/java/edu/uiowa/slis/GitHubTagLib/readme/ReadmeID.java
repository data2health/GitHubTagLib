package edu.uiowa.slis.GitHubTagLib.readme;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ReadmeID extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ReadmeID.class);

	public int doStartTag() throws JspException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			if (!theReadme.commitNeeded) {
				pageContext.getOut().print(theReadme.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Readme for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Readme for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			return theReadme.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Readme for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Readme for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			theReadme.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Readme for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Readme for ID tag ");
			}
		}
	}

}
