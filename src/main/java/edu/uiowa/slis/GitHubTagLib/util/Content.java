package edu.uiowa.slis.GitHubTagLib.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;
import org.json.JSONTokener;

// https://api.github.com/repos/data2health/scits-platform/contents/README.md

/*
{
  "name": "README.md",
  "path": "README.md",
  "sha": "4112ea2ced43115ab632ed8869588f8b9a02a433",
  "size": 3472,
  "url": "https://api.github.com/repos/data2health/scits-platform/contents/README.md?ref=master",
  "html_url": "https://github.com/data2health/scits-platform/blob/master/README.md",
  "git_url": "https://api.github.com/repos/data2health/scits-platform/git/blobs/4112ea2ced43115ab632ed8869588f8b9a02a433",
  "download_url": "https://raw.githubusercontent.com/data2health/scits-platform/master/README.md",
  "type": "file",
  "content": "IyBTY2llbmNlIG9mIFRyYW5zbGF0aW9uYWwgU2NpZW5jZ .... SBSZXNlYXJjaCBQ\nbGF0Zm9ybSAKC",
  "encoding": "base64",
  "_links": {
    "self": "https://api.github.com/repos/data2health/scits-platform/contents/README.md?ref=master",
    "git": "https://api.github.com/repos/data2health/scits-platform/git/blobs/4112ea2ced43115ab632ed8869588f8b9a02a433",
    "html": "https://github.com/data2health/scits-platform/blob/master/README.md"
  }
}
 */

public class Content {
    static Logger logger = Logger.getLogger(Content.class);
    protected static LocalProperties prop_file = null;

    static String client_id = "";
    static String client_secret = "";

    static Decoder decoder = Base64.getMimeDecoder();
    static Encoder encoder = Base64.getMimeEncoder(-1, "\n".getBytes());
    
    static {
	prop_file = PropertyLoader.loadProperties("github");
	client_id = prop_file.getProperty("client_id");
	client_secret = prop_file.getProperty("client_secret");	
    }

    public static void main(String[] args) throws IOException {
	PropertyConfigurator.configure(args[0]);
	switch (2) {
	case 1:
	    String content = read("data2health", "scits-platform", "README.md");
	    logger.info("contents:\n" + content);
	    break;
	case 2:
	    update("data2health", "scits-platform", "testing.md", "replacement", "workin' it", "trying an update");
	    break;
	case 3:
	    write("data2health", "scits-platform", "testing.md", "TO DO", "workin' it");
	    break;
	case 4:
	    replace("data2health", "scits-platform", "testing.md", "additional replacement content", "reworkin' it");
	    break;
	default:
	    break;
	}
	
    }

    static JSONObject readRaw(String owner, String repo, String path) throws IOException {
	URL theURL = new URL("https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path);
	// "&client_id=" + client_id + "&client_secret=" + client_secret);
	BufferedReader reader = new BufferedReader(new InputStreamReader(theURL.openConnection().getInputStream()));

	JSONObject results = new JSONObject(new JSONTokener(reader));
	
	return results;
    }

    static String read(String owner, String repo, String path) throws IOException {
	JSONObject response = readRaw(owner, repo, path);
	logger.debug("sha: " + response.getString("sha"));

	return new String(decoder.decode(response.getString("content").getBytes()));
    }
    
    static String getSha(String owner, String repo, String path) throws IOException {
	JSONObject response = readRaw(owner, repo, path);
	return response.getString("sha");
    }
    
    static void write(String owner, String repo, String path, String content, String message) throws IOException {
	write(owner, repo, path, content, message, false);
    }
    
    static void replace(String owner, String repo, String path, String content, String message) throws IOException {
	write(owner, repo, path, content, message, true);
    }
    
    static void write(String owner, String repo, String path, String content, String message, boolean rewrite) throws IOException {
	StringBuffer buffer = new StringBuffer();
	buffer.append("{\n");
	buffer.append("  \"message\": \"" + quotify(message) + "\",\n");
	if (rewrite)
	    buffer.append("  \"sha\": \"" + getSha(owner, repo, path) + "\",\n");
	buffer.append("  \"committer\": {\n");
	buffer.append("     \"name\": \"David Eichmann\",\n");
	buffer.append("     \"email\": \"david.eichmann@gmail.com\"\n");
	buffer.append("   },\n");
	buffer.append("  \"content\": \"" + encoder.encodeToString(content.getBytes()) + "\"\n");
	buffer.append("}\n");
	logger.debug("payload:\n" + buffer);
	
	submit("https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path, buffer.toString());
    }
    
    static void update(String owner, String repo, String path, String pattern, String replacement, String message) throws IOException {
	String content = read(owner, repo, path);
	logger.debug("originalContent: " + content);
	String newContent = content.replaceAll(pattern, replacement);
	logger.debug("newContent:\n" + newContent);
	replace(owner, repo, path, newContent, message);
    }

    static private JSONObject submit(String url, String request) throws IOException {
	// configure the connection
	URL uri = new URL(url);
	HttpURLConnection con = (HttpURLConnection) uri.openConnection();
	con.setRequestMethod("PUT"); // type: POST, PUT, DELETE, GET
	if (prop_file.getProperty("token") != null)
	    con.setRequestProperty("Authorization", "token " + prop_file.getProperty("token"));
	con.setRequestProperty("Accept","application/json");
	con.setDoOutput(true);
	con.setDoInput(true);
	
	// submit the GraphQL construct
	logger.debug("request: " + request);
	BufferedWriter out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
	out.write(request);
	out.flush();
	out.close();

	// pull down the response JSON
	con.connect();
	logger.info("response:" + con.getResponseCode());
	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	JSONObject results = new JSONObject(new JSONTokener(in));
	logger.debug("results:\n" + results.toString(3));
	in.close();
	return results;
    }

    static String quotify(String theString) {
	return theString.replace("\"", "\\\"");
    }
}
