package at.ait.dme.yuma.server.controller.rss;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.AbstractAnnotationController;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;

@Path("/feeds")
public class RSSAnnotationController extends AbstractAnnotationController {
	
	private static final int TIMELINE_LENGTH = 20;
	private static final String TIMELINE_TITLE = "YUMA Timeline Feed";
	private static final String TIMELINE_DESCRIPTION = "Most recent annotations on this YUMA annotation server.";
	private static final String TIMELINE_URL = Config.getInstance().getAnnotationBaseUrl() + "feeds/timeline";
	
	@GET
	@Produces("application/rss+xml")
	@Path("/timeline")
	public Response getTimeline()
		throws AnnotationDatabaseException, AnnotationNotFoundException, UnsupportedEncodingException {
		
		return super.getMostRecent(TIMELINE_LENGTH, new RSSFormatHandler(
				TIMELINE_TITLE,
				TIMELINE_DESCRIPTION,
				TIMELINE_URL));
	}
	
}
