package edu.uiowa.slis.GitHubTagLib.searchOrganization;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.GitHubTagLib.searchTerm.SearchTerm;
import edu.uiowa.slis.GitHubTagLib.organization.Organization;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class SearchOrganization extends GitHubTagLibTagSupport {

	static SearchOrganization currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(SearchOrganization.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int sid = 0;
	int orgid = 0;
	int rank = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
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

			SearchOrganizationIterator theSearchOrganizationIterator = (SearchOrganizationIterator)findAncestorWithClass(this, SearchOrganizationIterator.class);

			if (theSearchOrganizationIterator != null) {
				sid = theSearchOrganizationIterator.getSid();
				orgid = theSearchOrganizationIterator.getOrgid();
			}

			if (theSearchOrganizationIterator == null && theSearchTerm == null && theOrganization == null && sid == 0) {
				// no sid was provided - the default is to assume that it is a new SearchOrganization and to generate a new sid
				sid = Sequence.generateID();
				insertEntity();
			} else if (theSearchOrganizationIterator == null && theSearchTerm != null && theOrganization == null) {
				// an sid was provided as an attribute - we need to load a SearchOrganization from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sid,orgid,rank from github.search_organization where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sid == 0)
						sid = rs.getInt(1);
					if (orgid == 0)
						orgid = rs.getInt(2);
					if (rank == 0)
						rank = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theSearchOrganizationIterator == null && theSearchTerm == null && theOrganization != null) {
				// an sid was provided as an attribute - we need to load a SearchOrganization from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sid,orgid,rank from github.search_organization where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sid == 0)
						sid = rs.getInt(1);
					if (orgid == 0)
						orgid = rs.getInt(2);
					if (rank == 0)
						rank = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or sid was provided as an attribute - we need to load a SearchOrganization from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select rank from github.search_organization where sid = ? and orgid = ?");
				stmt.setInt(1,sid);
				stmt.setInt(2,orgid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (rank == 0)
						rank = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving sid " + sid, e);
			throw new JspTagException("Error: JDBC error retrieving sid " + sid);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update github.search_organization set rank = ? where sid = ? and orgid = ?");
				stmt.setInt(1,rank);
				stmt.setInt(2,sid);
				stmt.setInt(3,orgid);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			if (sid == 0) {
				sid = Sequence.generateID();
				log.debug("generating new SearchOrganization " + sid);
			}

			if (orgid == 0) {
				orgid = Sequence.generateID();
				log.debug("generating new SearchOrganization " + orgid);
			}

			PreparedStatement stmt = getConnection().prepareStatement("insert into github.search_organization(sid,orgid,rank) values (?,?,?)");
			stmt.setInt(1,sid);
			stmt.setInt(2,orgid);
			stmt.setInt(3,rank);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	public int getRank () {
		return rank;
	}

	public void setRank (int rank) {
		this.rank = rank;
		commitNeeded = true;
	}

	public int getActualRank () {
		return rank;
	}

	public static Integer sidValue() throws JspException {
		try {
			return currentInstance.getSid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sidValue()");
		}
	}

	public static Integer orgidValue() throws JspException {
		try {
			return currentInstance.getOrgid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function orgidValue()");
		}
	}

	public static Integer rankValue() throws JspException {
		try {
			return currentInstance.getRank();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function rankValue()");
		}
	}

	private void clearServiceState () {
		sid = 0;
		orgid = 0;
		rank = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();

	}

}
