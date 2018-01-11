package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationUpdatedAtToNow extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationUpdatedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setUpdatedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for updatedAt tag ");
		}
		return SKIP_BODY;
	}

	public Date getUpdatedAt() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getUpdatedAt();
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for updatedAt tag ");
		}
	}

	public void setUpdatedAt( ) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setUpdatedAtToNow( );
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for updatedAt tag ");
		}
	}

}
