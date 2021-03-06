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
public class FollowsIterator extends GitHubTagLibBodyTagSupport {
    int follower = 0;
    int following = 0;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(FollowsIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useUser = false;
   boolean useUser = false;

	public static String followsCountByUser(String ID) throws JspTagException {
		int count = 0;
		FollowsIterator theIterator = new FollowsIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.follows where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Follows iterator", e);
			throw new JspTagException("Error: JDBC error generating Follows iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean userHasFollows(String ID) throws JspTagException {
		return ! followsCountByUser(ID).equals("0");
	}

	public static String followsCountByUser(String ID) throws JspTagException {
		int count = 0;
		FollowsIterator theIterator = new FollowsIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.follows where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Follows iterator", e);
			throw new JspTagException("Error: JDBC error generating Follows iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean userHasFollows(String ID) throws JspTagException {
		return ! followsCountByUser(ID).equals("0");
	}

	public static Boolean followsExists (String follower, String following) throws JspTagException {
		int count = 0;
		FollowsIterator theIterator = new FollowsIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.follows where 1=1"
						+ " and follower = ?"
						+ " and following = ?"
						);

			stat.setInt(1,Integer.parseInt(follower));
			stat.setInt(2,Integer.parseInt(following));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Follows iterator", e);
			throw new JspTagException("Error: JDBC error generating Follows iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean userUserExists (String ID) throws JspTagException {
		int count = 0;
		FollowsIterator theIterator = new FollowsIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.follows where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Follows iterator", e);
			throw new JspTagException("Error: JDBC error generating Follows iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		User theUser = (User)findAncestorWithClass(this, User.class);
		if (theUser!= null)
			parentEntities.addElement(theUser);
		User theUser = (User)findAncestorWithClass(this, User.class);
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


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (follower == 0 ? "" : " and follower = ?")
                                                        + (following == 0 ? "" : " and following = ?")
                                                        +  generateLimitCriteria());
            if (follower != 0) stat.setInt(webapp_keySeq++, follower);
            if (following != 0) stat.setInt(webapp_keySeq++, following);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT github.follows.follower, github.follows.following from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (follower == 0 ? "" : " and follower = ?")
                                                        + (following == 0 ? "" : " and following = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (follower != 0) stat.setInt(webapp_keySeq++, follower);
            if (following != 0) stat.setInt(webapp_keySeq++, following);
            rs = stat.executeQuery();

            if (rs.next()) {
                follower = rs.getInt(1);
                following = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating Follows iterator: " + stat.toString(), e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating Follows iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("github.follows");
       if (useUser)
          theBuffer.append(", github.user");
       if (useUser)
          theBuffer.append(", github.user");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useUser)
          theBuffer.append(" and user.ID = follows.null");
       if (useUser)
          theBuffer.append(" and user.ID = follows.null");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "follower,following";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                follower = rs.getInt(1);
                following = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across Follows", e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across Follows");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            log.error("JDBC error ending Follows iterator",e);
            throw new JspTagException("Error: JDBC error ending Follows iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        follower = 0;
        following = 0;
        parentEntities = new Vector<GitHubTagLibTagSupport>();

        this.rs = null;
        this.stat = null;
        this.sortCriteria = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public int getLimitCriteria() {
        return limitCriteria;
    }

    public void setLimitCriteria(int limitCriteria) {
        this.limitCriteria = limitCriteria;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }


   public boolean getUseUser() {
        return useUser;
    }

    public void setUseUser(boolean useUser) {
        this.useUser = useUser;
    }

   public boolean getUseUser() {
        return useUser;
    }

    public void setUseUser(boolean useUser) {
        this.useUser = useUser;
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
