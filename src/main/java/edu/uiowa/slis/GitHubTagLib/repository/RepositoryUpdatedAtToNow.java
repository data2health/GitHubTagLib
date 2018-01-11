package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryUpdatedAtToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryUpdatedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setUpdatedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for updatedAt tag ");
		}
		return SKIP_BODY;
	}

	public Date getUpdatedAt() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getUpdatedAt();
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for updatedAt tag ");
		}
	}

	public void setUpdatedAt( ) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setUpdatedAtToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for updatedAt tag ");
		}
	}

}
