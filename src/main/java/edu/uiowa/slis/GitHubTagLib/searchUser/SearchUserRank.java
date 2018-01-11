package edu.uiowa.slis.GitHubTagLib.searchUser;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchUserRank extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchUserRank.class);


	public int doStartTag() throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			if (!theSearchUser.commitNeeded) {
				pageContext.getOut().print(theSearchUser.getRank());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for rank tag ");
		}
		return SKIP_BODY;
	}

	public int getRank() throws JspTagException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			return theSearchUser.getRank();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchUser for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for rank tag ");
		}
	}

	public void setRank(int rank) throws JspTagException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			theSearchUser.setRank(rank);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchUser for rank tag ");
		}
	}

}
