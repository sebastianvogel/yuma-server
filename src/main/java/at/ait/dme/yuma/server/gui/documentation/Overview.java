package at.ait.dme.yuma.server.gui.documentation;

import org.apache.wicket.PageParameters;

import at.ait.dme.yuma.server.gui.BaseTextPage;

public class Overview extends BaseTextPage {
	
	private static final String TITLE = "YUMA Annotation Server - Documentation - Overview";
	private static final String HEADING = "yuma server > documentation";
	
	public Overview(final PageParameters parameters) {
    	super(TITLE, HEADING, new DocumentationNavbar(Overview.class));
	}

}
