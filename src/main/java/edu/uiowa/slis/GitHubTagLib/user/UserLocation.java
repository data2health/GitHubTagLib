package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserLocation extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(UserLocation.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				pageContext.getOut().print(theUser.getLocation());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for location tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for location tag ");
		}
		return SKIP_BODY;
	}

	public String getLocation() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getLocation();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for location tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for location tag ");
		}
	}

	public void setLocation(String location) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setLocation(location);
		} catch (Exception e) {
			log.error("Can't find enclosing User for location tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for location tag ");
		}
	}

}
