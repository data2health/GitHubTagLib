package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryHasProjects extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryHasProjects.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getHasProjects());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasProjects tag ");
		}
		return SKIP_BODY;
	}

	public boolean getHasProjects() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getHasProjects();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for hasProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasProjects tag ");
		}
	}

	public void setHasProjects(boolean hasProjects) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setHasProjects(hasProjects);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasProjects tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for hasProjects tag ");
		}
	}

}
