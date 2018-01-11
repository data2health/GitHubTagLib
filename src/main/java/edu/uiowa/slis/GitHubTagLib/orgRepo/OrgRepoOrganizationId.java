package edu.uiowa.slis.GitHubTagLib.orgRepo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrgRepoOrganizationId extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrgRepoOrganizationId.class);


	public int doStartTag() throws JspException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			if (!theOrgRepo.commitNeeded) {
				pageContext.getOut().print(theOrgRepo.getOrganizationId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OrgRepo for organizationId tag ", e);
			throw new JspTagException("Error: Can't find enclosing OrgRepo for organizationId tag ");
		}
		return SKIP_BODY;
	}

	public int getOrganizationId() throws JspTagException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			return theOrgRepo.getOrganizationId();
		} catch (Exception e) {
			log.error(" Can't find enclosing OrgRepo for organizationId tag ", e);
			throw new JspTagException("Error: Can't find enclosing OrgRepo for organizationId tag ");
		}
	}

	public void setOrganizationId(int organizationId) throws JspTagException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			theOrgRepo.setOrganizationId(organizationId);
		} catch (Exception e) {
			log.error("Can't find enclosing OrgRepo for organizationId tag ", e);
			throw new JspTagException("Error: Can't find enclosing OrgRepo for organizationId tag ");
		}
	}

}
