package edu.uiowa.slis.GitHubTagLib.otherCommitter;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.GitHubTagLibBodyTagSupport;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

@SuppressWarnings("serial")
public class OtherCommitterIterator extends GitHubTagLibBodyTagSupport {
    int rid = 0;
    String email = null;
    String name = null;
    Timestamp mostRecent = null;
    int count = 0;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(OtherCommitterIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

	public static String otherCommitterCountByRepository(String ID) throws JspTagException {
		int count = 0;
		OtherCommitterIterator theIterator = new OtherCommitterIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.other_committer where 1=1"
						+ " and rid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating OtherCommitter iterator", e);
			throw new JspTagException("Error: JDBC error generating OtherCommitter iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean repositoryHasOtherCommitter(String ID) throws JspTagException {
		return ! otherCommitterCountByRepository(ID).equals("0");
	}

	public static Boolean otherCommitterExists (String rid, String email) throws JspTagException {
		int count = 0;
		OtherCommitterIterator theIterator = new OtherCommitterIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.other_committer where 1=1"
						+ " and rid = ?"
						+ " and email = ?"
						);

			stat.setInt(1,Integer.parseInt(rid));
			stat.setString(2,email);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating OtherCommitter iterator", e);
			throw new JspTagException("Error: JDBC error generating OtherCommitter iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
		if (theRepository!= null)
			parentEntities.addElement(theRepository);

		if (theRepository == null) {
		} else {
			rid = theRepository.getID();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (rid == 0 ? "" : " and rid = ?")
                                                        + generateLimitCriteria());
            if (rid != 0) stat.setInt(webapp_keySeq++, rid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT github.other_committer.rid, github.other_committer.email from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (rid == 0 ? "" : " and rid = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (rid != 0) stat.setInt(webapp_keySeq++, rid);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                rid = rs.getInt(1);
                email = rs.getString(2);
                if (var != null)
                    pageContext.setAttribute(var, this);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating OtherCommitter iterator: " + stat, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating OtherCommitter iterator: " + stat);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating OtherCommitter iterator: " + stat,e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("github.other_committer");
      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "rid,email";
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
                rid = rs.getInt(1);
                email = rs.getString(2);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across OtherCommitter", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across OtherCommitter" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across OtherCommitter",e);
			}

        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
			if( var != null )
				pageContext.removeAttribute(var);
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
            log.error("JDBC error ending OtherCommitter iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving rid " + rid);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending OtherCommitter iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        rid = 0;
        email = null;
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

    public Boolean isFirst() throws SQLException {
        return rs.isFirst();
    }

    public Boolean isLast() throws SQLException {
        return rs.isLast();
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

	public String getEmail () {
		return email;
	}

	public void setEmail (String email) {
		this.email = email;
	}

	public String getActualEmail () {
		return email;
	}
}
