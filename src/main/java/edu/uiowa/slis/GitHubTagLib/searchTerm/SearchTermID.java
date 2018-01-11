package edu.uiowa.slis.GitHubTagLib.searchTerm;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchTermID extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchTermID.class);


	public int doStartTag() throws JspException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			if (!theSearchTerm.commitNeeded) {
				pageContext.getOut().print(theSearchTerm.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchTerm for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			return theSearchTerm.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchTerm for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchTerm for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
			theSearchTerm.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchTerm for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchTerm for ID tag ");
		}
	}

}
