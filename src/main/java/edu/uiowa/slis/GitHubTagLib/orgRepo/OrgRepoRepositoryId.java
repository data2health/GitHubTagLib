package edu.uiowa.slis.GitHubTagLib.orgRepo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrgRepoRepositoryId extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrgRepoRepositoryId.class);


	public int doStartTag() throws JspException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			if (!theOrgRepo.commitNeeded) {
				pageContext.getOut().print(theOrgRepo.getRepositoryId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OrgRepo for repositoryId tag ", e);
			throw new JspTagException("Error: Can't find enclosing OrgRepo for repositoryId tag ");
		}
		return SKIP_BODY;
	}

	public int getRepositoryId() throws JspTagException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			return theOrgRepo.getRepositoryId();
		} catch (Exception e) {
			log.error(" Can't find enclosing OrgRepo for repositoryId tag ", e);
			throw new JspTagException("Error: Can't find enclosing OrgRepo for repositoryId tag ");
		}
	}

	public void setRepositoryId(int repositoryId) throws JspTagException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			theOrgRepo.setRepositoryId(repositoryId);
		} catch (Exception e) {
			log.error("Can't find enclosing OrgRepo for repositoryId tag ", e);
			throw new JspTagException("Error: Can't find enclosing OrgRepo for repositoryId tag ");
		}
	}

}
