package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserCreatedAtToNow extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(UserCreatedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setCreatedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing User for createdAt tag ", e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for createdAt tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for createdAt tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getCreatedAt() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getCreatedAt();
		} catch (Exception e) {

			log.error("Can't find enclosing User for createdAt tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for createdAt tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing User for createdAt tag ");
			}

		}
	}

	public void setCreatedAt() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setCreatedAtToNow( );
		} catch (Exception e) {

			log.error("Can't find enclosing User for createdAt tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing User for createdAt tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing User for createdAt tag ");
			}

		}
	}
}