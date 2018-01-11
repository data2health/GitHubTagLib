package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryName extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryName.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getName();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for name tag ");
		}
	}

}
