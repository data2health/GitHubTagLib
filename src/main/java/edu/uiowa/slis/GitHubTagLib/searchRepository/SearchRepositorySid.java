package edu.uiowa.slis.GitHubTagLib.searchRepository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchRepositorySid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchRepositorySid.class);


	public int doStartTag() throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			if (!theSearchRepository.commitNeeded) {
				pageContext.getOut().print(theSearchRepository.getSid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for sid tag ");
		}
		return SKIP_BODY;
	}

	public int getSid() throws JspTagException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			return theSearchRepository.getSid();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchRepository for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for sid tag ");
		}
	}

	public void setSid(int sid) throws JspTagException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			theSearchRepository.setSid(sid);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for sid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for sid tag ");
		}
	}

}
