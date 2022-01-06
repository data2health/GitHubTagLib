package edu.uiowa.slis.GitHubTagLib.searchTerm;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchTermID extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SearchTermID.class);

	public int doStartTag() throws JspException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			if (!theSearchTerm.commitNeeded) {
				pageContext.getOut().print(theSearchTerm.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchTerm for ID tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchTerm for ID tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getID() throws JspException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			return theSearchTerm.getID();
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchTerm for ID tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchTerm for ID tag ");
			}
		}
	}

	public void setID(int ID) throws JspException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			theSearchTerm.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for ID tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchTerm for ID tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchTerm for ID tag ");
			}
		}
	}

}
