package edu.uiowa.slis.GitHubTagLib.searchOrganization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchOrganizationRelevant extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchOrganizationRelevant.class);


	public int doStartTag() throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			if (!theSearchOrganization.commitNeeded) {
				pageContext.getOut().print(theSearchOrganization.getRelevant());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for relevant tag ");
		}
		return SKIP_BODY;
	}

	public boolean getRelevant() throws JspTagException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			return theSearchOrganization.getRelevant();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchOrganization for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for relevant tag ");
		}
	}

	public void setRelevant(boolean relevant) throws JspTagException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			theSearchOrganization.setRelevant(relevant);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for relevant tag ");
		}
	}

}
