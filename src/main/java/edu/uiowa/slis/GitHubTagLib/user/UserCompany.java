package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserCompany extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserCompany.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getCompany());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for company tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for company tag ");
		}
		return SKIP_BODY;
	}

	public String getCompany() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getCompany();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for company tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for company tag ");
		}
	}

	public void setCompany(String company) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setCompany(company);
		} catch (Exception e) {
			log.error("Can't find enclosing User for company tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for company tag ");
		}
	}

}
