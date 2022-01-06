package edu.uiowa.slis.GitHubTagLib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;

@SuppressWarnings("serial")
public class Sequence extends GitHubTagLibTagSupport {
	public static enum Driver {POSTGRESQL, ORACLE, MYSQL, SQLSERVER, UNKNOWN};
	public static Driver theDriver = Driver.UNKNOWN;
	static String theDriverString = null;
	static Hashtable<String, Driver> theDriverHash = new Hashtable<String, Driver>();
	String var = null;

	static {
		theDriverHash.put("PostgreSQL", Driver.POSTGRESQL);
		theDriverHash.put("Microsoft SQL Server", Driver.SQLSERVER);
	}
	private static final Logger log = LogManager.getLogger(Sequence.class);

	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, generateID());
		return SKIP_BODY;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	static public int generateID() {
		int nextInt = 0;
		Connection conn = null;

		try {
			conn = (new Sequence()).getConnection();
			if (theDriver == Driver.UNKNOWN) {
				theDriverString = conn.getMetaData().getDatabaseProductName();
				log.debug("'" + theDriverString + "'");
				theDriver = theDriverHash.get(theDriverString);
			}

			switch (theDriver) {
				case POSTGRESQL:
					PreparedStatement stat = conn.prepareStatement("SELECT nextval ('github.seqnum')");

					ResultSet rs = stat.executeQuery();

					while (rs.next()) {
						nextInt = rs.getInt(1);
					}
					stat.close();
					break;
				case SQLSERVER:
					nextInt = 1;
					break;
				default:
					nextInt = 1;
			}
		} catch (Exception e) {
			log.error("SeqNum Exception", e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("Problem closing Connection",e);
			}
		}
		return nextInt;
	}
}