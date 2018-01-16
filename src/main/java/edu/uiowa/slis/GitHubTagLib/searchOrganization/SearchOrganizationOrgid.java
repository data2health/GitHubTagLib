package edu.uiowa.slis.GitHubTagLib.searchOrganization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchOrganizationOrgid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchOrganizationOrgid.class);


	public int doStartTag() throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			if (!theSearchOrganization.commitNeeded) {
				pageContext.getOut().print(theSearchOrganization.getOrgid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for orgid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for orgid tag ");
		}
		return SKIP_BODY;
	}

	public int getOrgid() throws JspTagException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			return theSearchOrganization.getOrgid();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchOrganization for orgid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for orgid tag ");
		}
	}

	public void setOrgid(int orgid) throws JspTagException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			theSearchOrganization.setOrgid(orgid);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for orgid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for orgid tag ");
		}
	}

}
