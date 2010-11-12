package at.ait.dme.yuma.server;

import java.net.URI;
import java.net.URISyntaxException;

import at.ait.dme.yuma.server.config.Config;

public class URIBuilder {
	
	private static final String ERROR = "Could not build URI for ID ";
	
	public static URI toURI(String annotationID) {
		try {
			return new URI(Config.getInstance().getAnnotationBaseUrl() + annotationID);
		} catch (URISyntaxException e) {
			// Should never happen
			throw new RuntimeException(ERROR + annotationID);
		}
	}

}
