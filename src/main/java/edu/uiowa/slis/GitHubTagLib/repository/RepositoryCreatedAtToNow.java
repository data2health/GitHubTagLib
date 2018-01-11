package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryCreatedAtToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryCreatedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setCreatedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for createdAt tag ");
		}
		return SKIP_BODY;
	}

	public Date getCreatedAt() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getCreatedAt();
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for createdAt tag ");
		}
	}

	public void setCreatedAt( ) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setCreatedAtToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for createdAt tag ");
		}
	}

}
