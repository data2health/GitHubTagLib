package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryHasIssues extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryHasIssues.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getHasIssues());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasIssues tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasIssues tag ");
		}
		return SKIP_BODY;
	}

	public boolean getHasIssues() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getHasIssues();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for hasIssues tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasIssues tag ");
		}
	}

	public void setHasIssues(boolean hasIssues) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setHasIssues(hasIssues);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasIssues tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasIssues tag ");
		}
	}

}
