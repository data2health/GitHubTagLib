package edu.uiowa.slis.GitHubTagLib.readme;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ReadmeReadme extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ReadmeReadme.class);

	public int doStartTag() throws JspException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			if (!theReadme.commitNeeded) {
				pageContext.getOut().print(theReadme.getReadme());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for readme tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Readme for readme tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Readme for readme tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getReadme() throws JspException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			return theReadme.getReadme();
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for readme tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Readme for readme tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Readme for readme tag ");
			}
		}
	}

	public void setReadme(String readme) throws JspException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			theReadme.setReadme(readme);
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for readme tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Readme for readme tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Readme for readme tag ");
			}
		}
	}

}
