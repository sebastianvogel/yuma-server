package at.ait.dme.yuma.server.gui;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.MixedParamUrlCodingStrategy;

import at.ait.dme.yuma.server.gui.doc.APIDoc;
import at.ait.dme.yuma.server.gui.doc.DeveloperDoc;
import at.ait.dme.yuma.server.gui.doc.Overview;
import at.ait.dme.yuma.server.gui.feeds.Timeline;
import at.ait.dme.yuma.server.gui.feeds.User;
import at.ait.dme.yuma.server.gui.search.Search;

/**
 * Application object for your web application. If you want to run 
 * this application without deploying, run the Start class.
 * 
 * @see at.ait.dme.yuma.server.bootstrap.StartAnnotationServer#main(String[])
 */
public class WicketApplication extends WebApplication {    
    
	public WicketApplication() {
		this.mountBookmarkablePage("timeline", Timeline.class);
		
		this.mount(new MixedParamUrlCodingStrategy(
				"user", 
				User.class,
				new String[]{"username"}
		));
		
		this.mountBookmarkablePage("doc/overview", Overview.class);
		this.mountBookmarkablePage("doc/api", APIDoc.class);
		this.mountBookmarkablePage("doc/developer", DeveloperDoc.class);
	}
	
	public Class<Search> getHomePage() {
		return Search.class;
	}

}
