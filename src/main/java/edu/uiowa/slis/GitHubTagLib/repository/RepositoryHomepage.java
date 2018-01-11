package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryHomepage extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryHomepage.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getHomepage());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for homepage tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for homepage tag ");
		}
		return SKIP_BODY;
	}

	public String getHomepage() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getHomepage();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for homepage tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for homepage tag ");
		}
	}

	public void setHomepage(String homepage) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setHomepage(homepage);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for homepage tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for homepage tag ");
		}
	}

}
