package edu.uiowa.slis.GitHubTagLib.searchUser;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.GitHubTagLibBodyTagSupport;
import edu.uiowa.slis.GitHubTagLib.searchTerm.SearchTerm;
import edu.uiowa.slis.GitHubTagLib.user.User;

@SuppressWarnings("serial")
public class SearchUserDeleter extends GitHubTagLibBodyTagSupport {
    int sid = 0;
    int uid = 0;
    int rank = 0;
    boolean relevant = false;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(SearchUserDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
		if (theSearchTerm!= null)
			parentEntities.addElement(theSearchTerm);
		User theUser = (User)findAncestorWithClass(this, User.class);
		if (theUser!= null)
			parentEntities.addElement(theUser);

		if (theSearchTerm == null) {
		} else {
			sid = theSearchTerm.getID();
		}
		if (theUser == null) {
		} else {
			uid = theUser.getID();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from github.search_user where 1=1"
                                                        + (sid == 0 ? "" : " and sid = ? ")
                                                        + (uid == 0 ? "" : " and uid = ? ")
                                                        + (sid == 0 ? "" : " and sid = ? ")
                                                        + (uid == 0 ? "" : " and uid = ? "));
            if (sid != 0) stat.setInt(webapp_keySeq++, sid);
            if (uid != 0) stat.setInt(webapp_keySeq++, uid);
			if (sid != 0) stat.setInt(webapp_keySeq++, sid);
			if (uid != 0) stat.setInt(webapp_keySeq++, uid);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating SearchUser deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating SearchUser deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating SearchUser deleter",e);
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
        sid = 0;
        uid = 0;
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



	public int getSid () {
		return sid;
	}

	public void setSid (int sid) {
		this.sid = sid;
	}

	public int getActualSid () {
		return sid;
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
}
