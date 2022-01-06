package edu.uiowa.slis.GitHubTagLib.repository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class RepositoryStargazersCount extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RepositoryStargazersCount.class);

	public int doStartTag() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (!theRepository.commitNeeded) {
				pageContext.getOut().print(theRepository.getStargazersCount());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for stargazersCount tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for stargazersCount tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for stargazersCount tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getStargazersCount() throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			return theRepository.getStargazersCount();
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for stargazersCount tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for stargazersCount tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for stargazersCount tag ");
			}
		}
	}

	public void setStargazersCount(int stargazersCount) throws JspException {
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			theRepository.setStargazersCount(stargazersCount);
		} catch (Exception e) {
			log.error("Can't find enclosing Repository for stargazersCount tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Repository for stargazersCount tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Repository for stargazersCount tag ");
			}
		}
	}

}
