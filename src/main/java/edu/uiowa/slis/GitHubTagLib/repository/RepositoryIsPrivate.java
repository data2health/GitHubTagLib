package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryIsPrivate extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RepositoryIsPrivate.class);

	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getIsPrivate());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for isPrivate tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for isPrivate tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for isPrivate tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getIsPrivate() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getIsPrivate();
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for isPrivate tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for isPrivate tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for isPrivate tag ");
			}
		}
	}

	public void setIsPrivate(boolean isPrivate) throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setIsPrivate(isPrivate);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for isPrivate tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for isPrivate tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for isPrivate tag ");
			}
		}
	}

}
