package at.ait.dme.yuma.server.gui;

import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import at.ait.dme.yuma.server.URIBuilder;
import at.ait.dme.yuma.server.model.Annotation;

public abstract class BaseAnnotationListPage extends WebPage {

	private static final String SPAN = "<span class=\"grad\"></span>";
		
	public void setTitle(String title) {
		add(new Label("title", title));				
	}
		
	public void setHeadline(String headline) {
		add(new Label("heading", SPAN + headline).setEscapeModelStrings(false));		
	}
	
	public void setAnnotations(List<Annotation> annotations) {
		add(new AnnotationListView("annotations", annotations));				
	}
	
	public void setFeedURL(String feedUrl) {
		if (feedUrl != null) {
			add(new Label("feed-icon", "<img src=\"images/feed-icon-28x28.png\" />").setEscapeModelStrings(false));			
		} else {
			add(new Label("feed-icon", ""));			
		}		
	}
	
	private class AnnotationListView extends ListView<Annotation> {

		private static final long serialVersionUID = 6677934776500475422L;

		public AnnotationListView(String id, List<Annotation> list) {
			super(id, list);
		}

		@Override
		protected void populateItem(ListItem<Annotation> item) {
			Annotation a = (Annotation) item.getModelObject();
			item.add(new Label("title", a.getTitle()));
			item.add(new Label("author", a.getCreatedBy()));
			item.add(new Label("objectUri", a.getObjectUri()));
			item.add(new Label("lastModified", a.getLastModified().toString()));
			item.add(new Label("text", a.getText()));
			String uri = URIBuilder.toURI(a.getAnnotationID()).toString();
			item.add(new ExternalLink("uri", uri, uri));
		}
		
	}

}
