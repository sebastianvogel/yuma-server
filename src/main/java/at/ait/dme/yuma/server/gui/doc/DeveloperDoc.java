package at.ait.dme.yuma.server.gui.doc;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import at.ait.dme.yuma.server.gui.BaseTextPage;

public class DeveloperDoc extends BaseTextPage {
	
	public DeveloperDoc(final PageParameters parameters) {
		super(parameters);
    	add(new Label("title", "YUMA Title!"));
		add(new Label("heading", "Heading!"));
    	add(new Label("subheading", "sbueading!"));
	}

}
