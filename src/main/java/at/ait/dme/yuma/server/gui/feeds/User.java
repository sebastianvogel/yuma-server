package at.ait.dme.yuma.server.gui.feeds;

import java.util.ArrayList;

import org.apache.wicket.PageParameters;

import at.ait.dme.yuma.server.gui.BaseAnnotationListPage;
import at.ait.dme.yuma.server.model.Annotation;

public class User extends BaseAnnotationListPage {

	public static final String PARAM_USERNAME = "username";
	
	private static final String TITLE = "YUMA Annotation Server - User ";
	private static final String HEADLINE = "'s public feed";
	private static final String FEEDS = "feeds/";
	
	
	public User(final PageParameters parameters) {
		String username = parameters.getString(PARAM_USERNAME);		
		
		setTitle(TITLE + username);
		setHeadline(username + HEADLINE);		
		setAnnotations(new ArrayList<Annotation>());
		setFeedURL(FEEDS + username);
	}

}
