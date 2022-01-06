package edu.uiowa.slis.GitHubTagLib.searchUser;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchUserRank extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SearchUserRank.class);

	public int doStartTag() throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			if (!theSearchUser.commitNeeded) {
				pageContext.getOut().print(theSearchUser.getRank());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for rank tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchUser for rank tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchUser for rank tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getRank() throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			return theSearchUser.getRank();
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for rank tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchUser for rank tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchUser for rank tag ");
			}
		}
	}

	public void setRank(int rank) throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			theSearchUser.setRank(rank);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for rank tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchUser for rank tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchUser for rank tag ");
			}
		}
	}

}
