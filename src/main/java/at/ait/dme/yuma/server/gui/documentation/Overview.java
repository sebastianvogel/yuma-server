package at.ait.dme.yuma.server.gui.documentation;

import org.apache.wicket.PageParameters;

import at.ait.dme.yuma.server.gui.BaseTextPage;

public class Overview extends BaseTextPage {
	
	public Overview(final PageParameters parameters) {
    	super(parameters);
    	
    	setTitle("YUMA Annotation Server - Documentation - Overview");
		setHeading("yuma server > documentation");
		setNavbar(new DocumentationNavbar(Overview.class));
	}

}
