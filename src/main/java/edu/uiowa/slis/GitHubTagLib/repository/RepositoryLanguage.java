package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryLanguage extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryLanguage.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getLanguage());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for language tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for language tag ");
		}
		return SKIP_BODY;
	}

	public String getLanguage() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getLanguage();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for language tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for language tag ");
		}
	}

	public void setLanguage(String language) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setLanguage(language);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for language tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for language tag ");
		}
	}

}
