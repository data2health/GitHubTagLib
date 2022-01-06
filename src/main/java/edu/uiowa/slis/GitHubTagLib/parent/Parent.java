package edu.uiowa.slis.GitHubTagLib.parent;

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
public class Parent extends GitHubTagLibTagSupport {

	static Parent currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Parent.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int ID = 0;
	int parentId = 0;
	String parentFullName = null;

	private String var = null;

	private Parent cachedParent = null;

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

			ParentIterator theParentIterator = (ParentIterator)findAncestorWithClass(this, ParentIterator.class);

			if (theParentIterator != null) {
				ID = theParentIterator.getID();
			}

			if (theParentIterator == null && theRepository == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Parent and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Parent from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select parent_id,parent_full_name from github.parent where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (parentId == 0)
						parentId = rs.getInt(1);
					if (parentFullName == null)
						parentFullName = rs.getString(2);
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
			Parent currentParent = (Parent) pageContext.getAttribute("tag_parent");
			if(currentParent != null){
				cachedParent = currentParent;
			}
			currentParent = this;
			pageContext.setAttribute((var == null ? "tag_parent" : var), currentParent);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedParent != null){
				pageContext.setAttribute((var == null ? "tag_parent" : var), this.cachedParent);
			}else{
				pageContext.removeAttribute((var == null ? "tag_parent" : var));
				this.cachedParent = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.parent set parent_id = ?, parent_full_name = ? where id = ? ");
				stmt.setInt( 1, parentId );
				stmt.setString( 2, parentFullName );
				stmt.setInt(3,ID);
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
		if (parentFullName == null){
			parentFullName = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into github.parent(id,parent_id,parent_full_name) values (?,?,?)");
		stmt.setInt(1,ID);
		stmt.setInt(2,parentId);
		stmt.setString(3,parentFullName);
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

	public int getParentId () {
		return parentId;
	}

	public void setParentId (int parentId) {
		this.parentId = parentId;
		commitNeeded = true;
	}

	public int getActualParentId () {
		return parentId;
	}

	public String getParentFullName () {
		if (commitNeeded)
			return "";
		else
			return parentFullName;
	}

	public void setParentFullName (String parentFullName) {
		this.parentFullName = parentFullName;
		commitNeeded = true;
	}

	public String getActualParentFullName () {
		return parentFullName;
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

	public static Integer parentIdValue() throws JspException {
		try {
			return currentInstance.getParentId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function parentIdValue()");
		}
	}

	public static String parentFullNameValue() throws JspException {
		try {
			return currentInstance.getParentFullName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function parentFullNameValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		parentId = 0;
		parentFullName = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
