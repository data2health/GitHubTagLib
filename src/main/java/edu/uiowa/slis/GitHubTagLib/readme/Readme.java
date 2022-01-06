package edu.uiowa.slis.GitHubTagLib.readme;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class Readme extends GitHubTagLibTagSupport {

	static Readme currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Readme.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int ID = 0;
	String readme = null;

	private String var = null;

	private Readme cachedReadme = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
			if (theRepository!= null)
				parentEntities.addElement(theRepository);

			if (theRepository == null) {
			} else {
				ID = theRepository.getID();
			}

			ReadmeIterator theReadmeIterator = (ReadmeIterator)findAncestorWithClass(this, ReadmeIterator.class);

			if (theReadmeIterator != null) {
				ID = theReadmeIterator.getID();
			}

			if (theReadmeIterator == null && theRepository == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Readme and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Readme from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select readme from github.readme where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (readme == null)
						readme = rs.getString(1);
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
			Readme currentReadme = (Readme) pageContext.getAttribute("tag_readme");
			if(currentReadme != null){
				cachedReadme = currentReadme;
			}
			currentReadme = this;
			pageContext.setAttribute((var == null ? "tag_readme" : var), currentReadme);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedReadme != null){
				pageContext.setAttribute((var == null ? "tag_readme" : var), this.cachedReadme);
			}else{
				pageContext.removeAttribute((var == null ? "tag_readme" : var));
				this.cachedReadme = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.readme set readme = ? where id = ? ");
				stmt.setString( 1, readme );
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
		if (readme == null){
			readme = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into github.readme(id,readme) values (?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,readme);
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

	public String getReadme () {
		if (commitNeeded)
			return "";
		else
			return readme;
	}

	public void setReadme (String readme) {
		this.readme = readme;
		commitNeeded = true;
	}

	public String getActualReadme () {
		return readme;
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

	public static String readmeValue() throws JspException {
		try {
			return currentInstance.getReadme();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function readmeValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		readme = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
