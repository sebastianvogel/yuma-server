package at.ait.dme.yuma.server.util;

import java.net.URI;
import java.net.URISyntaxException;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.model.URISource;

/**
 * Utility class for building annotation URIs.
 * 
 * @author Rainer Simon
 */
public class URIBuilder {
	
	private static final String ERROR = "Could not build URI for ";
	private static final String API_PATH = "api/";
	
	public static URI toURI(String identifier, URISource source) {
		StringBuilder sb = new StringBuilder();
		String base = Config.getInstance().getServerBaseUrl();
		sb.append(base);
		if (!base.endsWith("/")) {
			sb.append("/");
		}
		sb.append(API_PATH);
		if (source!=null) {
			sb.append(source.toURIFragment());
		}
		sb.append(identifier);
		
		try {
			return new URI(sb.toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(ERROR + sb.toString());
		}
	}
	
	public static String toID(String uri) throws URISyntaxException {
		if (!uri.contains(API_PATH)) {
			throw new URISyntaxException(uri, "Not a valid annotation uri");
		}
		return uri.substring(uri.lastIndexOf("/") + 1);
	}
	
	/** 
	 * check if given URI is a local (=application generated) URI
	 * @param uri
	 * @return
	 */
	public static boolean isLocal(String uri) {
		return uri.startsWith(Config.getInstance().getServerBaseUrl());
	}
}