package at.ait.dme.yuma.server.gui;

import org.apache.wicket.protocol.http.WebApplication;

import at.ait.dme.yuma.server.gui.feeds.Timeline;
import at.ait.dme.yuma.server.gui.search.Search;

/**
 * Application object for your web application. If you want to run 
 * this application without deploying, run the Start class.
 * 
 * @see at.ait.dme.yuma.server.gui.Start#main(String[])
 */
public class WicketApplication extends WebApplication {    
    
	public WicketApplication() {
		this.mountBookmarkablePage("pages/timeline", Timeline.class);
	}
	
	public Class<Search> getHomePage() {
		return Search.class;
	}

}
