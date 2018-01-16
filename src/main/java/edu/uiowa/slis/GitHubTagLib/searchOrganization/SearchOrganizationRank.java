package edu.uiowa.slis.GitHubTagLib.searchOrganization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchOrganizationRank extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchOrganizationRank.class);


	public int doStartTag() throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			if (!theSearchOrganization.commitNeeded) {
				pageContext.getOut().print(theSearchOrganization.getRank());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for rank tag ");
		}
		return SKIP_BODY;
	}

	public int getRank() throws JspTagException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			return theSearchOrganization.getRank();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchOrganization for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for rank tag ");
		}
	}

	public void setRank(int rank) throws JspTagException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			theSearchOrganization.setRank(rank);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for rank tag ");
		}
	}

}
