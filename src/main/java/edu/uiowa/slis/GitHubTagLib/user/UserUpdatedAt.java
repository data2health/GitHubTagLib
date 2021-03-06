package edu.uiowa.slis.GitHubTagLib.user;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class UserUpdatedAt extends GitHubTagLibTagSupport {
	String type = "DATE";
	String dateStyle = "DEFAULT";
	String timeStyle = "DEFAULT";
	String pattern = null;
	private static final Log log = LogFactory.getLog(UserUpdatedAt.class);


	public int doStartTag() throws JspException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			if (!theUser.commitNeeded) {
				String resultString = null;
				if (theUser.getUpdatedAt() == null) {
					resultString = "";
				} else {
					if (pattern != null) {
						resultString = (new SimpleDateFormat(pattern)).format(theUser.getUpdatedAt());
					} else if (type.equals("BOTH")) {
						resultString = DateFormat.getDateTimeInstance(formatConvert(dateStyle),formatConvert(timeStyle)).format(theUser.getUpdatedAt());
					} else if (type.equals("TIME")) {
						resultString = DateFormat.getTimeInstance(formatConvert(timeStyle)).format(theUser.getUpdatedAt());
					} else { // date
						resultString = DateFormat.getDateInstance(formatConvert(dateStyle)).format(theUser.getUpdatedAt());
					}
				}
				pageContext.getOut().print(resultString);
			}
		} catch (Exception e) {
			log.error("Can't find enclosing User for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for updatedAt tag ");
		}
		return SKIP_BODY;
	}

	public Date getUpdatedAt() throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			return theUser.getUpdatedAt();
		} catch (Exception e) {
			log.error(" Can't find enclosing User for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for updatedAt tag ");
		}
	}

	public void setUpdatedAt(Date updatedAt) throws JspTagException {
		try {
			User theUser = (User)findAncestorWithClass(this, User.class);
			theUser.setUpdatedAt(updatedAt);
		} catch (Exception e) {
			log.error("Can't find enclosing User for updatedAt tag ", e);
			throw new JspTagException("Error: Can't find enclosing User for updatedAt tag ");
		}
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type.toUpperCase();
	}

	public String getDateStyle() {
		return dateStyle;
	}

	public void setDateStyle(String dateStyle) {
		this.dateStyle = dateStyle.toUpperCase();
	}

	public String getTimeStyle() {
		return timeStyle;
	}

	public void setTimeStyle(String timeStyle) {
		this.timeStyle = timeStyle.toUpperCase();
	}

	public static int formatConvert(String stringValue) {
		if (stringValue.equals("SHORT"))
			return DateFormat.SHORT;
		if (stringValue.equals("MEDIUM"))
			return DateFormat.MEDIUM;
		if (stringValue.equals("LONG"))
			return DateFormat.LONG;
		if (stringValue.equals("FULL"))
			return DateFormat.FULL;
		return DateFormat.DEFAULT;
	}

}
