package at.ait.dme.yuma.server.controller;

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

import at.ait.dme.yuma.server.Data;
import at.ait.dme.yuma.server.Setup;
import at.ait.dme.yuma.server.config.Config;

/**
 * Tests for the AnnoteaAnnotationController
 *  
 * @author Christian Sadilek
 * @author Rainer Simon
 */
public class JSONAnnotationControllerTest {
	private static final String JSON_ANNOTATION_CONTROLLER_BASE_URL = 
		"http://localhost:8081/server/json";

	private static final String ACCEPT_HEADER = "Accept";
	private static final String LOCATION_HEADER = "Location";
	private static final String CONTENT_TYPE_JSON = "application/json";
	private static final String ENCODING = "UTF-8";
	
	@BeforeClass
	public static void setUp() throws Exception {
        Setup.startEmbeddedJaxrsServer(JSONAnnotationController.class);
	}

	@AfterClass
	public static void tearDown() throws Exception {	
		Config.getInstance().getAnnotationDatabase().shutdown();			
	}
		
	@Test
	public void testCreateUpdateDeleteAnnotation() throws Exception {
		HttpClient httpClient = new HttpClient();
		
		PostMethod createMethod = new PostMethod(JSON_ANNOTATION_CONTROLLER_BASE_URL);		
		createMethod.setRequestEntity(new StringRequestEntity(Data.JSON_ANNOTATION, 
				CONTENT_TYPE_JSON, ENCODING));						
		assertEquals(httpClient.executeMethod(createMethod), HttpStatus.SC_CREATED);
		Header location = createMethod.getResponseHeader(LOCATION_HEADER);						
		String createdAnnotationUrl = location.getValue();
		assertNotNull(createdAnnotationUrl);
		
		PutMethod updateMethod = new PutMethod(createdAnnotationUrl);
		updateMethod.setRequestEntity(new StringRequestEntity(Data.JSON_ANNOTATION, 
				CONTENT_TYPE_JSON, ENCODING));						
		assertEquals(httpClient.executeMethod(updateMethod), HttpStatus.SC_OK);
		location = updateMethod.getResponseHeader(LOCATION_HEADER);						
		String updatedAnnotationUrl = location.getValue();
		assertNotNull(updatedAnnotationUrl);
	
		DeleteMethod deleteMethod = new DeleteMethod(updatedAnnotationUrl);
		deleteMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		assertEquals(httpClient.executeMethod(deleteMethod), HttpStatus.SC_OK);
		
		// for clients that can't send put and delete
		PostMethod postDeleteMethod = new PostMethod(createdAnnotationUrl);		
		postDeleteMethod.setQueryString("_method=delete");	
		postDeleteMethod.setRequestEntity(new StringRequestEntity("", 
				CONTENT_TYPE_JSON, ENCODING));			
		assertEquals(httpClient.executeMethod(postDeleteMethod), HttpStatus.SC_OK);
	}
	
	@Test
	public void testCreateFindDeleteAnnotation() throws Exception {
		HttpClient httpClient = new HttpClient();
		
		PostMethod createMethod = new PostMethod(JSON_ANNOTATION_CONTROLLER_BASE_URL);		
		createMethod.setRequestEntity(new StringRequestEntity(Data.JSON_ANNOTATION, 
				CONTENT_TYPE_JSON, ENCODING));						
		assertEquals(httpClient.executeMethod(createMethod), HttpStatus.SC_CREATED);
		Header location = createMethod.getResponseHeader(LOCATION_HEADER);						
		String createdAnnotationUrl = location.getValue();
		assertNotNull(createdAnnotationUrl);
		
		GetMethod findByIdMethod = new GetMethod(createdAnnotationUrl);
		findByIdMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		assertEquals(httpClient.executeMethod(findByIdMethod), HttpStatus.SC_OK);
		
		String createdAnnotationBodyUrl = createdAnnotationUrl.replace(
				Config.getInstance().getAnnotationBaseUrl(), 
				Config.getInstance().getAnnotationBodyBaseUrl());
		GetMethod findBodyByIdMethod = new GetMethod(createdAnnotationBodyUrl);
		findBodyByIdMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		assertEquals(httpClient.executeMethod(findBodyByIdMethod), HttpStatus.SC_OK);
		
		GetMethod listAnnMethod = new GetMethod(JSON_ANNOTATION_CONTROLLER_BASE_URL);
		listAnnMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		listAnnMethod.setQueryString("w3c_annotates=" +
				"http://upload.wikimedia.org/wikipedia/commons/7/77/Lissabon.jpg");	
		assertEquals(httpClient.executeMethod(listAnnMethod), HttpStatus.SC_OK);	
		
		DeleteMethod deleteMethod = new DeleteMethod(createdAnnotationUrl);
		deleteMethod.addRequestHeader(ACCEPT_HEADER, CONTENT_TYPE_JSON);
		assertEquals(httpClient.executeMethod(deleteMethod), HttpStatus.SC_OK);
	}
}
