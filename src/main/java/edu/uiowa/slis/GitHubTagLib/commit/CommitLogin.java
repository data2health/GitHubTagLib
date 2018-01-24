package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitLogin extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitLogin.class);


	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getLogin());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for login tag ");
		}
		return SKIP_BODY;
	}

	public String getLogin() throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getLogin();
		} catch (Exception e) {
			log.error(" Can't find enclosing Commit for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for login tag ");
		}
	}

	public void setLogin(String login) throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setLogin(login);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for login tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for login tag ");
		}
	}

}
