package edu.uiowa.slis.GitHubTagLib.userRepo;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;
import edu.uiowa.slis.GitHubTagLib.GitHubTagLibBodyTagSupport;
import edu.uiowa.slis.GitHubTagLib.user.User;
import edu.uiowa.slis.GitHubTagLib.repository.Repository;

@SuppressWarnings("serial")
public class UserRepoIterator extends GitHubTagLibBodyTagSupport {
    int userId = 0;
    int repositoryId = 0;
	Vector<GitHubTagLibTagSupport> parentEntities = new Vector<GitHubTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(UserRepoIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useUser = false;
   boolean useRepository = false;

	public static String userRepoCountByUser(String ID) throws JspTagException {
		int count = 0;
		UserRepoIterator theIterator = new UserRepoIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.user_repo where 1=1"
						+ " and user_id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating UserRepo iterator", e);
			throw new JspTagException("Error: JDBC error generating UserRepo iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean userHasUserRepo(String ID) throws JspTagException {
		return ! userRepoCountByUser(ID).equals("0");
	}

	public static String userRepoCountByRepository(String ID) throws JspTagException {
		int count = 0;
		UserRepoIterator theIterator = new UserRepoIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.user_repo where 1=1"
						+ " and repository_id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating UserRepo iterator", e);
			throw new JspTagException("Error: JDBC error generating UserRepo iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean repositoryHasUserRepo(String ID) throws JspTagException {
		return ! userRepoCountByRepository(ID).equals("0");
	}

	public static Boolean userRepoExists (String userId, String repositoryId) throws JspTagException {
		int count = 0;
		UserRepoIterator theIterator = new UserRepoIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.user_repo where 1=1"
						+ " and user_id = ?"
						+ " and repository_id = ?"
						);

			stat.setInt(1,Integer.parseInt(userId));
			stat.setInt(2,Integer.parseInt(repositoryId));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating UserRepo iterator", e);
			throw new JspTagException("Error: JDBC error generating UserRepo iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean userRepositoryExists (String ID) throws JspTagException {
		int count = 0;
		UserRepoIterator theIterator = new UserRepoIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from github.user_repo where 1=1"
						+ " and id = ?"
						);

			stat.setInt(1,Integer.parseInt(ID));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating UserRepo iterator", e);
			throw new JspTagException("Error: JDBC error generating UserRepo iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

    public int doStartTag() throws JspException {
		User theUser = (User)findAncestorWithClass(this, User.class);
		if (theUser!= null)
			parentEntities.addElement(theUser);
		Repository theRepository = (Repository)findAncestorWithClass(this, Repository.class);
		if (theRepository!= null)
			parentEntities.addElement(theRepository);

		if (theUser == null) {
		} else {
			userId = theUser.getID();
		}
		if (theRepository == null) {
		} else {
			repositoryId = theRepository.getID();
		}


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (userId == 0 ? "" : " and user_id = ?")
                                                        + (repositoryId == 0 ? "" : " and repository_id = ?")
                                                        + generateLimitCriteria());
            if (userId != 0) stat.setInt(webapp_keySeq++, userId);
            if (repositoryId != 0) stat.setInt(webapp_keySeq++, repositoryId);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT github.user_repo.user_id, github.user_repo.repository_id from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (userId == 0 ? "" : " and user_id = ?")
                                                        + (repositoryId == 0 ? "" : " and repository_id = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (userId != 0) stat.setInt(webapp_keySeq++, userId);
            if (repositoryId != 0) stat.setInt(webapp_keySeq++, repositoryId);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                userId = rs.getInt(1);
                repositoryId = rs.getInt(2);
                if (var != null)
                    pageContext.setAttribute(var, this);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating UserRepo iterator: " + stat, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating UserRepo iterator: " + stat);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating UserRepo iterator: " + stat,e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("github.user_repo");
       if (useUser)
          theBuffer.append(", github.user");
       if (useRepository)
          theBuffer.append(", github.repository");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useUser)
          theBuffer.append(" and github.user.id = github.user_repo.user_id");
       if (useRepository)
          theBuffer.append(" and github.repository.id = github.user_repo.repository_id");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "user_id,repository_id";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspException {
        try {
            if ( rs != null && rs.next() ) {
                userId = rs.getInt(1);
                repositoryId = rs.getInt(2);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across UserRepo", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across UserRepo" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across UserRepo",e);
			}

        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
			if( var != null )
				pageContext.removeAttribute(var);
			if( pageContext != null ){
				Boolean error = (Boolean) pageContext.getAttribute("tagError");
				if( error != null && error ){

					freeConnection();
					clearServiceState();

					Exception e = null; // (Exception) pageContext.getAttribute("tagErrorException");
					String message = null; // (String) pageContext.getAttribute("tagErrorMessage");

					if(pageContext != null){
						e = (Exception) pageContext.getAttribute("tagErrorException");
						message = (String) pageContext.getAttribute("tagErrorMessage");

					}
					Tag parent = getParent();
					if(parent != null){
						return parent.doEndTag();
					}else if(e != null && message != null){
						throw new JspException(message,e);
					}else if(parent == null && pageContext != null){
						pageContext.removeAttribute("tagError");
						pageContext.removeAttribute("tagErrorException");
						pageContext.removeAttribute("tagErrorMessage");
					}
				}
			}

            if( rs != null ){
                rs.close();
            }

            if( stat != null ){
                stat.close();
            }

        } catch ( SQLException e ) {
            log.error("JDBC error ending UserRepo iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving userId " + userId);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending UserRepo iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        userId = 0;
        repositoryId = 0;
        parentEntities = new Vector<GitHubTagLibTagSupport>();

        this.rs = null;
        this.stat = null;
        this.sortCriteria = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public int getLimitCriteria() {
        return limitCriteria;
    }

    public void setLimitCriteria(int limitCriteria) {
        this.limitCriteria = limitCriteria;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public Boolean isFirst() throws SQLException {
        return rs.isFirst();
    }

    public Boolean isLast() throws SQLException {
        return rs.isLast();
    }


   public boolean getUseUser() {
        return useUser;
    }

    public void setUseUser(boolean useUser) {
        this.useUser = useUser;
    }

   public boolean getUseRepository() {
        return useRepository;
    }

    public void setUseRepository(boolean useRepository) {
        this.useRepository = useRepository;
    }



	public int getUserId () {
		return userId;
	}

	public void setUserId (int userId) {
		this.userId = userId;
	}

	public int getActualUserId () {
		return userId;
	}

	public int getRepositoryId () {
		return repositoryId;
	}

	public void setRepositoryId (int repositoryId) {
		this.repositoryId = repositoryId;
	}

	public int getActualRepositoryId () {
		return repositoryId;
	}
}
