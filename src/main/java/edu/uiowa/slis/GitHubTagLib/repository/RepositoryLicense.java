package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryLicense extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryLicense.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getLicense());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for license tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for license tag ");
		}
		return SKIP_BODY;
	}

	public String getLicense() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getLicense();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for license tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for license tag ");
		}
	}

	public void setLicense(String license) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setLicense(license);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for license tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for license tag ");
		}
	}

}
