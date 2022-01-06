package edu.uiowa.slis.GitHubTagLib.member;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class MemberOrganizationId extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(MemberOrganizationId.class);

	public int doStartTag() throws JspException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			if (!theMember.commitNeeded) {
				pageContext.getOut().print(theMember.getOrganizationId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Member for organizationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Member for organizationId tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Member for organizationId tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getOrganizationId() throws JspException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			return theMember.getOrganizationId();
		} catch (Exception e) {
			log.error("Can't find enclosing Member for organizationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Member for organizationId tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Member for organizationId tag ");
			}
		}
	}

	public void setOrganizationId(int organizationId) throws JspException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			theMember.setOrganizationId(organizationId);
		} catch (Exception e) {
			log.error("Can't find enclosing Member for organizationId tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Member for organizationId tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Member for organizationId tag ");
			}
		}
	}

}
