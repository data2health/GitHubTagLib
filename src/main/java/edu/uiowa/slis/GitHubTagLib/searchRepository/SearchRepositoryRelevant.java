package edu.uiowa.slis.GitHubTagLib.searchRepository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchRepositoryRelevant extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SearchRepositoryRelevant.class);

	public int doStartTag() throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			if (!theSearchRepository.commitNeeded) {
				pageContext.getOut().print(theSearchRepository.getRelevant());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for relevant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchRepository for relevant tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchRepository for relevant tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getRelevant() throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			return theSearchRepository.getRelevant();
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for relevant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchRepository for relevant tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchRepository for relevant tag ");
			}
		}
	}

	public void setRelevant(boolean relevant) throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			theSearchRepository.setRelevant(relevant);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for relevant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchRepository for relevant tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchRepository for relevant tag ");
			}
		}
	}

}
