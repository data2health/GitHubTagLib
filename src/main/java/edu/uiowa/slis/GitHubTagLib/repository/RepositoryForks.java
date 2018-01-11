package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryForks extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryForks.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getForks());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for forks tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for forks tag ");
		}
		return SKIP_BODY;
	}

	public int getForks() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getForks();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for forks tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for forks tag ");
		}
	}

	public void setForks(int forks) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setForks(forks);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for forks tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for forks tag ");
		}
	}

}
