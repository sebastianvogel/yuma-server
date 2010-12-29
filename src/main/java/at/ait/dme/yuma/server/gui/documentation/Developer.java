package at.ait.dme.yuma.server.gui.documentation;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import at.ait.dme.yuma.server.gui.BaseTextPage;

public class Developer extends BaseTextPage {
	
	public Developer(final PageParameters parameters) {
		super(parameters);
    	add(new Label("title", "YUMA Title!"));
		add(new Label("heading", "Heading!"));
    	add(new Label("subheading", "sbueading!"));
	}

}
