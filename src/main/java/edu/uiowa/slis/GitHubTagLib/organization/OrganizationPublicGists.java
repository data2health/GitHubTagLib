package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationPublicGists extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationPublicGists.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getPublicGists());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for publicGists tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for publicGists tag ");
		}
		return SKIP_BODY;
	}

	public int getPublicGists() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getPublicGists();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for publicGists tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for publicGists tag ");
		}
	}

	public void setPublicGists(int publicGists) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setPublicGists(publicGists);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for publicGists tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for publicGists tag ");
		}
	}

}
