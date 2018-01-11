package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryIsPrivate extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryIsPrivate.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getIsPrivate());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for isPrivate tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for isPrivate tag ");
		}
		return SKIP_BODY;
	}

	public boolean getIsPrivate() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getIsPrivate();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for isPrivate tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for isPrivate tag ");
		}
	}

	public void setIsPrivate(boolean isPrivate) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setIsPrivate(isPrivate);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for isPrivate tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for isPrivate tag ");
		}
	}

}
