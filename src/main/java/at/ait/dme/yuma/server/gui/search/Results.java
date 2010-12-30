package at.ait.dme.yuma.server.gui.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.gui.BaseAnnotationListPage;
import at.ait.dme.yuma.server.model.Annotation;

public class Results extends BaseAnnotationListPage {
	
	private Logger logger = Logger.getLogger(Results.class);
	
	private static final String TITLE = "YUMA Annotation Server - Search";
	private static final String HEADLINE = " Annotations Found";
	
    public Results(final PageParameters parameters) {
    	setTitle(TITLE);

    	List<Annotation> searchResults = 
    		findAnnotations((String) parameters.get(Search.QUERY_PARAM));
    	
    	setHeadline(Integer.toString(searchResults.size()) + HEADLINE);    	
		setAnnotations(searchResults);
		setFeedURL(null);
    }
    
	private List<Annotation> findAnnotations(String query) {
		AbstractAnnotationDB db = null;
		List<Annotation> searchResults = new ArrayList<Annotation>();
		
		try {
			db = Config.getInstance().getAnnotationDatabase();
			db.connect();
			searchResults = db.findAnnotations(query);
		} catch (AnnotationDatabaseException e) {
			logger.fatal(e.getMessage());
		} finally {
			if(db != null) db.disconnect();
		}
		
		return searchResults;
	}

}
