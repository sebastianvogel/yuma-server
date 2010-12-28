package at.ait.dme.yuma.server.gui.doc;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import at.ait.dme.yuma.server.gui.BaseTextPage;

public class Overview extends BaseTextPage {
	
	public Overview(final PageParameters parameters) {
    	super(parameters);
    	
		add(new Label("title", "YUMA Annotation Server - Documentation - Overview"));		
		add(new Label("heading", "YUMA Server Documentation"));
		add(new Label("subheading", "Overview"));
    }

}
