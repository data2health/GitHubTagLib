package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryStargazersCount extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryStargazersCount.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getStargazersCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for stargazersCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for stargazersCount tag ");
		}
		return SKIP_BODY;
	}

	public int getStargazersCount() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getStargazersCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for stargazersCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for stargazersCount tag ");
		}
	}

	public void setStargazersCount(int stargazersCount) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setStargazersCount(stargazersCount);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for stargazersCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for stargazersCount tag ");
		}
	}

}
