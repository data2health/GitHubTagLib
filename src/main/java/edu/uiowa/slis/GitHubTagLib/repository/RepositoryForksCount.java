package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryForksCount extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryForksCount.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getForksCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for forksCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for forksCount tag ");
		}
		return SKIP_BODY;
	}

	public int getForksCount() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getForksCount();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for forksCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for forksCount tag ");
		}
	}

	public void setForksCount(int forksCount) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setForksCount(forksCount);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for forksCount tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for forksCount tag ");
		}
	}

}
