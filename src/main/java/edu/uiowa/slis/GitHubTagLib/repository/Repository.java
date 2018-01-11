package edu.uiowa.slis.GitHubTagLib.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class Repository extends GitHubTagLibTagSupport {

	static Repository currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log = LogFactory.getLog(Repository.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int ID = 0;
	String name = null;
	String fullName = null;
	boolean isPrivate = false;
	String description = null;
	boolean fork = false;
	Date createdAt = null;
	Date updatedAt = null;
	Date pushedAt = null;
	String homepage = null;
	int size = 0;
	int stargazersCount = 0;
	int watchersCount = 0;
	String language = null;
	boolean hasIssues = false;
	boolean hasProjects = false;
	boolean hasDownloads = false;
	boolean hasWiki = false;
	boolean hasPages = false;
	int forksCount = 0;
	boolean archived = false;
	int openIssuesCount = 0;
	String license = null;
	int forks = 0;
	int openIssues = 0;
	int watchers = 0;
	String defaultBranch = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			RepositoryIterator theRepositoryIterator = (RepositoryIterator)findAncestorWithClass(this, RepositoryIterator.class);

			if (theRepositoryIterator != null) {
				ID = theRepositoryIterator.getID();
			}

			if (theRepositoryIterator == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new Repository and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a Repository from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select name,full_name,is_private,description,fork,created_at,updated_at,pushed_at,homepage,size,stargazers_count,watchers_count,language,has_issues,has_projects,has_downloads,has_wiki,has_pages,forks_count,archived,open_issues_count,license,forks,open_issues,watchers,default_branch from github.repository where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (name == null)
						name = rs.getString(1);
					if (fullName == null)
						fullName = rs.getString(2);
					if (isPrivate == false)
						isPrivate = rs.getBoolean(3);
					if (description == null)
						description = rs.getString(4);
					if (fork == false)
						fork = rs.getBoolean(5);
					if (createdAt == null)
						createdAt = rs.getTimestamp(6);
					if (updatedAt == null)
						updatedAt = rs.getTimestamp(7);
					if (pushedAt == null)
						pushedAt = rs.getTimestamp(8);
					if (homepage == null)
						homepage = rs.getString(9);
					if (size == 0)
						size = rs.getInt(10);
					if (stargazersCount == 0)
						stargazersCount = rs.getInt(11);
					if (watchersCount == 0)
						watchersCount = rs.getInt(12);
					if (language == null)
						language = rs.getString(13);
					if (hasIssues == false)
						hasIssues = rs.getBoolean(14);
					if (hasProjects == false)
						hasProjects = rs.getBoolean(15);
					if (hasDownloads == false)
						hasDownloads = rs.getBoolean(16);
					if (hasWiki == false)
						hasWiki = rs.getBoolean(17);
					if (hasPages == false)
						hasPages = rs.getBoolean(18);
					if (forksCount == 0)
						forksCount = rs.getInt(19);
					if (archived == false)
						archived = rs.getBoolean(20);
					if (openIssuesCount == 0)
						openIssuesCount = rs.getInt(21);
					if (license == null)
						license = rs.getString(22);
					if (forks == 0)
						forks = rs.getInt(23);
					if (openIssues == 0)
						openIssues = rs.getInt(24);
					if (watchers == 0)
						watchers = rs.getInt(25);
					if (defaultBranch == null)
						defaultBranch = rs.getString(26);
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.repository set name = ?, full_name = ?, is_private = ?, description = ?, fork = ?, created_at = ?, updated_at = ?, pushed_at = ?, homepage = ?, size = ?, stargazers_count = ?, watchers_count = ?, language = ?, has_issues = ?, has_projects = ?, has_downloads = ?, has_wiki = ?, has_pages = ?, forks_count = ?, archived = ?, open_issues_count = ?, license = ?, forks = ?, open_issues = ?, watchers = ?, default_branch = ? where id = ?");
				stmt.setString(1,name);
				stmt.setString(2,fullName);
				stmt.setBoolean(3,isPrivate);
				stmt.setString(4,description);
				stmt.setBoolean(5,fork);
				stmt.setTimestamp(6,createdAt == null ? null : new java.sql.Timestamp(createdAt.getTime()));
				stmt.setTimestamp(7,updatedAt == null ? null : new java.sql.Timestamp(updatedAt.getTime()));
				stmt.setTimestamp(8,pushedAt == null ? null : new java.sql.Timestamp(pushedAt.getTime()));
				stmt.setString(9,homepage);
				stmt.setInt(10,size);
				stmt.setInt(11,stargazersCount);
				stmt.setInt(12,watchersCount);
				stmt.setString(13,language);
				stmt.setBoolean(14,hasIssues);
				stmt.setBoolean(15,hasProjects);
				stmt.setBoolean(16,hasDownloads);
				stmt.setBoolean(17,hasWiki);
				stmt.setBoolean(18,hasPages);
				stmt.setInt(19,forksCount);
				stmt.setBoolean(20,archived);
				stmt.setInt(21,openIssuesCount);
				stmt.setString(22,license);
				stmt.setInt(23,forks);
				stmt.setInt(24,openIssues);
				stmt.setInt(25,watchers);
				stmt.setString(26,defaultBranch);
				stmt.setInt(27,ID);
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
			if (ID == 0) {
				ID = Sequence.generateID();
				log.debug("generating new Repository " + ID);
			}

			if (name == null)
				name = "";
			if (fullName == null)
				fullName = "";
			if (description == null)
				description = "";
			if (homepage == null)
				homepage = "";
			if (language == null)
				language = "";
			if (license == null)
				license = "";
			if (defaultBranch == null)
				defaultBranch = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into github.repository(id,name,full_name,is_private,description,fork,created_at,updated_at,pushed_at,homepage,size,stargazers_count,watchers_count,language,has_issues,has_projects,has_downloads,has_wiki,has_pages,forks_count,archived,open_issues_count,license,forks,open_issues,watchers,default_branch) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setInt(1,ID);
			stmt.setString(2,name);
			stmt.setString(3,fullName);
			stmt.setBoolean(4,isPrivate);
			stmt.setString(5,description);
			stmt.setBoolean(6,fork);
			stmt.setTimestamp(7,createdAt == null ? null : new java.sql.Timestamp(createdAt.getTime()));
			stmt.setTimestamp(8,updatedAt == null ? null : new java.sql.Timestamp(updatedAt.getTime()));
			stmt.setTimestamp(9,pushedAt == null ? null : new java.sql.Timestamp(pushedAt.getTime()));
			stmt.setString(10,homepage);
			stmt.setInt(11,size);
			stmt.setInt(12,stargazersCount);
			stmt.setInt(13,watchersCount);
			stmt.setString(14,language);
			stmt.setBoolean(15,hasIssues);
			stmt.setBoolean(16,hasProjects);
			stmt.setBoolean(17,hasDownloads);
			stmt.setBoolean(18,hasWiki);
			stmt.setBoolean(19,hasPages);
			stmt.setInt(20,forksCount);
			stmt.setBoolean(21,archived);
			stmt.setInt(22,openIssuesCount);
			stmt.setString(23,license);
			stmt.setInt(24,forks);
			stmt.setInt(25,openIssues);
			stmt.setInt(26,watchers);
			stmt.setString(27,defaultBranch);
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

	public String getName () {
		if (commitNeeded)
			return "";
		else
			return name;
	}

	public void setName (String name) {
		this.name = name;
		commitNeeded = true;
	}

	public String getActualName () {
		return name;
	}

	public String getFullName () {
		if (commitNeeded)
			return "";
		else
			return fullName;
	}

	public void setFullName (String fullName) {
		this.fullName = fullName;
		commitNeeded = true;
	}

	public String getActualFullName () {
		return fullName;
	}

	public boolean getIsPrivate () {
		return isPrivate;
	}

	public void setIsPrivate (boolean isPrivate) {
		this.isPrivate = isPrivate;
		commitNeeded = true;
	}

	public boolean getActualIsPrivate () {
		return isPrivate;
	}

	public String getDescription () {
		if (commitNeeded)
			return "";
		else
			return description;
	}

	public void setDescription (String description) {
		this.description = description;
		commitNeeded = true;
	}

	public String getActualDescription () {
		return description;
	}

	public boolean getFork () {
		return fork;
	}

	public void setFork (boolean fork) {
		this.fork = fork;
		commitNeeded = true;
	}

	public boolean getActualFork () {
		return fork;
	}

	public Date getCreatedAt () {
		return createdAt;
	}

	public void setCreatedAt (Date createdAt) {
		this.createdAt = createdAt;
		commitNeeded = true;
	}

	public Date getActualCreatedAt () {
		return createdAt;
	}

	public void setCreatedAtToNow ( ) {
		this.createdAt = new java.util.Date();
		commitNeeded = true;
	}

	public Date getUpdatedAt () {
		return updatedAt;
	}

	public void setUpdatedAt (Date updatedAt) {
		this.updatedAt = updatedAt;
		commitNeeded = true;
	}

	public Date getActualUpdatedAt () {
		return updatedAt;
	}

	public void setUpdatedAtToNow ( ) {
		this.updatedAt = new java.util.Date();
		commitNeeded = true;
	}

	public Date getPushedAt () {
		return pushedAt;
	}

	public void setPushedAt (Date pushedAt) {
		this.pushedAt = pushedAt;
		commitNeeded = true;
	}

	public Date getActualPushedAt () {
		return pushedAt;
	}

	public void setPushedAtToNow ( ) {
		this.pushedAt = new java.util.Date();
		commitNeeded = true;
	}

	public String getHomepage () {
		if (commitNeeded)
			return "";
		else
			return homepage;
	}

	public void setHomepage (String homepage) {
		this.homepage = homepage;
		commitNeeded = true;
	}

	public String getActualHomepage () {
		return homepage;
	}

	public int getSize () {
		return size;
	}

	public void setSize (int size) {
		this.size = size;
		commitNeeded = true;
	}

	public int getActualSize () {
		return size;
	}

	public int getStargazersCount () {
		return stargazersCount;
	}

	public void setStargazersCount (int stargazersCount) {
		this.stargazersCount = stargazersCount;
		commitNeeded = true;
	}

	public int getActualStargazersCount () {
		return stargazersCount;
	}

	public int getWatchersCount () {
		return watchersCount;
	}

	public void setWatchersCount (int watchersCount) {
		this.watchersCount = watchersCount;
		commitNeeded = true;
	}

	public int getActualWatchersCount () {
		return watchersCount;
	}

	public String getLanguage () {
		if (commitNeeded)
			return "";
		else
			return language;
	}

	public void setLanguage (String language) {
		this.language = language;
		commitNeeded = true;
	}

	public String getActualLanguage () {
		return language;
	}

	public boolean getHasIssues () {
		return hasIssues;
	}

	public void setHasIssues (boolean hasIssues) {
		this.hasIssues = hasIssues;
		commitNeeded = true;
	}

	public boolean getActualHasIssues () {
		return hasIssues;
	}

	public boolean getHasProjects () {
		return hasProjects;
	}

	public void setHasProjects (boolean hasProjects) {
		this.hasProjects = hasProjects;
		commitNeeded = true;
	}

	public boolean getActualHasProjects () {
		return hasProjects;
	}

	public boolean getHasDownloads () {
		return hasDownloads;
	}

	public void setHasDownloads (boolean hasDownloads) {
		this.hasDownloads = hasDownloads;
		commitNeeded = true;
	}

	public boolean getActualHasDownloads () {
		return hasDownloads;
	}

	public boolean getHasWiki () {
		return hasWiki;
	}

	public void setHasWiki (boolean hasWiki) {
		this.hasWiki = hasWiki;
		commitNeeded = true;
	}

	public boolean getActualHasWiki () {
		return hasWiki;
	}

	public boolean getHasPages () {
		return hasPages;
	}

	public void setHasPages (boolean hasPages) {
		this.hasPages = hasPages;
		commitNeeded = true;
	}

	public boolean getActualHasPages () {
		return hasPages;
	}

	public int getForksCount () {
		return forksCount;
	}

	public void setForksCount (int forksCount) {
		this.forksCount = forksCount;
		commitNeeded = true;
	}

	public int getActualForksCount () {
		return forksCount;
	}

	public boolean getArchived () {
		return archived;
	}

	public void setArchived (boolean archived) {
		this.archived = archived;
		commitNeeded = true;
	}

	public boolean getActualArchived () {
		return archived;
	}

	public int getOpenIssuesCount () {
		return openIssuesCount;
	}

	public void setOpenIssuesCount (int openIssuesCount) {
		this.openIssuesCount = openIssuesCount;
		commitNeeded = true;
	}

	public int getActualOpenIssuesCount () {
		return openIssuesCount;
	}

	public String getLicense () {
		if (commitNeeded)
			return "";
		else
			return license;
	}

	public void setLicense (String license) {
		this.license = license;
		commitNeeded = true;
	}

	public String getActualLicense () {
		return license;
	}

	public int getForks () {
		return forks;
	}

	public void setForks (int forks) {
		this.forks = forks;
		commitNeeded = true;
	}

	public int getActualForks () {
		return forks;
	}

	public int getOpenIssues () {
		return openIssues;
	}

	public void setOpenIssues (int openIssues) {
		this.openIssues = openIssues;
		commitNeeded = true;
	}

	public int getActualOpenIssues () {
		return openIssues;
	}

	public int getWatchers () {
		return watchers;
	}

	public void setWatchers (int watchers) {
		this.watchers = watchers;
		commitNeeded = true;
	}

	public int getActualWatchers () {
		return watchers;
	}

	public String getDefaultBranch () {
		if (commitNeeded)
			return "";
		else
			return defaultBranch;
	}

	public void setDefaultBranch (String defaultBranch) {
		this.defaultBranch = defaultBranch;
		commitNeeded = true;
	}

	public String getActualDefaultBranch () {
		return defaultBranch;
	}

	public static Integer IDValue() throws JspException {
		try {
			return currentInstance.getID();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function IDValue()");
		}
	}

	public static String nameValue() throws JspException {
		try {
			return currentInstance.getName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameValue()");
		}
	}

	public static String fullNameValue() throws JspException {
		try {
			return currentInstance.getFullName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function fullNameValue()");
		}
	}

	public static Boolean isPrivateValue() throws JspException {
		try {
			return currentInstance.getIsPrivate();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function isPrivateValue()");
		}
	}

	public static String descriptionValue() throws JspException {
		try {
			return currentInstance.getDescription();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function descriptionValue()");
		}
	}

	public static Boolean forkValue() throws JspException {
		try {
			return currentInstance.getFork();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function forkValue()");
		}
	}

	public static Date createdAtValue() throws JspException {
		try {
			return currentInstance.getCreatedAt();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function createdAtValue()");
		}
	}

	public static Date updatedAtValue() throws JspException {
		try {
			return currentInstance.getUpdatedAt();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function updatedAtValue()");
		}
	}

	public static Date pushedAtValue() throws JspException {
		try {
			return currentInstance.getPushedAt();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function pushedAtValue()");
		}
	}

	public static String homepageValue() throws JspException {
		try {
			return currentInstance.getHomepage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function homepageValue()");
		}
	}

	public static Integer sizeValue() throws JspException {
		try {
			return currentInstance.getSize();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sizeValue()");
		}
	}

	public static Integer stargazersCountValue() throws JspException {
		try {
			return currentInstance.getStargazersCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function stargazersCountValue()");
		}
	}

	public static Integer watchersCountValue() throws JspException {
		try {
			return currentInstance.getWatchersCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function watchersCountValue()");
		}
	}

	public static String languageValue() throws JspException {
		try {
			return currentInstance.getLanguage();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function languageValue()");
		}
	}

	public static Boolean hasIssuesValue() throws JspException {
		try {
			return currentInstance.getHasIssues();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function hasIssuesValue()");
		}
	}

	public static Boolean hasProjectsValue() throws JspException {
		try {
			return currentInstance.getHasProjects();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function hasProjectsValue()");
		}
	}

	public static Boolean hasDownloadsValue() throws JspException {
		try {
			return currentInstance.getHasDownloads();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function hasDownloadsValue()");
		}
	}

	public static Boolean hasWikiValue() throws JspException {
		try {
			return currentInstance.getHasWiki();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function hasWikiValue()");
		}
	}

	public static Boolean hasPagesValue() throws JspException {
		try {
			return currentInstance.getHasPages();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function hasPagesValue()");
		}
	}

	public static Integer forksCountValue() throws JspException {
		try {
			return currentInstance.getForksCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function forksCountValue()");
		}
	}

	public static Boolean archivedValue() throws JspException {
		try {
			return currentInstance.getArchived();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function archivedValue()");
		}
	}

	public static Integer openIssuesCountValue() throws JspException {
		try {
			return currentInstance.getOpenIssuesCount();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function openIssuesCountValue()");
		}
	}

	public static String licenseValue() throws JspException {
		try {
			return currentInstance.getLicense();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function licenseValue()");
		}
	}

	public static Integer forksValue() throws JspException {
		try {
			return currentInstance.getForks();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function forksValue()");
		}
	}

	public static Integer openIssuesValue() throws JspException {
		try {
			return currentInstance.getOpenIssues();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function openIssuesValue()");
		}
	}

	public static Integer watchersValue() throws JspException {
		try {
			return currentInstance.getWatchers();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function watchersValue()");
		}
	}

	public static String defaultBranchValue() throws JspException {
		try {
			return currentInstance.getDefaultBranch();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function defaultBranchValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		name = null;
		fullName = null;
		isPrivate = false;
		description = null;
		fork = false;
		createdAt = null;
		updatedAt = null;
		pushedAt = null;
		homepage = null;
		size = 0;
		stargazersCount = 0;
		watchersCount = 0;
		language = null;
		hasIssues = false;
		hasProjects = false;
		hasDownloads = false;
		hasWiki = false;
		hasPages = false;
		forksCount = 0;
		archived = false;
		openIssuesCount = 0;
		license = null;
		forks = 0;
		openIssues = 0;
		watchers = 0;
		defaultBranch = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();

	}

}
