package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryHasDownloads extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryHasDownloads.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getHasDownloads());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasDownloads tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasDownloads tag ");
		}
		return SKIP_BODY;
	}

	public boolean getHasDownloads() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getHasDownloads();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for hasDownloads tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasDownloads tag ");
		}
	}

	public void setHasDownloads(boolean hasDownloads) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setHasDownloads(hasDownloads);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasDownloads tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasDownloads tag ");
		}
	}

}
