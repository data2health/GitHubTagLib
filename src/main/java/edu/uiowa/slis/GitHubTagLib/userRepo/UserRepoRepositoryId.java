package edu.uiowa.slis.GitHubTagLib.userRepo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserRepoRepositoryId extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserRepoRepositoryId.class);


	public int doStartTag() throws JspException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			if (!theUserRepo.commitNeeded) {
				pageContext.getOut().print(theUserRepo.getRepositoryId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for repositoryId tag ", e);
			throw new JspTagException("Error: Can't find enclosing UserRepo for repositoryId tag ");
		}
		return SKIP_BODY;
	}

	public int getRepositoryId() throws JspTagException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			return theUserRepo.getRepositoryId();
		} catch (Exception e) {
			log.error(" Can't find enclosing UserRepo for repositoryId tag ", e);
			throw new JspTagException("Error: Can't find enclosing UserRepo for repositoryId tag ");
		}
	}

	public void setRepositoryId(int repositoryId) throws JspTagException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			theUserRepo.setRepositoryId(repositoryId);
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for repositoryId tag ", e);
			throw new JspTagException("Error: Can't find enclosing UserRepo for repositoryId tag ");
		}
	}

}
