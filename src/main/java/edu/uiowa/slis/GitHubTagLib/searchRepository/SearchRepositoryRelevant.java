package edu.uiowa.slis.GitHubTagLib.searchRepository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchRepositoryRelevant extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchRepositoryRelevant.class);


	public int doStartTag() throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			if (!theSearchRepository.commitNeeded) {
				pageContext.getOut().print(theSearchRepository.getRelevant());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for relevant tag ");
		}
		return SKIP_BODY;
	}

	public boolean getRelevant() throws JspTagException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			return theSearchRepository.getRelevant();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchRepository for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for relevant tag ");
		}
	}

	public void setRelevant(boolean relevant) throws JspTagException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			theSearchRepository.setRelevant(relevant);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for relevant tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for relevant tag ");
		}
	}

}
