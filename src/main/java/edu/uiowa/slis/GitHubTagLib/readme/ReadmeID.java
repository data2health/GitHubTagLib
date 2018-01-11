package edu.uiowa.slis.GitHubTagLib.readme;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class ReadmeID extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(ReadmeID.class);


	public int doStartTag() throws JspException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			if (!theReadme.commitNeeded) {
				pageContext.getOut().print(theReadme.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Readme for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			return theReadme.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing Readme for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Readme for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			Readme theReadme = (Readme)findAncestorWithClass(this, Readme.class);
			theReadme.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Readme for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Readme for ID tag ");
		}
	}

}
