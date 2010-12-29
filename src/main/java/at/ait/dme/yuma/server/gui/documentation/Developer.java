package at.ait.dme.yuma.server.gui.documentation;

import org.apache.wicket.PageParameters;

import at.ait.dme.yuma.server.gui.BaseTextPage;

public class Developer extends BaseTextPage {
	
	public Developer(final PageParameters parameters) {
    	super(parameters);
    	
    	setTitle("YUMA Annotation Server - Documentation - Developer");
		setHeading("yuma server > documentation");
		setNavbar(new DocumentationNavbar(Developer.class));
	}

}
