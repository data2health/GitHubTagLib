package edu.uiowa.slis.GitHubTagLib.readme;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ReadmeReadme extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(ReadmeReadme.class);


	public int doStartTag() throws JspException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			if (!theReadme.commitNeeded) {
				pageContext.getOut().print(theReadme.getReadme());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for readme tag ", e);
			throw new JspTagException("Error: Can't find enclosing Readme for readme tag ");
		}
		return SKIP_BODY;
	}

	public String getReadme() throws JspTagException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			return theReadme.getReadme();
		} catch (Exception e) {
			log.error(" Can't find enclosing Readme for readme tag ", e);
			throw new JspTagException("Error: Can't find enclosing Readme for readme tag ");
		}
	}

	public void setReadme(String readme) throws JspTagException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			theReadme.setReadme(readme);
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for readme tag ", e);
			throw new JspTagException("Error: Can't find enclosing Readme for readme tag ");
		}
	}

}
