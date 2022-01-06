package edu.uiowa.slis.GitHubTagLib.searchRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.searchTerm.SearchTerm;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class SearchRepository extends GitHubTagLibTagSupport {

	static SearchRepository currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(SearchRepository.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int sid = 0;
	int rid = 0;
	int rank = 0;
	boolean relevant = false;

	private String var = null;

	private SearchRepository cachedSearchRepository = null;

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
				PreparedStatement stmt = getConnection().prepareStatement("select sid,rid,rank,relevant from github.search_repository where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sid == 0)
						sid = rs.getInt(1);
					if (rid == 0)
						rid = rs.getInt(2);
					if (rank == 0)
						rank = rs.getInt(3);
					if (relevant == false)
						relevant = rs.getBoolean(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theSearchRepositoryIterator == null && theSearchTerm == null && theRepository != null) {
				// an sid was provided as an attribute - we need to load a SearchRepository from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sid,rid,rank,relevant from github.search_repository where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sid == 0)
						sid = rs.getInt(1);
					if (rid == 0)
						rid = rs.getInt(2);
					if (rank == 0)
						rank = rs.getInt(3);
					if (relevant == false)
						relevant = rs.getBoolean(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or sid was provided as an attribute - we need to load a SearchRepository from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select rank,relevant from github.search_repository where sid = ? and rid = ?");
				stmt.setInt(1,sid);
				stmt.setInt(2,rid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (rank == 0)
						rank = rs.getInt(1);
					if (relevant == false)
						relevant = rs.getBoolean(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving sid " + sid, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving sid " + sid);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving sid " + sid,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			SearchRepository currentSearchRepository = (SearchRepository) pageContext.getAttribute("tag_searchRepository");
			if(currentSearchRepository != null){
				cachedSearchRepository = currentSearchRepository;
			}
			currentSearchRepository = this;
			pageContext.setAttribute((var == null ? "tag_searchRepository" : var), currentSearchRepository);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedSearchRepository != null){
				pageContext.setAttribute((var == null ? "tag_searchRepository" : var), this.cachedSearchRepository);
			}else{
				pageContext.removeAttribute((var == null ? "tag_searchRepository" : var));
				this.cachedSearchRepository = null;
			}
		}

		try {
			Boolean error = null; // (Boolean) pageContext.getAttribute("tagError");
			if(pageContext != null){
				error = (Boolean) pageContext.getAttribute("tagError");
			}

			if(error != null && error){

				freeConnection();
				clearServiceState();

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
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update github.search_repository set rank = ?, relevant = ? where sid = ?  and rid = ? ");
				stmt.setInt( 1, rank );
				stmt.setBoolean( 2, relevant );
				stmt.setInt(3,sid);
				stmt.setInt(4,rid);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: IOException while writing to the user");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: IOException while writing to the user");
			}

		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException, SQLException {
		if (sid == 0) {
			sid = Sequence.generateID();
			log.debug("generating new SearchRepository " + sid);
		}

		if (rid == 0) {
			rid = Sequence.generateID();
			log.debug("generating new SearchRepository " + rid);
		}

		PreparedStatement stmt = getConnection().prepareStatement("insert into github.search_repository(sid,rid,rank,relevant) values (?,?,?,?)");
		stmt.setInt(1,sid);
		stmt.setInt(2,rid);
		stmt.setInt(3,rank);
		stmt.setBoolean(4,relevant);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
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

	public boolean getRelevant () {
		return relevant;
	}

	public void setRelevant (boolean relevant) {
		this.relevant = relevant;
		commitNeeded = true;
	}

	public boolean getActualRelevant () {
		return relevant;
	}

	public String getVar () {
		return var;
	}

	public void setVar (String var) {
		this.var = var;
	}

	public String getActualVar () {
		return var;
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

	public static Boolean relevantValue() throws JspException {
		try {
			return currentInstance.getRelevant();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function relevantValue()");
		}
	}

	private void clearServiceState () {
		sid = 0;
		rid = 0;
		rank = 0;
		relevant = false;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
