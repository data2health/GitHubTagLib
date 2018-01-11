package edu.uiowa.slis.GitHubTagLib.searchRepository;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.slis.GitHubTagLib.GitHubTagLibTagSupport;

@SuppressWarnings("serial")
public class SearchRepositoryRank extends GitHubTagLibTagSupport {
	private static final Log log = LogFactory.getLog(SearchRepositoryRank.class);


	public int doStartTag() throws JspException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			if (!theSearchRepository.commitNeeded) {
				pageContext.getOut().print(theSearchRepository.getRank());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for rank tag ");
		}
		return SKIP_BODY;
	}

	public int getRank() throws JspTagException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			return theSearchRepository.getRank();
		} catch (Exception e) {
			log.error(" Can't find enclosing SearchRepository for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for rank tag ");
		}
	}

	public void setRank(int rank) throws JspTagException {
		try {
			SearchRepository theSearchRepository = (SearchRepository)findAncestorWithClass(this, SearchRepository.class);
			theSearchRepository.setRank(rank);
		} catch (Exception e) {
			log.error("Can't find enclosing SearchRepository for rank tag ", e);
			throw new JspTagException("Error: Can't find enclosing SearchRepository for rank tag ");
		}
	}

}
