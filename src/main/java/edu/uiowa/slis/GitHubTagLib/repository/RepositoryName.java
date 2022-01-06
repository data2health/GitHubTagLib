package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryName extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RepositoryName.class);

	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for name tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for name tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getName() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getName();
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for name tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for name tag ");
			}
		}
	}

	public void setName(String name) throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for name tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for name tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for name tag ");
			}
		}
	}

}
