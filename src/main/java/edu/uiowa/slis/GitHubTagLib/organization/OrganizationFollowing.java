package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationFollowing extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OrganizationFollowing.class);

	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getFollowing());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for following tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for following tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getFollowing() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getFollowing();
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for following tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for following tag ");
			}
		}
	}

	public void setFollowing(int following) throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setFollowing(following);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for following tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for following tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for following tag ");
			}
		}
	}

}
