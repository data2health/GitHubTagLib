package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryDescription extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryDescription.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getDescription());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for description tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for description tag ");
		}
		return SKIP_BODY;
	}

	public String getDescription() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getDescription();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for description tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for description tag ");
		}
	}

	public void setDescription(String description) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setDescription(description);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for description tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for description tag ");
		}
	}

}
