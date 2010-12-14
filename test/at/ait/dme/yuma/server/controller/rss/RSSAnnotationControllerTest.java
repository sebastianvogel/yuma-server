package at.ait.dme.yuma.server.controller.rss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.Setup;
import at.ait.dme.yuma.server.config.Config;

public class RSSAnnotationControllerTest {
	
	private static final String RSS_ANNOTATION_CONTROLLER_BASE_URL = 
		"http://localhost:8081/yuma-server/feeds";
	
	private static final String ACCEPT_HEADER = "Accept";
	private static final String CONTENT_TYPE_RSS = "application/rss+xml";

	@BeforeClass
	public static void setUp() throws Exception {
		Setup.buildMongoDBConfiguration();
        Setup.startEmbeddedJaxrsServer(RSSAnnotationController.class);
	}

	@AfterClass
	public static void tearDown() throws Exception {	
		Config.getInstance().getAnnotationDatabase().shutdown();			
	}
		
	@Test
	public void testGetTimeline() throws Exception {
		HttpClient httpClient = new HttpClient();
		
		GetMethod getTimelineMethod = new GetMethod(RSS_ANNOTATION_CONTROLLER_BASE_URL + "/timeline");
		getTimelineMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_RSS);
		assertEquals(HttpStatus.SC_OK, httpClient.executeMethod(getTimelineMethod));
		assertNotNull(getTimelineMethod.getResponseBodyAsString());
	}
	
}
