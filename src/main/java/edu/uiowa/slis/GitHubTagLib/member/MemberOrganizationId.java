package edu.uiowa.slis.GitHubTagLib.member;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class MemberOrganizationId extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(MemberOrganizationId.class);


	public int doStartTag() throws JspException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			if (!theMember.commitNeeded) {
				pageContext.getOut().print(theMember.getOrganizationId());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Member for organizationId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Member for organizationId tag ");
		}
		return SKIP_BODY;
	}

	public int getOrganizationId() throws JspTagException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			return theMember.getOrganizationId();
		} catch (Exception e) {
			log.error(" Can't find enclosing Member for organizationId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Member for organizationId tag ");
		}
	}

	public void setOrganizationId(int organizationId) throws JspTagException {
		try {
			Member theMember = (Member)findAncestorWithClass(this, Member.class);
			theMember.setOrganizationId(organizationId);
		} catch (Exception e) {
			log.error("Can't find enclosing Member for organizationId tag ", e);
			throw new JspTagException("Error: Can't find enclosing Member for organizationId tag ");
		}
	}

}
