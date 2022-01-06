package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryFullName extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RepositoryFullName.class);

	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getFullName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for fullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for fullName tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for fullName tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getFullName() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getFullName();
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for fullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for fullName tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for fullName tag ");
			}
		}
	}

	public void setFullName(String fullName) throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setFullName(fullName);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for fullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for fullName tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for fullName tag ");
			}
		}
	}

}
