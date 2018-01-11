package edu.uiowa.slis.GitHubTagLib.searchRepository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchRepositoryRid extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchRepositoryRid.class);


	public int doStartTag() throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			if (!theSearchRepository.commitNeeded) {
				pageContext.getOut().print(theSearchRepository.getRid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for rid tag ");
		}
		return SKIP_BODY;
	}

	public int getRid() throws JspTagException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			return theSearchRepository.getRid();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchRepository for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for rid tag ");
		}
	}

	public void setRid(int rid) throws JspTagException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			theSearchRepository.setRid(rid);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for rid tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for rid tag ");
		}
	}

}
