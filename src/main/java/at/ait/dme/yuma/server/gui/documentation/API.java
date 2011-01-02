package at.ait.dme.yuma.server.gui.documentation;

import org.apache.wicket.PageParameters;

import at.ait.dme.yuma.server.gui.BaseTextPage;

public class API extends BaseTextPage {
	
	private static final String TITLE = "YUMA Annotation Server - Documentation - API";
	private static final String HEADING = "yuma server > docs > api";
	
	public API(final PageParameters parameters) {
    	super(TITLE, HEADING, new DocumentationNavbar(API.class));
    }

}
