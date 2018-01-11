package edu.uiowa.slis.GitHubTagLib.follows;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.GitHubTagLibBodyTagSupport;
import edu.uiowa.slis.GitHubTagLib.user.User;
import edu.uiowa.slis.GitHubTagLib.user.User;

@SuppressWarnings("serial")
public class FollowsDeleter extends GitHubTagLibBodyTagSupport {
    int follower = 0;
    int following = 0;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(FollowsDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		User theUser = (User)findAncestorWithClass(this, User.class);
		if (theUser!= null)
			parentEntities.addElement(theUser);
		User theUser2 = (User)findAncestorWithClass(this, User.class);
		if (theUser!= null)
			parentEntities.addElement(theUser);

		if (theUser == null) {
		} else {
			follower = theUser.getID();
		}
		if (theUser == null) {
		} else {
			following = theUser.getID();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from github.follows where 1=1"
                                                        + (follower == 0 ? "" : " and follower = ? ")
                                                        + (following == 0 ? "" : " and following = ? "));
            if (follower != 0) stat.setInt(webapp_keySeq++, follower);
            if (following != 0) stat.setInt(webapp_keySeq++, following);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Follows deleter", e);
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Follows deleter");
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
        follower = 0;
        following = 0;
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



	public int getFollower () {
		return follower;
	}

	public void setFollower (int follower) {
		this.follower = follower;
	}

	public int getActualFollower () {
		return follower;
	}

	public int getFollowing () {
		return following;
	}

	public void setFollowing (int following) {
		this.following = following;
	}

	public int getActualFollowing () {
		return following;
	}
}
