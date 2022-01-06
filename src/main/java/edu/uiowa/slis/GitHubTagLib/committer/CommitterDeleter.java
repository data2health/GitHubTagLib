package edu.uiowa.slis.GitHubTagLib.committer;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.GitHubTagLibBodyTagSupport;
import edu.uiowa.slis.GitHubTagLib.user.User;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

@SuppressWarnings("serial")
public class CommitterDeleter extends GitHubTagLibBodyTagSupport {
    int uid = 0;
    int rid = 0;
    Timestamp mostRecent = null;
    int count = 0;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(CommitterDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		User theUser = (User)findAncestorWithClass(this, User.class);
		if (theUser!= null)
			parentEntities.addElement(theUser);
		Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
		if (theRepository!= null)
			parentEntities.addElement(theRepository);

		if (theUser == null) {
		} else {
			uid = theUser.getID();
		}
		if (theRepository == null) {
		} else {
			rid = theRepository.getID();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from github.committer where 1=1"
                                                        + (uid == 0 ? "" : " and uid = ? ")
                                                        + (rid == 0 ? "" : " and rid = ? ")
                                                        + (uid == 0 ? "" : " and uid = ? ")
                                                        + (rid == 0 ? "" : " and rid = ? "));
            if (uid != 0) stat.setInt(webapp_keySeq++, uid);
            if (rid != 0) stat.setInt(webapp_keySeq++, rid);
			if (uid != 0) stat.setInt(webapp_keySeq++, uid);
			if (rid != 0) stat.setInt(webapp_keySeq++, rid);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Committer deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Committer deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Committer deleter",e);
			}

        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {

		clearServiceState();
		Boolean error = (Boolean) pageContext.getAttribute("tagError");
		if(error != null && error){

			freeConnection();

			Exception e = (Exception) pageContext.getAttribute("tagErrorException");
			String message = (String) pageContext.getAttribute("tagErrorMessage");

			Tag parent = getParent();
			if(parent != null){
				return parent.doEndTag();
			}else if(e != null && message != null){
				throw new JspException(message,e);
			}else if(parent == null){
				pageContext.removeAttribute("tagError");
				pageContext.removeAttribute("tagErrorException");
				pageContext.removeAttribute("tagErrorMessage");
			}
		}
		return super.doEndTag();
	}

    private void clearServiceState() {
        uid = 0;
        rid = 0;
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



	public int getUid () {
		return uid;
	}

	public void setUid (int uid) {
		this.uid = uid;
	}

	public int getActualUid () {
		return uid;
	}

	public int getRid () {
		return rid;
	}

	public void setRid (int rid) {
		this.rid = rid;
	}

	public int getActualRid () {
		return rid;
	}
}
