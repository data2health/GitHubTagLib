package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationCompany extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationCompany.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getCompany());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for company tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for company tag ");
		}
		return SKIP_BODY;
	}

	public String getCompany() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getCompany();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for company tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for company tag ");
		}
	}

	public void setCompany(String company) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setCompany(company);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for company tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for company tag ");
		}
	}

}
