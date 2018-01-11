package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryFork extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryFork.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getFork());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for fork tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for fork tag ");
		}
		return SKIP_BODY;
	}

	public boolean getFork() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getFork();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for fork tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for fork tag ");
		}
	}

	public void setFork(boolean fork) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setFork(fork);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for fork tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for fork tag ");
		}
	}

}
