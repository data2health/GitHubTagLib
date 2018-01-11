package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryFullName extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryFullName.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getFullName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for fullName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for fullName tag ");
		}
		return SKIP_BODY;
	}

	public String getFullName() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getFullName();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for fullName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for fullName tag ");
		}
	}

	public void setFullName(String fullName) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setFullName(fullName);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for fullName tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for fullName tag ");
		}
	}

}
