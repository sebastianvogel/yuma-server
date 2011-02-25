package at.ait.dme.yuma.server.db.europeana;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.jboss.resteasy.util.HttpResponseCodes;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * Adapter class that implements an AbstractAnnotationDB
 * front-end to the remote Europeana Annotation API.
 * 
 * @author Rainer Simon
 */
public class EuropeanaDB extends AbstractAnnotationDB {

	/**
	 * The URL to the Europeana Annotation API
	 */
	private static String EUROPEANA_API_URL;
	
	/**
	 * The calling Servlet request
	 */
	private HttpServletRequest request = null;
	
	/**
	 * The response to the calling Servlet request
	 */
	private HttpServletResponse response = null;
	
	@Override
	public void init() throws AnnotationDatabaseException {
		final Config config = Config.getInstance();
		EUROPEANA_API_URL = "http://" + config.getDbHost() + ":" + config.getDbPort() + "/";
	}

	@Override
	public void shutdown() {
		// Nothing to do
	}

	@Override
	public void connect(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	@Override
	public void disconnect() {
		// Nothing to do
	}

	@Override
	public void commit() {
		// Nothing to do
	}

	@Override
	public void rollback() {
		throw new RuntimeException("Sorry - rollbacks not supported by Europeana API");
	}

	@Override
	public String createAnnotation(Annotation annotation)
			throws AnnotationDatabaseException, AnnotationModifiedException,
			InvalidAnnotationException {
		EuropeanaAnnotationAPI api = getAPI();       
        ClientResponse<String> response = api.createAnnotation("http://www.europeana.eu/resolve/record/92001/9C1BB12A62C6908E10DF37101DF222FBFFD1168C", new JSONFormatHandler().serialize(annotation));
        System.out.println(response.getStatus());
        System.out.println(response.getLocation().toString());
        return response.getEntity();
	}

	@Override
	public String updateAnnotation(String annotationId, Annotation annotation)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			AnnotationHasReplyException, InvalidAnnotationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAnnotation(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			AnnotationHasReplyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnnotationTree findAnnotationsForObject(String objectUri)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countAnnotationsForObject(String objectUri)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Annotation> findAnnotationsForUser(String username)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Annotation findAnnotationById(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {

		EuropeanaAnnotationAPI api = getAPI();       
		ClientResponse<String> response = api.findAnnotationById(annotationId);
		forwardResponseHeaders(response.getHeaders());
		
		int status = response.getStatus();
		if (status == HttpResponseCodes.SC_OK) {
			try {
				return new JSONFormatHandler().parse(response.getEntity());
			} catch (InvalidAnnotationException e) {
				throw new AnnotationDatabaseException(e);
			}
		} else if (status == HttpResponseCodes.SC_NOT_FOUND) {
			throw new AnnotationNotFoundException();
		} else {
			throw new AnnotationDatabaseException("Error " + response.getStatus() + "\n"
					+ response.getEntity());
		}
	}

	@Override
	public long countReplies(String annotationId)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AnnotationTree getReplies(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> getMostRecent(int n, boolean publicOnly)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> findAnnotations(String query)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	private EuropeanaAnnotationAPI getAPI() {		
		HttpClient client = new HttpClient();
		
		// Forward all cookies from the calling request
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {			
			c.setDomain(request.getServerName());
			c.setPath("/");

			org.apache.commons.httpclient.Cookie apacheCookie =
				new org.apache.commons.httpclient.Cookie(
						c.getDomain(), c.getName(), c.getValue(),
						c.getPath(), c.getMaxAge(), c.getSecure());

			client.getState().addCookie(apacheCookie);			
		}
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
				
		return ProxyFactory.create(EuropeanaAnnotationAPI.class, EUROPEANA_API_URL,
				new ApacheHttpClientExecutor(client));		
	}

	private void forwardResponseHeaders(MultivaluedMap<String, String> headers) {
		if (headers==null) return;
		
		for (String key : headers.keySet()) {
			for (String value : headers.get(key)) {						
				response.addHeader(key, value);
			}
		}
	}

}
