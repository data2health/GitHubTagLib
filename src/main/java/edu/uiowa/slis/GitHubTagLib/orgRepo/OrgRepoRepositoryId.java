package edu.uiowa.slis.GitHubTagLib.orgRepo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrgRepoRepositoryId extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OrgRepoRepositoryId.class);

	public int doStartTag() throws JspException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			if (!theOrgRepo.commitNeeded) {
				pageContext.getOut().print(theOrgRepo.getRepositoryId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing OrgRepo for repositoryId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OrgRepo for repositoryId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OrgRepo for repositoryId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getRepositoryId() throws JspException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			return theOrgRepo.getRepositoryId();
		} catch (Exception e) {
			log.error("Can't find enclosing OrgRepo for repositoryId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OrgRepo for repositoryId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing OrgRepo for repositoryId tag ");
			}
		}
	}

	public void setRepositoryId(int repositoryId) throws JspException {
		try {
			OrgRepo theOrgRepo = (OrgRepo)findAncestorWithClass(this, OrgRepo.class);
			theOrgRepo.setRepositoryId(repositoryId);
		} catch (Exception e) {
			log.error("Can't find enclosing OrgRepo for repositoryId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing OrgRepo for repositoryId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing OrgRepo for repositoryId tag ");
			}
		}
	}

}
