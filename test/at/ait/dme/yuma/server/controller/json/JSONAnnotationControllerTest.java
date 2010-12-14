package at.ait.dme.yuma.server.controller.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ait.dme.yuma.server.bootstrap.Data;
import at.ait.dme.yuma.server.bootstrap.Setup;
import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.json.JSONAnnotationController;

/**
 * Tests for the AnnoteaAnnotationController
 *  
 * @author Christian Sadilek
 * @author Rainer Simon
 */
public class JSONAnnotationControllerTest {
	private static final String JSON_ANNOTATION_CONTROLLER_BASE_URL = 
		"http://localhost:8081/yuma-server/api/annotation";

	private static final String ACCEPT_HEADER = "Accept";
	private static final String LOCATION_HEADER = "Location";
	private static final String CONTENT_TYPE_JSON = "application/json";
	private static final String ENCODING = "UTF-8";
	
	@BeforeClass
	public static void setUp() throws Exception {
		Setup.buildMongoDBConfiguration();
        Setup.startEmbeddedJaxrsServer(JSONAnnotationController.class);
	}

	@AfterClass
	public static void tearDown() throws Exception {	
		Config.getInstance().getAnnotationDatabase().shutdown();			
	}
		
	@Test
	public void testCreateUpdateDeleteAnnotation() throws Exception {
		HttpClient httpClient = new HttpClient();
			
		// Create
		PostMethod createMethod = new PostMethod(JSON_ANNOTATION_CONTROLLER_BASE_URL);		
		createMethod.setRequestEntity(new StringRequestEntity(Data.ANNOTATION_JSON_ORIGINAL, 
				CONTENT_TYPE_JSON, ENCODING));						
		assertEquals(httpClient.executeMethod(createMethod), HttpStatus.SC_CREATED);
		
		Header location = createMethod.getResponseHeader(LOCATION_HEADER);						
		String createdAnnotationUrl = location.getValue();
		assertNotNull(createdAnnotationUrl);
		
		// Read
		GetMethod findByIdMethod = new GetMethod(createdAnnotationUrl);
		findByIdMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		assertEquals(HttpStatus.SC_OK, httpClient.executeMethod(findByIdMethod));
		httpClient.executeMethod(findByIdMethod);
		
		// Update
		PutMethod updateMethod = new PutMethod(createdAnnotationUrl);
		updateMethod.setRequestEntity(new StringRequestEntity(Data.ANNOTATION_JSON_UPDATE, 
				CONTENT_TYPE_JSON, ENCODING));						
		assertEquals(HttpStatus.SC_OK, httpClient.executeMethod(updateMethod));
		location = updateMethod.getResponseHeader(LOCATION_HEADER);						
		String updatedAnnotationUrl = location.getValue();
		assertNotNull(updatedAnnotationUrl);
		
		// Delete
		DeleteMethod deleteMethod = new DeleteMethod(updatedAnnotationUrl);
		deleteMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		assertEquals(HttpStatus.SC_NO_CONTENT, httpClient.executeMethod(deleteMethod));
		
		/* for clients that can't send put and delete
		PostMethod postDeleteMethod = new PostMethod(createdAnnotationUrl);		
		postDeleteMethod.setQueryString("_method=delete");	
		postDeleteMethod.setRequestEntity(new StringRequestEntity("", 
				CONTENT_TYPE_JSON, ENCODING));			
		assertEquals(httpClient.executeMethod(postDeleteMethod), HttpStatus.SC_OK);
		*/
	}
	
	/**
	 * Creates the following annotation tree structure
	 * 
	 * - root #1
	 *   - reply #1
	 *     - sub-reply #1
	 *   - reply #2
	 * - root #2
	 *   - reply #3
	 */
	@Test
	public void testReplyFunctionality() throws Exception {
		HttpClient httpClient = new HttpClient();
		
		// root #1
		PostMethod createMethod = new PostMethod(JSON_ANNOTATION_CONTROLLER_BASE_URL);		
		createMethod.setRequestEntity(new StringRequestEntity(Data.ROOT_JSON, 
				CONTENT_TYPE_JSON, ENCODING));						
		assertEquals(httpClient.executeMethod(createMethod), HttpStatus.SC_CREATED);
		Header location = createMethod.getResponseHeader(LOCATION_HEADER);						
		String root1 = location.getValue();
		root1 = root1.substring(root1.lastIndexOf("/") + 1);
		
		// root #2
		createMethod = new PostMethod(JSON_ANNOTATION_CONTROLLER_BASE_URL);		
		createMethod.setRequestEntity(new StringRequestEntity(Data.ROOT_JSON, 
				CONTENT_TYPE_JSON, ENCODING));						
		assertEquals(httpClient.executeMethod(createMethod), HttpStatus.SC_CREATED);
		location = createMethod.getResponseHeader(LOCATION_HEADER);						
		String root2 = location.getValue();
		root2 = root2.substring(root2.lastIndexOf("/") + 1);
		
		// reply #1
		createMethod = new PostMethod(JSON_ANNOTATION_CONTROLLER_BASE_URL);		
		createMethod.setRequestEntity(new StringRequestEntity(Data.reply(root1, root1), 
				CONTENT_TYPE_JSON, ENCODING));						
		assertEquals(httpClient.executeMethod(createMethod), HttpStatus.SC_CREATED);
		location = createMethod.getResponseHeader(LOCATION_HEADER);						
		String reply1 = location.getValue();
		assertNotNull(reply1);
		
		/* Read
		String treeUrl = "http://localhost:8081/yuma-server/tree/object-lissabon";
		
		GetMethod findTreeMethod = new GetMethod(treeUrl);
		findTreeMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		httpClient.executeMethod(findTreeMethod);
		*/
	}
	
}
