package edu.uiowa.slis.GitHubTagLib.searchTerm;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchTermTerm extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchTermTerm.class);


	public int doStartTag() throws JspException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			if (!theSearchTerm.commitNeeded) {
				pageContext.getOut().print(theSearchTerm.getTerm());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for term tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchTerm for term tag ");
		}
		return SKIP_BODY;
	}

	public String getTerm() throws JspTagException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			return theSearchTerm.getTerm();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchTerm for term tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchTerm for term tag ");
		}
	}

	public void setTerm(String term) throws JspTagException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			theSearchTerm.setTerm(term);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for term tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchTerm for term tag ");
		}
	}

}
