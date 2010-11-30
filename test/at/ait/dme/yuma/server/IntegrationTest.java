package at.ait.dme.yuma.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;


import org.junit.Before;
import org.junit.Test;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Conducts tests against the built .war file deployed in an embedded 
 * Web server.
 * 
 * @author Rainer Simon
 */
public class IntegrationTest {
	
	private static final String ACCEPT_HEADER = "Accept";
	private static final String CONTENT_TYPE_JSON = "application/json";
	private static final String ENCODING = "UTF-8";
	private static final String LOCATION_HEADER = "Location";
	
	private static final int SERVER_PORT = 8089;
	
	private static final String WAR_FILE = "yuma-server.war";
	private static final String CONTEXT_PATH = "/yuma-server";
	
	private static final String BASE_PATH = "http://localhost:" + 
								SERVER_PORT + 
								CONTEXT_PATH + "/json";
	
	@Before
	public void setUp() throws Exception {
		final Server server = new Server(SERVER_PORT);
		server.setHandler(new WebAppContext(WAR_FILE, CONTEXT_PATH));
		server.setStopAtShutdown(true);
		server.start();
	}
	
	
	@Test
	public void testCRUD() throws Exception {
		HttpClient httpClient = new HttpClient();
		
		// Create
		PostMethod createMethod = new PostMethod(BASE_PATH);
		createMethod.setRequestEntity(new StringRequestEntity(
				Data.ANNOTATION_JSON_ORIGINAL, CONTENT_TYPE_JSON, ENCODING));		
		httpClient.executeMethod(createMethod);
		Header location = createMethod.getResponseHeader(LOCATION_HEADER);						
		String createdAnnotationUrl = location.getValue();
		assertNotNull(createdAnnotationUrl);
		
		// Read
		GetMethod getMethod = new GetMethod(createdAnnotationUrl);
		httpClient.executeMethod(getMethod);
		
		// TODO Update
		
		// Delete
		DeleteMethod deleteMethod = new DeleteMethod(createdAnnotationUrl);
		deleteMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		assertEquals(HttpStatus.SC_NO_CONTENT, httpClient.executeMethod(deleteMethod));
	}

}
