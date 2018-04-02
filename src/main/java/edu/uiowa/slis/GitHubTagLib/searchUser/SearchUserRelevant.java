package edu.uiowa.slis.GitHubTagLib.searchUser;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchUserRelevant extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchUserRelevant.class);


	public int doStartTag() throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			if (!theSearchUser.commitNeeded) {
				pageContext.getOut().print(theSearchUser.getRelevant());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for relevant tag ");
		}
		return SKIP_BODY;
	}

	public boolean getRelevant() throws JspTagException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			return theSearchUser.getRelevant();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchUser for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for relevant tag ");
		}
	}

	public void setRelevant(boolean relevant) throws JspTagException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			theSearchUser.setRelevant(relevant);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for relevant tag ");
		}
	}

}
