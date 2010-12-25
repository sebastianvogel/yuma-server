package at.ait.dme.yuma.server.gui.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.gui.components.AnnotationList;
import at.ait.dme.yuma.server.model.Annotation;

public class Results extends AnnotationList {
	
	private Logger logger = Logger.getLogger(Results.class);
	
    public Results(final PageParameters parameters) {
		super(parameters);
		
    	List<Annotation> searchResults = 
    		findAnnotations((String) parameters.get(Search.QUERY_PARAM));
		
		add(new Label("title", "YUMA Annotation Server - Search"));		
		add(new Label("heading", Integer.toString(searchResults.size()) + " Annotations Found"));
		add(new Label("subheading", ""));    	
    	
		setAnnotations(searchResults);
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
