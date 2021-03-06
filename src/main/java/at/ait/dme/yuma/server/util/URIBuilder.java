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
	
	public static URI toURI(String identifier, URISource source, boolean relative) {
		StringBuilder sb = constructBase(relative);
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
	
	private static StringBuilder constructBase(boolean relative) {
		StringBuilder sb = new StringBuilder();
		String base = Config.getInstance().getServerBaseUrl();
		if (relative) {
			sb.append("/");
		} else {
			sb.append(base);
			if (!base.endsWith("/")) {
				sb.append("/");
			}
		}
		sb.append(API_PATH);
		return sb;		
	}
	
	/**
	 * extract identifier from given uri
	 * @param uri
	 * @return
	 * @throws URISyntaxException
	 */
	public static String toID(String uri) throws URISyntaxException {
		if (!uri.contains(API_PATH)) {
			throw new URISyntaxException(uri, "Not a valid annotation uri");
		}
		return uri.substring(uri.lastIndexOf("/") + 1);
	}
	
	/**
	 * convert given absolute local uri to relative uri
	 * @param uri
	 * @return
	 */
	public static String toRelativeLocalURI(String uri) {
		return uri.replace(Config.getInstance().getServerBaseUrl(), "/");
	}
	
	/** 
	 * check if given URI is a local (=application generated) non-relative URI
	 * @param uri
	 * @return
	 */
	public static boolean isLocal(String uri) {
		return uri.startsWith(Config.getInstance().getServerBaseUrl());
	}
	
	/**
	 * check if give uri is relative (starts with a slash)
	 * @param uri
	 * @return
	 */
	public static boolean isRelative(String uri) {
		return uri.startsWith("/");
	}
	
	/**
	 * check is given uri is a public internet absolut uri
	 * @param uri
	 * @return
	 */
	public static boolean isPublic(String uri) {
		return !isRelative(uri) && !isLocal(uri);
	}
	
	public static boolean isPublic(URI uri) {
		return isPublic(uri.toString());
	}
	
	/**
	 * extract URISource from give URI
	 * @param uri
	 * @return
	 */
	public static URISource getURISource(String uri) {
		boolean relative = isRelative(uri.toString());
		String base = constructBase(relative).toString();
		for (URISource s : URISource.values()) {
			if (uri.startsWith(base.concat(s.toURIFragment()))) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * check if given uri contains given URISource
	 * @param source
	 * @param uri
	 * @return
	 */
	public static boolean isURISource(URISource source, String uri) {
		boolean relative = isRelative(uri.toString());
		String base = constructBase(relative).toString();
		return uri.startsWith(base.concat(source.toURIFragment()));
	}
}
