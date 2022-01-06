package edu.uiowa.slis.GitHubTagLib.searchOrganization;


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
import edu.uiowa.slis.GitHubTagLib.organization.Organization;

@SuppressWarnings("serial")
public class SearchOrganizationIterator extends GitHubTagLibBodyTagSupport {
    int sid = 0;
    int orgid = 0;
    int rank = 0;
    boolean relevant = false;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(SearchOrganizationIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useSearchTerm = false;
   boolean useOrganization = false;

	public static String searchOrganizationCountBySearchTerm(String ID) throws JspTagException {
		int count = 0;
		SearchOrganizationIterator theIterator = new SearchOrganizationIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_organization where 1=1"
						+ " and sid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchOrganization iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchOrganization iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean searchTermHasSearchOrganization(String ID) throws JspTagException {
		return ! searchOrganizationCountBySearchTerm(ID).equals("0");
	}

	public static String searchOrganizationCountByOrganization(String ID) throws JspTagException {
		int count = 0;
		SearchOrganizationIterator theIterator = new SearchOrganizationIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_organization where 1=1"
						+ " and orgid = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchOrganization iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchOrganization iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean organizationHasSearchOrganization(String ID) throws JspTagException {
		return ! searchOrganizationCountByOrganization(ID).equals("0");
	}

	public static Boolean searchOrganizationExists (String sid, String orgid) throws JspTagException {
		int count = 0;
		SearchOrganizationIterator theIterator = new SearchOrganizationIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_organization where 1=1"
						+ " and sid = ?"
						+ " and orgid = ?"
						);

			stat.setInt(1,Integer.parseInt(sid));
			stat.setInt(2,Integer.parseInt(orgid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchOrganization iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchOrganization iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean searchTermOrganizationExists (String ID) throws JspTagException {
		int count = 0;
		SearchOrganizationIterator theIterator = new SearchOrganizationIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.search_organization where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating SearchOrganization iterator", e);
			throw new JspTagException("Error: JDBC error generating SearchOrganization iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		SearchTerm theSearchTerm = (SearchTerm)findAncestorWithClass(this, SearchTerm.class);
		if (theSearchTerm!= null)
			parentEntities.addElement(theSearchTerm);
		Organization theOrganization = (Organization)findAncestorWithClass(this, Organization.class);
		if (theOrganization!= null)
			parentEntities.addElement(theOrganization);

		if (theSearchTerm == null) {
		} else {
			sid = theSearchTerm.getID();
		}
		if (theOrganization == null) {
		} else {
			orgid = theOrganization.getID();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (sid == 0 ? "" : " and sid = ?")
                                                        + (orgid == 0 ? "" : " and orgid = ?")
                                                        + generateLimitCriteria());
            if (sid != 0) stat.setInt(webapp_keySeq++, sid);
            if (orgid != 0) stat.setInt(webapp_keySeq++, orgid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT github.search_organization.sid, github.search_organization.orgid from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (sid == 0 ? "" : " and sid = ?")
                                                        + (orgid == 0 ? "" : " and orgid = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (sid != 0) stat.setInt(webapp_keySeq++, sid);
            if (orgid != 0) stat.setInt(webapp_keySeq++, orgid);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                sid = rs.getInt(1);
                orgid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating SearchOrganization iterator: " + stat, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating SearchOrganization iterator: " + stat);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating SearchOrganization iterator: " + stat,e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("github.search_organization");
       if (useSearchTerm)
          theBuffer.append(", github.search_term");
       if (useOrganization)
          theBuffer.append(", github.organization");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useSearchTerm)
          theBuffer.append(" and github.search_term.id = github.search_organization.sid");
       if (useOrganization)
          theBuffer.append(" and github.organization.id = github.search_organization.orgid");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "sid,orgid";
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
                orgid = rs.getInt(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across SearchOrganization", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across SearchOrganization" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across SearchOrganization",e);
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
            log.error("JDBC error ending SearchOrganization iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving sid " + sid);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending SearchOrganization iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        sid = 0;
        orgid = 0;
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

   public boolean getUseOrganization() {
        return useOrganization;
    }

    public void setUseOrganization(boolean useOrganization) {
        this.useOrganization = useOrganization;
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

	public int getOrgid () {
		return orgid;
	}

	public void setOrgid (int orgid) {
		this.orgid = orgid;
	}

	public int getActualOrgid () {
		return orgid;
	}
}
