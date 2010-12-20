package at.ait.dme.yuma.server.gui.feeds;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.gui.components.AnnotationListView;
import at.ait.dme.yuma.server.model.Annotation;

public class Timeline extends WebPage {
	
	private Logger logger = Logger.getLogger(Timeline.class);
	
	public Timeline(final PageParameters parameters) {
    	add(new AnnotationListView("listview", getMostRecent(20)));
    }
    
	private List<Annotation> getMostRecent(int n) {
		AbstractAnnotationDB db = null;
		List<Annotation> mostRecent = new ArrayList<Annotation>();
		
		try {
			db = Config.getInstance().getAnnotationDatabase();
			db.connect();
			mostRecent = db.getMostRecent(n);
		} catch (AnnotationDatabaseException e) {
			logger.fatal(e.getMessage());
		} finally {
			if(db != null) db.disconnect();
		}
		
		return mostRecent;
	}

}
