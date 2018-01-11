package edu.uiowa.slis.GitHubTagLib.organization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class OrganizationBlog extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(OrganizationBlog.class);


	public int doStartTag() throws JspException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			if (!theOrganization.commitNeeded) {
				pageContext.getOut().print(theOrganization.getBlog());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for blog tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for blog tag ");
		}
		return SKIP_BODY;
	}

	public String getBlog() throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			return theOrganization.getBlog();
		} catch (Exception e) {
			log.error(" Can't find enclosing Organization for blog tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for blog tag ");
		}
	}

	public void setBlog(String blog) throws JspTagException {
		try {
			Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
			theOrganization.setBlog(blog);
		} catch (Exception e) {
			log.error("Can't find enclosing Organization for blog tag ", e);
			throw new JspTagException("Error: Can't find enclosing Organization for blog tag ");
		}
	}

}
