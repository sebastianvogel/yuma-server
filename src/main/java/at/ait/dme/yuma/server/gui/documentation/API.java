package at.ait.dme.yuma.server.gui.documentation;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.ExternalLink;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.gui.BaseTextPage;

/**
 * The API doc page.
 * 
 * @author Rainer Simon
 */
public class API extends BaseTextPage {
	
	private static final String TITLE = "YUMA Annotation Server - Documentation - API";
	private static final String HEADING = "yuma server > docs > api";
	private static final String BASE_URL = Config.getInstance().getServerBaseUrl();
	
	public API(final PageParameters parameters) {
    	super(TITLE, HEADING, new DocumentationNavbar(API.class));
    	
    	add(new ExternalLink("api-json", BASE_URL + "api", BASE_URL + "api"));
    }

}
