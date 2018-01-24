package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitName extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitName.class);


	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for name tag ");
		}
		return SKIP_BODY;
	}

	public String getName() throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getName();
		} catch (Exception e) {
			log.error(" Can't find enclosing Commit for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for name tag ");
		}
	}

	public void setName(String name) throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setName(name);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for name tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for name tag ");
		}
	}

}
