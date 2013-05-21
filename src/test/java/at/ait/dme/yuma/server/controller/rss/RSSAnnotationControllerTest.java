package at.ait.dme.yuma.server.controller.rss;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Ignore;
import org.junit.Test;

public class RSSAnnotationControllerTest {
	
	private static final String RSS_ANNOTATION_CONTROLLER_BASE_URL = 
		"http://localhost:8080/yuma-server/feeds";
	
	private static final String ACCEPT_HEADER = "Accept";
	private static final String CONTENT_TYPE_RSS = "application/rss+xml";
		
	@Test
	@Ignore
	public void testGetTimeline() throws Exception {
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpGet getTimelineMethod = new HttpGet(RSS_ANNOTATION_CONTROLLER_BASE_URL + "/timeline");
		getTimelineMethod.addHeader(ACCEPT_HEADER, CONTENT_TYPE_RSS);
		HttpResponse response = httpClient.execute(getTimelineMethod);
		
		assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
		assertNotNull(response.getEntity());
		// response.getEntity().writeTo(System.out);
		response.getEntity().consumeContent();
	}
	
}
