package at.ait.dme.yuma.server;

import java.net.URI;
import java.net.URISyntaxException;

import at.ait.dme.yuma.server.config.Config;

/**
 * Utility class for building annotation URIs.
 * 
 * @author Rainer Simon
 */
public class URIBuilder {
	
	private static final String ERROR = "Could not build URI for ID ";
	
	public static URI toURI(String annotationID) {
		try {
			return new URI(Config.getInstance().getServerBaseUrl() + "api/annotation/" + annotationID);
		} catch (URISyntaxException e) {
			// Should never happen
			throw new RuntimeException(ERROR + annotationID);
		}
	}
	
	public static String toID(String uri) {
		return uri.substring(uri.lastIndexOf("/") + 1);
	}

}
