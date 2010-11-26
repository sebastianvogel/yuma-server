package at.ait.dme.yuma.server;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;


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
	
	/**
	 * Server port
	 */
	private static final int SERVER_PORT = 8089;
	
	/**
	 * Relative path to built war file
	 */
	private static final String PATH_TO_WAR = "yuma-server.war";
	
	/**
	 * Webapp context path
	 */
	private static final String CONTEXT_PATH = "/yuma-server";
	
	@Before
	public void setUp() throws Exception {
		final Server server = new Server(SERVER_PORT);
		server.setHandler(new WebAppContext(PATH_TO_WAR, CONTEXT_PATH));
		server.setStopAtShutdown(true);
		server.start();
	}
	
	
	@Test
	public void crudTest() throws Exception {
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod("http://localhost:8089/yuma-server/json/2134bn2341");
		System.out.println(httpClient.executeMethod(getMethod));
		System.out.println("done.");
	}

}
