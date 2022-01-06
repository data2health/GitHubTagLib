package edu.uiowa.slis.GitHubTagLib.searchOrganization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchOrganizationOrgid extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SearchOrganizationOrgid.class);

	public int doStartTag() throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			if (!theSearchOrganization.commitNeeded) {
				pageContext.getOut().print(theSearchOrganization.getOrgid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for orgid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchOrganization for orgid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchOrganization for orgid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getOrgid() throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			return theSearchOrganization.getOrgid();
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for orgid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchOrganization for orgid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchOrganization for orgid tag ");
			}
		}
	}

	public void setOrgid(int orgid) throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			theSearchOrganization.setOrgid(orgid);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for orgid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchOrganization for orgid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchOrganization for orgid tag ");
			}
		}
	}

}
