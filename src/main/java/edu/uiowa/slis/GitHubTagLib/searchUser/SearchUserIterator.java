package edu.uiowa.slis.GitHubTagLib.searchUser;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.GitHubTagLibBodyTagSupport;
import edu.uiowa.slis.GitHubTagLib.searchTerm.SearchTerm;
import edu.uiowa.slis.GitHubTagLib.user.User;

@SuppressWarnings("serial")
public class SearchUserIterator extends GitHubTagLibBodyTagSupport {
    int sid = 0;
    int uid = 0;
    int rank = 0;
    boolean relevant = false;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(SearchUserIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useSearchTerm = false;
   boolean useUser = false;

	public static String searchUserCountBySearchTerm(String ID) throws JspTagException {
		int count = 0;
		SearchUserIterator theIterator = new SearchUserIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_user where 1=1"
						+ " and sid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchUser iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchUser iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean searchTermHasSearchUser(String ID) throws JspTagException {
		return ! searchUserCountBySearchTerm(ID).equals("0");
	}

	public static String searchUserCountByUser(String ID) throws JspTagException {
		int count = 0;
		SearchUserIterator theIterator = new SearchUserIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_user where 1=1"
						+ " and uid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchUser iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchUser iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean userHasSearchUser(String ID) throws JspTagException {
		return ! searchUserCountByUser(ID).equals("0");
	}

	public static Boolean searchUserExists (String sid, String uid) throws JspTagException {
		int count = 0;
		SearchUserIterator theIterator = new SearchUserIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_user where 1=1"
						+ " and sid = ?"
						+ " and uid = ?"
						);

			stat.setInt(1,Integer.parseInt(sid));
			stat.setInt(2,Integer.parseInt(uid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchUser iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchUser iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean searchTermUserExists (String ID) throws JspTagException {
		int count = 0;
		SearchUserIterator theIterator = new SearchUserIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_user where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchUser iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchUser iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

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


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (sid == 0 ? "" : " and sid = ?")
                                                        + (uid == 0 ? "" : " and uid = ?")
                                                        + generateLimitCriteria());
            if (sid != 0) stat.setInt(webapp_keySeq++, sid);
            if (uid != 0) stat.setInt(webapp_keySeq++, uid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT github.search_user.sid, github.search_user.uid from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (sid == 0 ? "" : " and sid = ?")
                                                        + (uid == 0 ? "" : " and uid = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (sid != 0) stat.setInt(webapp_keySeq++, sid);
            if (uid != 0) stat.setInt(webapp_keySeq++, uid);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                sid = rs.getInt(1);
                uid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating SearchUser iterator: " + stat, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating SearchUser iterator: " + stat);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating SearchUser iterator: " + stat,e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("github.search_user");
       if (useSearchTerm)
          theBuffer.append(", github.search_term");
       if (useUser)
          theBuffer.append(", github.user");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useSearchTerm)
          theBuffer.append(" and github.search_term.id = github.search_user.sid");
       if (useUser)
          theBuffer.append(" and github.user.id = github.search_user.uid");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "sid,uid";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspException {
        try {
            if ( rs != null && rs.next() ) {
                sid = rs.getInt(1);
                uid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across SearchUser", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across SearchUser" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across SearchUser",e);
			}

        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
			if( pageContext != null ){
				Boolean error = (Boolean) pageContext.getAttribute("tagError");
				if( error != null && error ){

					freeConnection();
					clearServiceState();

					Exception e = null; // (Exception) pageContext.getAttribute("tagErrorException");
					String message = null; // (String) pageContext.getAttribute("tagErrorMessage");

					if(pageContext != null){
						e = (Exception) pageContext.getAttribute("tagErrorException");
						message = (String) pageContext.getAttribute("tagErrorMessage");

					}
					Tag parent = getParent();
					if(parent != null){
						return parent.doEndTag();
					}else if(e != null && message != null){
						throw new JspException(message,e);
					}else if(parent == null && pageContext != null){
						pageContext.removeAttribute("tagError");
						pageContext.removeAttribute("tagErrorException");
						pageContext.removeAttribute("tagErrorMessage");
					}
				}
			}

            if( rs != null ){
                rs.close();
            }

            if( stat != null ){
                stat.close();
            }

        } catch ( SQLException e ) {
            log.error("JDBC error ending SearchUser iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving sid " + sid);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending SearchUser iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        sid = 0;
        uid = 0;
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


   public boolean getUseSearchTerm() {
        return useSearchTerm;
    }

    public void setUseSearchTerm(boolean useSearchTerm) {
        this.useSearchTerm = useSearchTerm;
    }

   public boolean getUseUser() {
        return useUser;
    }

    public void setUseUser(boolean useUser) {
        this.useUser = useUser;
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
