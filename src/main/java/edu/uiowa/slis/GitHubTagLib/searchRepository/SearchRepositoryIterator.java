package edu.uiowa.slis.GitHubTagLib.searchRepository;


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
import edu.uiowa.slis.GitHubTagLib.searchTerm.SearchTerm;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

@SuppressWarnings("serial")
public class SearchRepositoryIterator extends GitHubTagLibBodyTagSupport {
    int sid = 0;
    int rid = 0;
    int rank = 0;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Log log = LogFactory.getLog(SearchRepositoryIterator.class);


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
                                                        +  generateLimitCriteria());
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
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (sid != 0) stat.setInt(webapp_keySeq++, sid);
            if (rid != 0) stat.setInt(webapp_keySeq++, rid);
            rs = stat.executeQuery();

            if (rs.next()) {
                sid = rs.getInt(1);
                rid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating SearchRepository iterator: " + stat.toString(), e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating SearchRepository iterator: " + stat.toString());
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
          theBuffer.append(" and search_term.ID = search_repository.sid");
       if (useRepository)
          theBuffer.append(" and repository.ID = search_repository.rid");

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

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                sid = rs.getInt(1);
                rid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across SearchRepository", e);
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across SearchRepository");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            log.error("JDBC error ending SearchRepository iterator",e);
            throw new JspTagException("Error: JDBC error ending SearchRepository iterator");
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
