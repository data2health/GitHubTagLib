package edu.uiowa.slis.GitHubTagLib.searchOrganization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchOrganizationRank extends GitHubTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(SearchOrganizationRank.class);

	public int doStartTag() throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			if (!theSearchOrganization.commitNeeded) {
				pageContext.getOut().print(theSearchOrganization.getRank());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for rank tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchOrganization for rank tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchOrganization for rank tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getRank() throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			return theSearchOrganization.getRank();
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for rank tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchOrganization for rank tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchOrganization for rank tag ");
			}
		}
	}

	public void setRank(int rank) throws JspException {
		try {
			SearchOrganization theSearchOrganization = (SearchOrganization)findAncestorWithClass(this, SearchOrganization.class);
			theSearchOrganization.setRank(rank);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchOrganization for rank tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing SearchOrganization for rank tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing SearchOrganization for rank tag ");
			}
		}
	}

}
