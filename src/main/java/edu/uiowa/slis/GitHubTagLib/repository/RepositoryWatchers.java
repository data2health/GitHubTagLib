package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryWatchers extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryWatchers.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getWatchers());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for watchers tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for watchers tag ");
		}
		return SKIP_BODY;
	}

	public int getWatchers() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getWatchers();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for watchers tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for watchers tag ");
		}
	}

	public void setWatchers(int watchers) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setWatchers(watchers);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for watchers tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for watchers tag ");
		}
	}

}
