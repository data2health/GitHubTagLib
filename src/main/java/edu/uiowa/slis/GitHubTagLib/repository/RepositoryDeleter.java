package edu.uiowa.slis.GitHubTagLib.repository;


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

@SuppressWarnings("serial")
public class RepositoryDeleter extends GitHubTagLibBodyTagSupport {
    int ID = 0;
    String name = null;
    String fullName = null;
    boolean isPrivate = false;
    String description = null;
    boolean fork = false;
    Date createdAt = null;
    Date updatedAt = null;
    Date pushedAt = null;
    String homepage = null;
    int size = 0;
    int stargazersCount = 0;
    int watchersCount = 0;
    String language = null;
    boolean hasIssues = false;
    boolean hasProjects = false;
    boolean hasDownloads = false;
    boolean hasWiki = false;
    boolean hasPages = false;
    int forksCount = 0;
    boolean archived = false;
    int openIssuesCount = 0;
    String license = null;
    int forks = 0;
    int openIssues = 0;
    int watchers = 0;
    String defaultBranch = null;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(RepositoryDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from github.repository where 1=1"
                                                        + (ID == 0 ? "" : " and id = ? "));
            if (ID != 0) stat.setInt(webapp_keySeq++, ID);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Repository deleter", e);
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Repository deleter");
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
}
