package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationCreatedAtToNow extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OrganizationCreatedAtToNow.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setCreatedAtToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for createdAt tag ", e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Organization for createdAt tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for createdAt tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getCreatedAt() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getCreatedAt();
		} catch (Exception e) {

			log.error("Can't find enclosing Organization for createdAt tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Organization for createdAt tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for createdAt tag ");
			}

		}
	}

	public void setCreatedAt() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setCreatedAtToNow( );
		} catch (Exception e) {

			log.error("Can't find enclosing Organization for createdAt tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Organization for createdAt tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for createdAt tag ");
			}

		}
	}
}