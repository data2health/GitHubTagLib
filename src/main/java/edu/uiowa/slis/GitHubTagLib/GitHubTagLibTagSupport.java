package edu.uiowa.slis.GitHubTagLib;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@SuppressWarnings("serial")
public class GitHubTagLibTagSupport extends TagSupport {

    protected DataSource theDataSource = null;
    protected Connection theConnection = null;
    private static final Logger log = LogManager.getLogger(GitHubTagLibTagSupport.class);

    public GitHubTagLibTagSupport() {
        super();
    }

    @Override
    public int doEndTag() throws JspException {
		freeConnection();
    	return super.doEndTag();
    }

    public DataSource getDataSource() {
        if (theDataSource == null) try {
            theDataSource = (DataSource)new InitialContext().lookup("java:/comp/env/jdbc/GitHubTagLib");
        } catch (Exception e) {
            log.error("Error in database initialization: " + e);
        }

        return theDataSource;
    }
    
    public Connection getConnection() throws SQLException {
        if (theConnection == null)
        	theConnection = getDataSource().getConnection();
        return theConnection;
    }

    public void freeConnection() throws JspTagException {
     try {
        if (theConnection != null)
         theConnection.close();
        theConnection = null;
     } catch (SQLException e) {
         log.error("JDBC error freeing connection", e);
        theConnection = null;
         throw new JspTagException("Error: JDBC error freeing connection");
     }
    }

}
