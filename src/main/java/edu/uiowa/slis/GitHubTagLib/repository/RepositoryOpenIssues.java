package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryOpenIssues extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryOpenIssues.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getOpenIssues());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for openIssues tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for openIssues tag ");
		}
		return SKIP_BODY;
	}

	public int getOpenIssues() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getOpenIssues();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for openIssues tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for openIssues tag ");
		}
	}

	public void setOpenIssues(int openIssues) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setOpenIssues(openIssues);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for openIssues tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for openIssues tag ");
		}
	}

}
