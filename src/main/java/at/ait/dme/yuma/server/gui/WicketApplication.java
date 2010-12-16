package at.ait.dme.yuma.server.gui;

import org.apache.wicket.protocol.http.WebApplication;

/**
 * Application object for your web application. If you want to run 
 * this application without deploying, run the Start class.
 * 
 * @see at.ait.dme.yuma.server.gui.Start#main(String[])
 */
public class WicketApplication extends WebApplication {    
    
	public WicketApplication() { }
	
	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

}
