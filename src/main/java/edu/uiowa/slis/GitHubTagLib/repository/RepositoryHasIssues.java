package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryHasIssues extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RepositoryHasIssues.class);

	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getHasIssues());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasIssues tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for hasIssues tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for hasIssues tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getHasIssues() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getHasIssues();
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasIssues tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for hasIssues tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for hasIssues tag ");
			}
		}
	}

	public void setHasIssues(boolean hasIssues) throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setHasIssues(hasIssues);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for hasIssues tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for hasIssues tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for hasIssues tag ");
			}
		}
	}

}
