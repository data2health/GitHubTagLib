package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryPushedAtToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryPushedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setPushedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for pushedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for pushedAt tag ");
		}
		return SKIP_BODY;
	}

	public Date getPushedAt() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getPushedAt();
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for pushedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for pushedAt tag ");
		}
	}

	public void setPushedAt( ) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setPushedAtToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for pushedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for pushedAt tag ");
		}
	}

}
