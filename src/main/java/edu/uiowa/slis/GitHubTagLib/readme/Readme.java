package edu.uiowa.slis.GitHubTagLib.readme;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class Readme extends GitHubTagLibTagSupport {

	static Readme currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Readme.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int ID = 0;
	String readme = null;

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
			throw new JspTagException("Error: JDBC error retrieving ID " + ID);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update github.readme set readme = ? where id = ?");
				stmt.setString(1,readme);
				stmt.setInt(2,ID);
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
			if (readme == null)
				readme = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into github.readme(id,readme) values (?,?)");
			stmt.setInt(1,ID);
			stmt.setString(2,readme);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	}

}
