package edu.uiowa.slis.GitHubTagLib.searchOrganization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchOrganizationSid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchOrganizationSid.class);


	public int doStartTag() throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			if (!theSearchOrganization.commitNeeded) {
				pageContext.getOut().print(theSearchOrganization.getSid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for sid tag ");
		}
		return SKIP_BODY;
	}

	public int getSid() throws JspTagException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			return theSearchOrganization.getSid();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchOrganization for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for sid tag ");
		}
	}

	public void setSid(int sid) throws JspTagException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			theSearchOrganization.setSid(sid);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchOrganization for sid tag ");
		}
	}

}
