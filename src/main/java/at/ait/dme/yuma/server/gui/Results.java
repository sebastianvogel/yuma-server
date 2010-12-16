package at.ait.dme.yuma.server.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.model.Annotation;

public class Results extends WebPage {
	
    public Results(final PageParameters parameters) {
    	List<Annotation> mostRecent = getMostRecent(20);
    	
    	@SuppressWarnings({ "unchecked", "rawtypes" })
		ListView<Annotation> listView = new ListView("listview", mostRecent) {
			private static final long serialVersionUID = 6113269523170287985L;

			@Override
			protected void populateItem(ListItem item) {
				Annotation a = (Annotation) item.getModelObject();
				item.add(new Label("author", a.getCreatedBy()));
				item.add(new Label("title", a.getTitle()));
				item.add(new Label("text", a.getText()));
				item.add(new Label("lastModified", a.getLastModified().toString()));
			}
    	};

    	add(listView);
    }
    
	private List<Annotation> getMostRecent(int n) {
		AbstractAnnotationDB db = null;
		List<Annotation> mostRecent = new ArrayList<Annotation>();
		
		try {
			db = Config.getInstance().getAnnotationDatabase();
			db.connect();
			mostRecent = db.getMostRecent(n);
		} catch (AnnotationDatabaseException e) {
			// TODO log this
		} finally {
			if(db != null) db.disconnect();
		}
		
		return mostRecent;
	}

}
