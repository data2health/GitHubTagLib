package edu.uiowa.slis.GitHubTagLib.searchTerm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;


import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class SearchTerm extends GitHubTagLibTagSupport {

	static SearchTerm currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(SearchTerm.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int ID = 0;
	String term = null;

	private String var = null;

	private SearchTerm cachedSearchTerm = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			SearchTermIterator theSearchTermIterator = (SearchTermIterator)findAncestorWithClass(this, SearchTermIterator.class);

			if (theSearchTermIterator != null) {
				ID = theSearchTermIterator.getID();
			}

			if (theSearchTermIterator == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new SearchTerm and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a SearchTerm from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select term from github.search_term where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (term == null)
						term = rs.getString(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			log.error("JDBC error retrieving ID " + ID, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving ID " + ID);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving ID " + ID,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			SearchTerm currentSearchTerm = (SearchTerm) pageContext.getAttribute("tag_searchTerm");
			if(currentSearchTerm != null){
				cachedSearchTerm = currentSearchTerm;
			}
			currentSearchTerm = this;
			pageContext.setAttribute((var == null ? "tag_searchTerm" : var), currentSearchTerm);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedSearchTerm != null){
				pageContext.setAttribute((var == null ? "tag_searchTerm" : var), this.cachedSearchTerm);
			}else{
				pageContext.removeAttribute((var == null ? "tag_searchTerm" : var));
				this.cachedSearchTerm = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.search_term set term = ? where id = ? ");
				stmt.setString( 1, term );
				stmt.setInt(2,ID);
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
		if (ID == 0) {
			ID = Sequence.generateID();
			log.debug("generating new SearchTerm " + ID);
		}

		if (term == null){
			term = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into github.search_term(id,term) values (?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,term);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
	}

	public int getID () {
		return ID;
	}

	public void setID (int ID) {
		this.ID = ID;
	}

	public int getActualID () {
		return ID;
	}

	public String getTerm () {
		if (commitNeeded)
			return "";
		else
			return term;
	}

	public void setTerm (String term) {
		this.term = term;
		commitNeeded = true;
	}

	public String getActualTerm () {
		return term;
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

	public static Integer IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static String termValue() throws JspException {
		try {
			return currentInstance.getTerm();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function termValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		term = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
