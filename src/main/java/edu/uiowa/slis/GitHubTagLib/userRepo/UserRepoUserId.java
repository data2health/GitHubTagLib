package edu.uiowa.slis.GitHubTagLib.userRepo;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserRepoUserId extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserRepoUserId.class);

	public int doStartTag() throws JspException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			if (!theUserRepo.commitNeeded) {
				pageContext.getOut().print(theUserRepo.getUserId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing UserRepo for userId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing UserRepo for userId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getUserId() throws JspException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			return theUserRepo.getUserId();
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing UserRepo for userId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing UserRepo for userId tag ");
			}
		}
	}

	public void setUserId(int userId) throws JspException {
		try {
			UserRepo theUserRepo = (UserRepo)findAncestorWithClass(this, UserRepo.class);
			theUserRepo.setUserId(userId);
		} catch (Exception e) {
			log.error("Can't find enclosing UserRepo for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing UserRepo for userId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing UserRepo for userId tag ");
			}
		}
	}

}
