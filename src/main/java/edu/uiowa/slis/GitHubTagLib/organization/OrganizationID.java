package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationID extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationID.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for ID tag ");
		}
	}

}
