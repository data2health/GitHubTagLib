package edu.uiowa.slis.GitHubTagLib.searchUser;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchUserRelevant extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SearchUserRelevant.class);

	public int doStartTag() throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			if (!theSearchUser.commitNeeded) {
				pageContext.getOut().print(theSearchUser.getRelevant());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for relevant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchUser for relevant tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchUser for relevant tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getRelevant() throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			return theSearchUser.getRelevant();
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for relevant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchUser for relevant tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchUser for relevant tag ");
			}
		}
	}

	public void setRelevant(boolean relevant) throws JspException {
		try {
			SearchUser theSearchUser = (SearchUser)findAncestorWithClass(this, SearchUser.class);
			theSearchUser.setRelevant(relevant);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchUser for relevant tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchUser for relevant tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchUser for relevant tag ");
			}
		}
	}

}
