package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationCreatedAtToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationCreatedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setCreatedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for createdAt tag ");
		}
		return SKIP_BODY;
	}

	public Date getCreatedAt() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getCreatedAt();
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for createdAt tag ");
		}
	}

	public void setCreatedAt( ) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setCreatedAtToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for createdAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for createdAt tag ");
		}
	}

}
