package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserID extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserID.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing User for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for ID tag ");
		}
	}

}
