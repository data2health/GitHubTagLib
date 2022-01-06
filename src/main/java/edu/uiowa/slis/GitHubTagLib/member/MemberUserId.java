package edu.uiowa.slis.GitHubTagLib.member;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class MemberUserId extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(MemberUserId.class);

	public int doStartTag() throws JspException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			if (!theMember.commitNeeded) {
				pageContext.getOut().print(theMember.getUserId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Member for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Member for userId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Member for userId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getUserId() throws JspException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			return theMember.getUserId();
		} catch (Exception e) {
			log.error("Can't find enclosing Member for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Member for userId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Member for userId tag ");
			}
		}
	}

	public void setUserId(int userId) throws JspException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			theMember.setUserId(userId);
		} catch (Exception e) {
			log.error("Can't find enclosing Member for userId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Member for userId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Member for userId tag ");
			}
		}
	}

}
