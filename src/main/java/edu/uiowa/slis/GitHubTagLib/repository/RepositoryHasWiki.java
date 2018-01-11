package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryHasWiki extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryHasWiki.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getHasWiki());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasWiki tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasWiki tag ");
		}
		return SKIP_BODY;
	}

	public boolean getHasWiki() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getHasWiki();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for hasWiki tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasWiki tag ");
		}
	}

	public void setHasWiki(boolean hasWiki) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setHasWiki(hasWiki);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasWiki tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasWiki tag ");
		}
	}

}
