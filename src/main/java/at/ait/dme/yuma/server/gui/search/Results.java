package at.ait.dme.yuma.server.gui.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.gui.components.AnnotationListView;
import at.ait.dme.yuma.server.model.Annotation;

public class Results extends WebPage {
	
    public Results(final PageParameters parameters) {
    	List<Annotation> searchResults = 
    		findAnnotations((String) parameters.get(Search.QUERY_PARAM));
    
    	add(new Label("numResults", Integer.toString(searchResults.size())));
    	add(new AnnotationListView("listview", searchResults));
    }
    
	private List<Annotation> findAnnotations(String query) {
		AbstractAnnotationDB db = null;
		List<Annotation> searchResults = new ArrayList<Annotation>();
		
		try {
			db = Config.getInstance().getAnnotationDatabase();
			db.connect();
			searchResults = db.findAnnotations(query);
		} catch (AnnotationDatabaseException e) {
			// TODO log this
		} finally {
			if(db != null) db.disconnect();
		}
		
		return searchResults;
	}

}
