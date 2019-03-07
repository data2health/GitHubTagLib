package edu.uiowa.slis.GitHubTagLib.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import org.apache.log4j.Logger;
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

    static Decoder decoder = Base64.getMimeDecoder();
    static Encoder encoder = Base64.getMimeEncoder();

    public static void main(String[] args) throws IOException {
	switch (1) {
	case 1:
	    String content = read("data2health", "scits-platform", "README.md");
	    logger.info("contents:\n" + content);
	    break;
	case 2:
	    update("data2health", "scits-platform", "README.md", "TO DO", "workin' it");
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
	logger.info("sha: " + response.getString("sha"));

	return new String(decoder.decode(response.getString("content").getBytes()));
    }
    
    static void update(String owner, String repo, String path, String pattern, String replacement) throws IOException {
	JSONObject response = readRaw(owner, repo, path);
	String sha = response.getString("sha");
	String content = new String(decoder.decode(response.getString("content").getBytes()));
	logger.info("sha: " + sha);
	String newContent = content.replaceAll(pattern, replacement);
	logger.info("newContent:\n" + newContent);
    }

}
