package edu.uiowa.slis.GitHubTagLib.searchRepository;


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
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

@SuppressWarnings("serial")
public class SearchRepositoryIterator extends GitHubTagLibBodyTagSupport {
    int sid = 0;
    int rid = 0;
    int rank = 0;
    boolean relevant = false;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(SearchRepositoryIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useSearchTerm = false;
   boolean useRepository = false;

	public static String searchRepositoryCountBySearchTerm(String ID) throws JspTagException {
		int count = 0;
		SearchRepositoryIterator theIterator = new SearchRepositoryIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_repository where 1=1"
						+ " and sid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchRepository iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchRepository iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean searchTermHasSearchRepository(String ID) throws JspTagException {
		return ! searchRepositoryCountBySearchTerm(ID).equals("0");
	}

	public static String searchRepositoryCountByRepository(String ID) throws JspTagException {
		int count = 0;
		SearchRepositoryIterator theIterator = new SearchRepositoryIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_repository where 1=1"
						+ " and rid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchRepository iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchRepository iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean repositoryHasSearchRepository(String ID) throws JspTagException {
		return ! searchRepositoryCountByRepository(ID).equals("0");
	}

	public static Boolean searchRepositoryExists (String sid, String rid) throws JspTagException {
		int count = 0;
		SearchRepositoryIterator theIterator = new SearchRepositoryIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_repository where 1=1"
						+ " and sid = ?"
						+ " and rid = ?"
						);

			stat.setInt(1,Integer.parseInt(sid));
			stat.setInt(2,Integer.parseInt(rid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchRepository iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchRepository iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean searchTermRepositoryExists (String ID) throws JspTagException {
		int count = 0;
		SearchRepositoryIterator theIterator = new SearchRepositoryIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_repository where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchRepository iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchRepository iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
		if (theSearchTerm!= null)
			parentEntities.addElement(theSearchTerm);
		Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
		if (theRepository!= null)
			parentEntities.addElement(theRepository);

		if (theSearchTerm == null) {
		} else {
			sid = theSearchTerm.getID();
		}
		if (theRepository == null) {
		} else {
			rid = theRepository.getID();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (sid == 0 ? "" : " and sid = ?")
                                                        + (rid == 0 ? "" : " and rid = ?")
                                                        + generateLimitCriteria());
            if (sid != 0) stat.setInt(webapp_keySeq++, sid);
            if (rid != 0) stat.setInt(webapp_keySeq++, rid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT github.search_repository.sid, github.search_repository.rid from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (sid == 0 ? "" : " and sid = ?")
                                                        + (rid == 0 ? "" : " and rid = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (sid != 0) stat.setInt(webapp_keySeq++, sid);
            if (rid != 0) stat.setInt(webapp_keySeq++, rid);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                sid = rs.getInt(1);
                rid = rs.getInt(2);
                if (var != null)
                    pageContext.setAttribute(var, this);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating SearchRepository iterator: " + stat, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating SearchRepository iterator: " + stat);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating SearchRepository iterator: " + stat,e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("github.search_repository");
       if (useSearchTerm)
          theBuffer.append(", github.search_term");
       if (useRepository)
          theBuffer.append(", github.repository");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useSearchTerm)
          theBuffer.append(" and github.search_term.id = github.search_repository.sid");
       if (useRepository)
          theBuffer.append(" and github.repository.id = github.search_repository.rid");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "sid,rid";
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
                rid = rs.getInt(2);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across SearchRepository", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across SearchRepository" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across SearchRepository",e);
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
            log.error("JDBC error ending SearchRepository iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving sid " + sid);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending SearchRepository iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        sid = 0;
        rid = 0;
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


   public boolean getUseSearchTerm() {
        return useSearchTerm;
    }

    public void setUseSearchTerm(boolean useSearchTerm) {
        this.useSearchTerm = useSearchTerm;
    }

   public boolean getUseRepository() {
        return useRepository;
    }

    public void setUseRepository(boolean useRepository) {
        this.useRepository = useRepository;
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
