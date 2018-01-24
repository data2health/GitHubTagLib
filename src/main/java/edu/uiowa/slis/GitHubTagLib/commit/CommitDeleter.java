package edu.uiowa.slis.GitHubTagLib.commit;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.GitHubTagLibBodyTagSupport;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

@SuppressWarnings("serial")
public class CommitDeleter extends GitHubTagLibBodyTagSupport {
    int ID = 0;
    Date committed = null;
    String name = null;
    String email = null;
    int userId = 0;
    String login = null;
    String message = null;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(CommitDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
		if (theRepository!= null)
			parentEntities.addElement(theRepository);

		if (theRepository == null) {
		} else {
			ID = theRepository.getID();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from github.commit where 1=1"
                                                        + (ID == 0 ? "" : " and id = ? ")
                                                        + (committed == null ? "" : " and committed = ? "));
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            if (committed != null) stat.setTimestamp(webapp_keySeq++, committed == null ? null : new java.sql.Timestamp(committed.getTime()));
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Commit deleter", e);
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Commit deleter");
        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {
		clearServiceState();
		return super.doEndTag();
	}

    private void clearServiceState() {
        ID = 0;
        committed = null;
        parentEntities = new Vector<GitHubTagLibTagSupport>();

        this.rs = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }



	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
	}

	public Date getCommitted () {
		return committed;
	}

	public void setCommitted (Date committed) {
		this.committed = committed;
	}

	public Date getActualCommitted () {
		return committed;
	}

	public void setCommittedToNow ( ) {
		this.committed = new java.util.Date();
	}
}
