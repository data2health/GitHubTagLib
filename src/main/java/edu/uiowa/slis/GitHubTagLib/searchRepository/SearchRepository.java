package edu.uiowa.slis.GitHubTagLib.searchRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.GitHubTagLib.searchTerm.SearchTerm;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class SearchRepository extends GitHubTagLibTagSupport {

	static SearchRepository currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(SearchRepository.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int sid = 0;
	int rid = 0;
	int rank = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
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

			SearchRepositoryIterator theSearchRepositoryIterator = (SearchRepositoryIterator)findAncestorWithClass(this, SearchRepositoryIterator.class);

			if (theSearchRepositoryIterator != null) {
				sid = theSearchRepositoryIterator.getSid();
				rid = theSearchRepositoryIterator.getRid();
			}

			if (theSearchRepositoryIterator == null && theSearchTerm == null && theRepository == null && sid == 0) {
				// no sid was provided - the default is to assume that it is a new SearchRepository and to generate a new sid
				sid = Sequence.generateID();
				insertEntity();
			} else if (theSearchRepositoryIterator == null && theSearchTerm != null && theRepository == null) {
				// an sid was provided as an attribute - we need to load a SearchRepository from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sid,rid,rank from github.search_repository where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sid == 0)
						sid = rs.getInt(1);
					if (rid == 0)
						rid = rs.getInt(2);
					if (rank == 0)
						rank = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theSearchRepositoryIterator == null && theSearchTerm == null && theRepository != null) {
				// an sid was provided as an attribute - we need to load a SearchRepository from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sid,rid,rank from github.search_repository where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sid == 0)
						sid = rs.getInt(1);
					if (rid == 0)
						rid = rs.getInt(2);
					if (rank == 0)
						rank = rs.getInt(3);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or sid was provided as an attribute - we need to load a SearchRepository from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select rank from github.search_repository where sid = ? and rid = ?");
				stmt.setInt(1,sid);
				stmt.setInt(2,rid);
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.search_repository set rank = ? where sid = ? and rid = ?");
				stmt.setInt(1,rank);
				stmt.setInt(2,sid);
				stmt.setInt(3,rid);
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
				log.debug("generating new SearchRepository " + sid);
			}

			if (rid == 0) {
				rid = Sequence.generateID();
				log.debug("generating new SearchRepository " + rid);
			}

			PreparedStatement stmt = getConnection().prepareStatement("insert into github.search_repository(sid,rid,rank) values (?,?,?)");
			stmt.setInt(1,sid);
			stmt.setInt(2,rid);
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

	public int getRid () {
		return rid;
	}

	public void setRid (int rid) {
		this.rid = rid;
	}

	public int getActualRid () {
		return rid;
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

	public static Integer ridValue() throws JspException {
		try {
			return currentInstance.getRid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function ridValue()");
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
		rid = 0;
		rank = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();

	}

}
