package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryWatchersCount extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryWatchersCount.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getWatchersCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for watchersCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for watchersCount tag ");
		}
		return SKIP_BODY;
	}

	public int getWatchersCount() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getWatchersCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for watchersCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for watchersCount tag ");
		}
	}

	public void setWatchersCount(int watchersCount) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setWatchersCount(watchersCount);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for watchersCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for watchersCount tag ");
		}
	}

}
