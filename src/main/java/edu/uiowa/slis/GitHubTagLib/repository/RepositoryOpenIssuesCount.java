package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryOpenIssuesCount extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryOpenIssuesCount.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getOpenIssuesCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for openIssuesCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for openIssuesCount tag ");
		}
		return SKIP_BODY;
	}

	public int getOpenIssuesCount() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getOpenIssuesCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for openIssuesCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for openIssuesCount tag ");
		}
	}

	public void setOpenIssuesCount(int openIssuesCount) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setOpenIssuesCount(openIssuesCount);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for openIssuesCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for openIssuesCount tag ");
		}
	}

}
