package edu.uiowa.slis.GitHubTagLib.searchRepository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchRepositorySid extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SearchRepositorySid.class);

	public int doStartTag() throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			if (!theSearchRepository.commitNeeded) {
				pageContext.getOut().print(theSearchRepository.getSid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for sid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchRepository for sid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchRepository for sid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getSid() throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			return theSearchRepository.getSid();
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for sid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchRepository for sid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchRepository for sid tag ");
			}
		}
	}

	public void setSid(int sid) throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			theSearchRepository.setSid(sid);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for sid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchRepository for sid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchRepository for sid tag ");
			}
		}
	}

}
