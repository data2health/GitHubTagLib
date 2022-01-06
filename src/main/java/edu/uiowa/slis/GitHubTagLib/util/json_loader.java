package edu.uiowa.slis.GitHubTagLib.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.uiowa.extraction.LocalProperties;
import edu.uiowa.extraction.PropertyLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class json_loader {
    static boolean load = true;
    static Logger logger = LogManager.getLogger(json_loader.class);

    public static final String networkHostName = "localhost";
    static Connection conn = null;

    static String databaseName = "";
    static String schemaName = "public";
    static String tableName = null;
    static String encoding = null;

    protected static Character separator = ',';
    protected static String fileSuffix = ".json";
    protected static LocalProperties prop_file = null;
    
    // this is in this project only as a temporary convenience

    public static void main(String args[]) throws Exception {
	prop_file = PropertyLoader.loadProperties("github");

	databaseName = args[1];
	schemaName = args[2];
	if (args.length > 4)
	    encoding = args[4];

	if (load) {
	    logger.info("connecting to database...");
	    Class.forName("org.postgresql.Driver");
	    Properties props = new Properties();
		props.setProperty("user", prop_file.getProperty("jdbc.user"));
		props.setProperty("password", prop_file.getProperty("jdbc.password"));
	    // props.setProperty("sslfactory",
	    // "org.postgresql.ssl.NonValidatingFactory");
	    // props.setProperty("ssl", "true");
	    conn = DriverManager.getConnection("jdbc:postgresql://" + networkHostName + "/" + databaseName, props);
	    conn.setAutoCommit(false);

	    simpleStmt("create schema " + schemaName);
	    simpleStmt("commit work");
	    simpleStmt("set search_path to " + schemaName);
	}

	processFile(args[3]);

	if (load)
	    simpleStmt("commit work");
    }

    static void processFile(String thePath) throws IOException, SQLException {
	logger.info("separator: '" + separator + "'\tfileSuffix: " + fileSuffix);
	File theFile = new File(thePath);

	if (theFile.isDirectory()) {
	    File[] theFiles = theFile.listFiles();
	    for (int i = 0; i < theFiles.length; i++)
		processFile(theFiles[i]);
	} else {
	    processFile(theFile);
	}
    }

    static void processFile(File theFile) throws IOException, SQLException {
	logger.info("file:" + theFile.getName());
	tableName = theFile.getName();
	if (tableName.endsWith("~"))
	    return;
	if (!tableName.endsWith(fileSuffix))
	    return;
	tableName = tableName.substring(0, tableName.length() - fileSuffix.length()).replace('.', '_').replace('-', '_');
	logger.info("table:" + tableName);
	simpleStmt("create table " + schemaName + "." + tableName + "(id serial, json jsonb)");
	BufferedReader reader = new BufferedReader(encoding == null ? new FileReader(theFile) : new InputStreamReader(new FileInputStream(theFile), encoding));

	reader.mark(1000);
	String firstLine = reader.readLine();
	reader.reset();
	
	if (firstLine.trim().startsWith("[")) {
	    PreparedStatement stmt = conn.prepareStatement("insert into " + schemaName + "." + tableName + "(json) values(?::jsonb)");
	    JSONArray array = new JSONArray(new JSONTokener(reader));
	    for (int i = 0; i < array.length(); i++) {
		JSONObject object = array.getJSONObject(i);
		stmt.setString(1, object.toString());
		stmt.execute();
	    }
	    stmt.close();
	} else {
	    JSONObject object = new JSONObject(new JSONTokener(reader));
	    PreparedStatement stmt = conn.prepareStatement("insert into " + schemaName + "." + tableName + "(json) values(?::jsonb)");
	    stmt.setString(1, object.toString());
	    stmt.execute();
		stmt.close();
	}
	conn.commit();

    }

    static void simpleStmt(String queryString) {
	try {
	    logger.info("executing " + queryString + "...");
	    PreparedStatement beginStmt = conn.prepareStatement(queryString);
	    beginStmt.executeUpdate();
	    beginStmt.close();
	} catch (Exception e) {
	    logger.error("Error in database initialization: ", e);
	}
    }
}
