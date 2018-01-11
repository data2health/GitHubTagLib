package edu.uiowa.slis.GitHubTagLib.member;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class MemberUserId extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(MemberUserId.class);


	public int doStartTag() throws JspException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			if (!theMember.commitNeeded) {
				pageContext.getOut().print(theMember.getUserId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Member for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Member for userId tag ");
		}
		return SKIP_BODY;
	}

	public int getUserId() throws JspTagException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			return theMember.getUserId();
		} catch (Exception e) {
			log.error(" Can't find enclosing Member for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Member for userId tag ");
		}
	}

	public void setUserId(int userId) throws JspTagException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			theMember.setUserId(userId);
		} catch (Exception e) {
			log.error("Can't find enclosing Member for userId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Member for userId tag ");
		}
	}

}
