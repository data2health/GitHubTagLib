package edu.uiowa.slis.GitHubTagLib.searchTerm;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchTermTerm extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SearchTermTerm.class);

	public int doStartTag() throws JspException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			if (!theSearchTerm.commitNeeded) {
				pageContext.getOut().print(theSearchTerm.getTerm());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for term tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchTerm for term tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchTerm for term tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getTerm() throws JspException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			return theSearchTerm.getTerm();
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for term tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchTerm for term tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchTerm for term tag ");
			}
		}
	}

	public void setTerm(String term) throws JspException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			theSearchTerm.setTerm(term);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for term tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchTerm for term tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchTerm for term tag ");
			}
		}
	}

}
