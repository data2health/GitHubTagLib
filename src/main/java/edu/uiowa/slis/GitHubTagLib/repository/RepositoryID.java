package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryID extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(RepositoryID.class);


	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing Repository for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Repository for ID tag ");
		}
	}

}
