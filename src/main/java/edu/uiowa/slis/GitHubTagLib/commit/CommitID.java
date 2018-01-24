package edu.uiowa.slis.GitHubTagLib.commit;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class CommitID extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(CommitID.class);


	public int doStartTag() throws JspException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			if (!theCommit.commitNeeded) {
				pageContext.getOut().print(theCommit.getID());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for ID tag ");
		}
		return SKIP_BODY;
	}

	public int getID() throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			return theCommit.getID();
		} catch (Exception e) {
			log.error(" Can't find enclosing Commit for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for ID tag ");
		}
	}

	public void setID(int ID) throws JspTagException {
		try {
			Commit theCommit = (Commit)findAncestorWithClass(this, Commit.class);
			theCommit.setID(ID);
		} catch (Exception e) {
			log.error("Can't find enclosing Commit for ID tag ", e);
			throw new JspTagException("Error: Can't find enclosing Commit for ID tag ");
		}
	}

}
