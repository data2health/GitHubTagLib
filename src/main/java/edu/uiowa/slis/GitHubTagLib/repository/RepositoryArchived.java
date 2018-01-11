package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryArchived extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryArchived.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getArchived());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for archived tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for archived tag ");
		}
		return SKIP_BODY;
	}

	public boolean getArchived() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getArchived();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for archived tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for archived tag ");
		}
	}

	public void setArchived(boolean archived) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setArchived(archived);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for archived tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for archived tag ");
		}
	}

}
