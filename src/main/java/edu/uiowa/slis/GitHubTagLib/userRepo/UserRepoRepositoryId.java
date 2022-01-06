package edu.uiowa.slis.GitHubTagLib.userRepo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserRepoRepositoryId extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserRepoRepositoryId.class);

	public int doStartTag() throws JspException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			if (!theUserRepo.commitNeeded) {
				pageContext.getOut().print(theUserRepo.getRepositoryId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for repositoryId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing UserRepo for repositoryId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing UserRepo for repositoryId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getRepositoryId() throws JspException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			return theUserRepo.getRepositoryId();
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for repositoryId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing UserRepo for repositoryId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing UserRepo for repositoryId tag ");
			}
		}
	}

	public void setRepositoryId(int repositoryId) throws JspException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			theUserRepo.setRepositoryId(repositoryId);
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for repositoryId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing UserRepo for repositoryId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing UserRepo for repositoryId tag ");
			}
		}
	}

}
