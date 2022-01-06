package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import java.sql.Timestamp;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationUpdatedAt extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(OrganizationUpdatedAt.class);

	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getUpdatedAt());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for updatedAt tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for updatedAt tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for updatedAt tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getUpdatedAt() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getUpdatedAt();
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for updatedAt tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for updatedAt tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for updatedAt tag ");
			}
		}
	}

	public void setUpdatedAt(Timestamp updatedAt) throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setUpdatedAt(updatedAt);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for updatedAt tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Organization for updatedAt tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Organization for updatedAt tag ");
			}
		}
	}

}
