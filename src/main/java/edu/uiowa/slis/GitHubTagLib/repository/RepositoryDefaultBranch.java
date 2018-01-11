package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryDefaultBranch extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryDefaultBranch.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getDefaultBranch());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for defaultBranch tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for defaultBranch tag ");
		}
		return SKIP_BODY;
	}

	public String getDefaultBranch() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getDefaultBranch();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for defaultBranch tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for defaultBranch tag ");
		}
	}

	public void setDefaultBranch(String defaultBranch) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setDefaultBranch(defaultBranch);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for defaultBranch tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for defaultBranch tag ");
		}
	}

}
