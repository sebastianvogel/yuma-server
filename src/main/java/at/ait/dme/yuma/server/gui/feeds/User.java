package at.ait.dme.yuma.server.gui.feeds;

import java.util.ArrayList;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import at.ait.dme.yuma.server.gui.BaseAnnotationListPage;
import at.ait.dme.yuma.server.model.Annotation;

public class User extends BaseAnnotationListPage {

	public User(final PageParameters parameters) {
		super(parameters);

		String username = parameters.getString("username");		
		
		add(new Label("title", "YUMA Annotation Server - User " + username));		
		add(new Label("heading", username + "'s public feed"));
		
		setAnnotations(new ArrayList<Annotation>());
	}

}
