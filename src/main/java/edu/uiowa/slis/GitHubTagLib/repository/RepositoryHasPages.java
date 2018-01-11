package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryHasPages extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryHasPages.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getHasPages());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasPages tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasPages tag ");
		}
		return SKIP_BODY;
	}

	public boolean getHasPages() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getHasPages();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for hasPages tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasPages tag ");
		}
	}

	public void setHasPages(boolean hasPages) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setHasPages(hasPages);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasPages tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasPages tag ");
		}
	}

}
