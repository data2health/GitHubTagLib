package edu.uiowa.slis.GitHubTagLib.userRepo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserRepoUserId extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserRepoUserId.class);


	public int doStartTag() throws JspException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			if (!theUserRepo.commitNeeded) {
				pageContext.getOut().print(theUserRepo.getUserId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing UserRepo for userId tag ");
		}
		return SKIP_BODY;
	}

	public int getUserId() throws JspTagException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			return theUserRepo.getUserId();
		} catch (Exception e) {
			log.error(" Can't find enclosing UserRepo for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing UserRepo for userId tag ");
		}
	}

	public void setUserId(int userId) throws JspTagException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			theUserRepo.setUserId(userId);
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing UserRepo for userId tag ");
		}
	}

}
