package at.ait.dme.yuma.server.controller.rss;

import java.io.IOException;
import java.io.StringWriter;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import at.ait.dme.yuma.server.controller.FormatHandler;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * Format handler for RSS (serialization only)
 * 
 * @author Rainer Simon
 */
public class RSSFormatHandler implements FormatHandler {

	private static final String TIMELINE_TITLE = "YUMA Recent Annotations";
	private static final String TIMELINE_DESCRIPTION = "Feed generated with Rome!";
	private static final String TIMELINE_LINK = "http://localhost:8080/yuma-server/feeds/timeline";
	
	@Override
	public Annotation parse(String serialized)
			throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public String serialize(Annotation annotation) 
			throws UnsupportedOperationException {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public String serialize(AnnotationTree tree) {
		SyndFeed feed = new SyndFeedImpl();
		feed.setFeedType("rss_2.0");
		feed.setTitle(TIMELINE_TITLE);
		feed.setDescription(TIMELINE_DESCRIPTION);
		feed.setLink(TIMELINE_LINK);
		
		try {
			SyndFeedOutput output = new SyndFeedOutput();
			StringWriter sw = new StringWriter();
			output.output(feed, sw);
			return sw.toString();
		} catch (IOException e) {
			// TODO log this
			System.out.println(e.getMessage());
		} catch (FeedException e) {
			// TODO log this
			System.out.println(e.getMessage());
		}
		return null;
	}

}
