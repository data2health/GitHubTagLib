package edu.uiowa.slis.GitHubTagLib.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;


import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.Sequence;

@SuppressWarnings("serial")
public class User extends GitHubTagLibTagSupport {

	static User currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(User.class);

	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	int ID = 0;
	String login = null;
	String name = null;
	String company = null;
	String location = null;
	String email = null;
	String bio = null;
	String blog = null;
	boolean siteAdmin = false;
	int publicRepos = 0;
	int publicGists = 0;
	int followers = 0;
	int following = 0;
	Timestamp createdAt = null;
	Timestamp updatedAt = null;

	private String var = null;

	private User cachedUser = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			UserIterator theUserIterator = (UserIterator)findAncestorWithClass(this, UserIterator.class);

			if (theUserIterator != null) {
				ID = theUserIterator.getID();
			}

			if (theUserIterator == null && ID == 0) {
				// no ID was provided - the default is to assume that it is a new User and to generate a new ID
				ID = Sequence.generateID();
				insertEntity();
			} else {
				// an iterator or ID was provided as an attribute - we need to load a User from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select login,name,company,location,email,bio,blog,site_admin,public_repos,public_gists,followers,following,created_at,updated_at from github.user where id = ?");
				stmt.setInt(1,ID);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (login == null)
						login = rs.getString(1);
					if (name == null)
						name = rs.getString(2);
					if (company == null)
						company = rs.getString(3);
					if (location == null)
						location = rs.getString(4);
					if (email == null)
						email = rs.getString(5);
					if (bio == null)
						bio = rs.getString(6);
					if (blog == null)
						blog = rs.getString(7);
					if (siteAdmin == false)
						siteAdmin = rs.getBoolean(8);
					if (publicRepos == 0)
						publicRepos = rs.getInt(9);
					if (publicGists == 0)
						publicGists = rs.getInt(10);
					if (followers == 0)
						followers = rs.getInt(11);
					if (following == 0)
						following = rs.getInt(12);
					if (createdAt == null)
						createdAt = rs.getTimestamp(13);
					if (updatedAt == null)
						updatedAt = rs.getTimestamp(14);
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
			User currentUser = (User) pageContext.getAttribute("tag_user");
			if(currentUser != null){
				cachedUser = currentUser;
			}
			currentUser = this;
			pageContext.setAttribute((var == null ? "tag_user" : var), currentUser);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedUser != null){
				pageContext.setAttribute((var == null ? "tag_user" : var), this.cachedUser);
			}else{
				pageContext.removeAttribute((var == null ? "tag_user" : var));
				this.cachedUser = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update github.user set login = ?, name = ?, company = ?, location = ?, email = ?, bio = ?, blog = ?, site_admin = ?, public_repos = ?, public_gists = ?, followers = ?, following = ?, created_at = ?, updated_at = ? where id = ? ");
				stmt.setString( 1, login );
				stmt.setString( 2, name );
				stmt.setString( 3, company );
				stmt.setString( 4, location );
				stmt.setString( 5, email );
				stmt.setString( 6, bio );
				stmt.setString( 7, blog );
				stmt.setBoolean( 8, siteAdmin );
				stmt.setInt( 9, publicRepos );
				stmt.setInt( 10, publicGists );
				stmt.setInt( 11, followers );
				stmt.setInt( 12, following );
				stmt.setTimestamp( 13, createdAt );
				stmt.setTimestamp( 14, updatedAt );
				stmt.setInt(15,ID);
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
			log.debug("generating new User " + ID);
		}

		if (login == null){
			login = "";
		}
		if (name == null){
			name = "";
		}
		if (company == null){
			company = "";
		}
		if (location == null){
			location = "";
		}
		if (email == null){
			email = "";
		}
		if (bio == null){
			bio = "";
		}
		if (blog == null){
			blog = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into github.user(id,login,name,company,location,email,bio,blog,site_admin,public_repos,public_gists,followers,following,created_at,updated_at) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		stmt.setInt(1,ID);
		stmt.setString(2,login);
		stmt.setString(3,name);
		stmt.setString(4,company);
		stmt.setString(5,location);
		stmt.setString(6,email);
		stmt.setString(7,bio);
		stmt.setString(8,blog);
		stmt.setBoolean(9,siteAdmin);
		stmt.setInt(10,publicRepos);
		stmt.setInt(11,publicGists);
		stmt.setInt(12,followers);
		stmt.setInt(13,following);
		stmt.setTimestamp(14,createdAt);
		stmt.setTimestamp(15,updatedAt);
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

	public String getLogin () {
		if (commitNeeded)
			return "";
		else
			return login;
	}

	public void setLogin (String login) {
		this.login = login;
		commitNeeded = true;
	}

	public String getActualLogin () {
		return login;
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

	public String getCompany () {
		if (commitNeeded)
			return "";
		else
			return company;
	}

	public void setCompany (String company) {
		this.company = company;
		commitNeeded = true;
	}

	public String getActualCompany () {
		return company;
	}

	public String getLocation () {
		if (commitNeeded)
			return "";
		else
			return location;
	}

	public void setLocation (String location) {
		this.location = location;
		commitNeeded = true;
	}

	public String getActualLocation () {
		return location;
	}

	public String getEmail () {
		if (commitNeeded)
			return "";
		else
			return email;
	}

	public void setEmail (String email) {
		this.email = email;
		commitNeeded = true;
	}

	public String getActualEmail () {
		return email;
	}

	public String getBio () {
		if (commitNeeded)
			return "";
		else
			return bio;
	}

	public void setBio (String bio) {
		this.bio = bio;
		commitNeeded = true;
	}

	public String getActualBio () {
		return bio;
	}

	public String getBlog () {
		if (commitNeeded)
			return "";
		else
			return blog;
	}

	public void setBlog (String blog) {
		this.blog = blog;
		commitNeeded = true;
	}

	public String getActualBlog () {
		return blog;
	}

	public boolean getSiteAdmin () {
		return siteAdmin;
	}

	public void setSiteAdmin (boolean siteAdmin) {
		this.siteAdmin = siteAdmin;
		commitNeeded = true;
	}

	public boolean getActualSiteAdmin () {
		return siteAdmin;
	}

	public int getPublicRepos () {
		return publicRepos;
	}

	public void setPublicRepos (int publicRepos) {
		this.publicRepos = publicRepos;
		commitNeeded = true;
	}

	public int getActualPublicRepos () {
		return publicRepos;
	}

	public int getPublicGists () {
		return publicGists;
	}

	public void setPublicGists (int publicGists) {
		this.publicGists = publicGists;
		commitNeeded = true;
	}

	public int getActualPublicGists () {
		return publicGists;
	}

	public int getFollowers () {
		return followers;
	}

	public void setFollowers (int followers) {
		this.followers = followers;
		commitNeeded = true;
	}

	public int getActualFollowers () {
		return followers;
	}

	public int getFollowing () {
		return following;
	}

	public void setFollowing (int following) {
		this.following = following;
		commitNeeded = true;
	}

	public int getActualFollowing () {
		return following;
	}

	public Timestamp getCreatedAt () {
		return createdAt;
	}

	public void setCreatedAt (Timestamp createdAt) {
		this.createdAt = createdAt;
		commitNeeded = true;
	}

	public Timestamp getActualCreatedAt () {
		return createdAt;
	}

	public void setCreatedAtToNow ( ) {
		this.createdAt = new java.sql.Timestamp(new java.util.Date().getTime());
		commitNeeded = true;
	}

	public Timestamp getUpdatedAt () {
		return updatedAt;
	}

	public void setUpdatedAt (Timestamp updatedAt) {
		this.updatedAt = updatedAt;
		commitNeeded = true;
	}

	public Timestamp getActualUpdatedAt () {
		return updatedAt;
	}

	public void setUpdatedAtToNow ( ) {
		this.updatedAt = new java.sql.Timestamp(new java.util.Date().getTime());
		commitNeeded = true;
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

	public static String loginValue() throws JspException {
		try {
			return currentInstance.getLogin();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function loginValue()");
		}
	}

	public static String nameValue() throws JspException {
		try {
			return currentInstance.getName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nameValue()");
		}
	}

	public static String companyValue() throws JspException {
		try {
			return currentInstance.getCompany();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function companyValue()");
		}
	}

	public static String locationValue() throws JspException {
		try {
			return currentInstance.getLocation();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function locationValue()");
		}
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static String bioValue() throws JspException {
		try {
			return currentInstance.getBio();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function bioValue()");
		}
	}

	public static String blogValue() throws JspException {
		try {
			return currentInstance.getBlog();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function blogValue()");
		}
	}

	public static Boolean siteAdminValue() throws JspException {
		try {
			return currentInstance.getSiteAdmin();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function siteAdminValue()");
		}
	}

	public static Integer publicReposValue() throws JspException {
		try {
			return currentInstance.getPublicRepos();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function publicReposValue()");
		}
	}

	public static Integer publicGistsValue() throws JspException {
		try {
			return currentInstance.getPublicGists();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function publicGistsValue()");
		}
	}

	public static Integer followersValue() throws JspException {
		try {
			return currentInstance.getFollowers();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function followersValue()");
		}
	}

	public static Integer followingValue() throws JspException {
		try {
			return currentInstance.getFollowing();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function followingValue()");
		}
	}

	public static Timestamp createdAtValue() throws JspException {
		try {
			return currentInstance.getCreatedAt();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function createdAtValue()");
		}
	}

	public static Timestamp updatedAtValue() throws JspException {
		try {
			return currentInstance.getUpdatedAt();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function updatedAtValue()");
		}
	}

	private void clearServiceState () {
		ID = 0;
		login = null;
		name = null;
		company = null;
		location = null;
		email = null;
		bio = null;
		blog = null;
		siteAdmin = false;
		publicRepos = 0;
		publicGists = 0;
		followers = 0;
		following = 0;
		createdAt = null;
		updatedAt = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<GitHubTagLibTagSupport>();
		this.var = null;

	}

}
