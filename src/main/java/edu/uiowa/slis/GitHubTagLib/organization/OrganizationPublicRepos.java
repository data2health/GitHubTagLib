package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationPublicRepos extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationPublicRepos.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getPublicRepos());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for publicRepos tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for publicRepos tag ");
		}
		return SKIP_BODY;
	}

	public int getPublicRepos() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getPublicRepos();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for publicRepos tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for publicRepos tag ");
		}
	}

	public void setPublicRepos(int publicRepos) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setPublicRepos(publicRepos);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for publicRepos tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for publicRepos tag ");
		}
	}

}
