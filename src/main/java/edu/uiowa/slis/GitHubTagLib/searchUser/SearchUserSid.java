package edu.uiowa.slis.GitHubTagLib.searchUser;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchUserSid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchUserSid.class);


	public int doStartTag() throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			if (!theSearchUser.commitNeeded) {
				pageContext.getOut().print(theSearchUser.getSid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for sid tag ");
		}
		return SKIP_BODY;
	}

	public int getSid() throws JspTagException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			return theSearchUser.getSid();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchUser for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for sid tag ");
		}
	}

	public void setSid(int sid) throws JspTagException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			theSearchUser.setSid(sid);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for sid tag ");
		}
	}

}
