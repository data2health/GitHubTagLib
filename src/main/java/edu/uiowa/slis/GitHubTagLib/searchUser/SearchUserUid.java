package edu.uiowa.slis.GitHubTagLib.searchUser;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchUserUid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchUserUid.class);


	public int doStartTag() throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			if (!theSearchUser.commitNeeded) {
				pageContext.getOut().print(theSearchUser.getUid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for uid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for uid tag ");
		}
		return SKIP_BODY;
	}

	public int getUid() throws JspTagException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			return theSearchUser.getUid();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchUser for uid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for uid tag ");
		}
	}

	public void setUid(int uid) throws JspTagException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			theSearchUser.setUid(uid);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for uid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for uid tag ");
		}
	}

}
