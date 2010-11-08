package at.ait.dme.yuma.server.db.fast;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.jboss.resteasy.util.HttpResponseCodes;

import at.ait.dme.yuma.server.config.Config;

/**
 * Helper class to setup FAST (see Report M5.5 Integration of the
 * annotation middleware and the FAST Annotation Service).
 * 
 * @author Christian Sadilek
 */
public class FastSetup {	
	private static class FastSetupException extends Exception {
		private static final long serialVersionUID = 7414326554396816421L;

		public FastSetupException(String message) {
			super(message);
		}		
	}
	
	public static void main(String[] args) {
		try {		    
			FastSetup setup = new FastSetup();
			
			// create namespaces 
			Namespace nsArc = new Namespace(
					"http://dme.arcs.ac.at/annotation-middleware/Annotation-t0",
					"arc-t0","Annotation Namespace of ARC for TELplus");
			setup.createNamespace(nsArc);
			
			Namespace nsTel = new Namespace("http://www.theeuropeanlibrary.org/telplus-t0",
					"telplus-t0","Namespace of the TELplus project");
			nsTel.addTrustedNamespace("http://fast.dei.unipd.it");
			nsTel.addTrustedNamespace("http://dme.arcs.ac.at/annotation-middleware/Annotation-t0");		
			setup.createNamespace(nsTel);
			
			// can not be created otherwise due to the circular reference of the 
			// tel and annotation middleware namespace
			setup.addTrustedNamespace(nsArc.getId(), nsTel);
			setup.addTrustedNamespace(nsArc.getId(), 
					new Namespace("http://fast.dei.unipd.it","fast",".."));
			
			// create meaning
			Meaning meaning = new Meaning("annotea-t0","http://www.theeuropeanlibrary.org/telplus-t0",
					"The content of the annotation is expressed based on the Annotea" +
					"annotation model, see http://www.w3.org/2001/Annotea");
			setup.createMeaning(meaning);
			
			// create user
			User user = new User("telplus-t0","TELplus project default user","dummypwd",
					"ferro@dei.unipd.it","NL","en","http://www.theeuropeanlibrary.org/telplus-t0");
			setup.createUser(user);
			
			System.out.println("namespaces:\n" + setup.listNamespaces());
			System.out.println("meanings:\n" + setup.listMeanings());
			System.out.println("users:\n" + setup.listUsers());
			
			} catch(FastSetupException e) {
			System.out.println("error:"+e.getMessage());
		}
	}
	
	// namespace
	
	public String listNamespaces() throws FastSetupException {
		return processResponse(getFastService().listNamespaces(), HttpResponseCodes.SC_OK);
	}

	public String retrieveNamespace(String nsId) throws FastSetupException {
		return processResponse(getFastService().retrieveNamespace(nsId), HttpResponseCodes.SC_OK);
	}
		
	public String createNamespace(Namespace ns) throws FastSetupException {		
		return processResponse(getFastService().createNamespace(ns.toString()), 
				HttpResponseCodes.SC_CREATED);
	}
	
	public String addTrustedNamespace(String nsId, Namespace ns) throws FastSetupException {	
		return processResponse(getFastService().addTrustedNamespace(nsId,ns.toString()), 
				HttpResponseCodes.SC_CREATED);
	}
	
	public String deleteNamespace(String nsId) throws FastSetupException {	
		return processResponse(getFastService().deleteNamespace(nsId), 
				HttpResponseCodes.SC_OK);
	}
	
	// meaning
	
	public String listMeanings() throws FastSetupException {
		return processResponse(getFastService().listMeanings(), HttpResponseCodes.SC_OK);
	}

	// don't forget to add the nsId to the meaningId (meaningId;nsId)
	public String retrieveMeaning(String meaningId) throws FastSetupException {
		return processResponse(getFastService().retrieveMeaning(meaningId), 
				HttpResponseCodes.SC_OK);
	}
		
	public String createMeaning(Meaning meaning) throws FastSetupException {		
		return processResponse(getFastService().createMeaning(meaning.toString()), 
				HttpResponseCodes.SC_CREATED);
	}
	
	// don't forget to add the nsId to the meaningId (meaningId;nsId)
	public String deleteMeaning(String meaningId) throws FastSetupException {	
		return processResponse(getFastService().deleteMeaning(meaningId), 
				HttpResponseCodes.SC_OK);
	}
	
	// user
	
	public String listUsers() throws FastSetupException {
		return processResponse(getFastService().listUsers(), HttpResponseCodes.SC_OK);
	}

	// don't forget to add the nsId to the userId (userId;nsId)
	public String retrieveUser(String userId) throws FastSetupException {
		return processResponse(getFastService().retrieveUser(userId), 
				HttpResponseCodes.SC_OK);
	}
		
	public String createUser(User user) throws FastSetupException {		
		return processResponse(getFastService().createUser(user.toString()), 
				HttpResponseCodes.SC_CREATED);
	}
	
	// don't forget to add the nsId to the userId (userId;nsId)
	public String deleteUser(String userId) throws FastSetupException {	
		return processResponse(getFastService().deleteUser(userId), 
				HttpResponseCodes.SC_OK);
	}
	
	private String processResponse(ClientResponse<String> response ,int expectedResponseCode)
		throws FastSetupException {
		
		if(response.getStatus()!=expectedResponseCode) {
			throw new FastSetupException(response.getEntity());
		}
		return response.getEntity();
	}
	
	private FastService getFastService() {
		HttpClient client = new HttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials(
				"fast;http%3A%2F%2Ffast.dei.unipd.it%2F", "dummypwd");
		client.getState().setCredentials(new AuthScope(AuthScope.ANY_HOST, 
				AuthScope.ANY_PORT, AuthScope.ANY_REALM), defaultcreds);
			
		return ProxyFactory.create(FastService.class, 
				Config.getInstance().getDbHost()+":"+
				Config.getInstance().getDbPort(),
				new ApacheHttpClientExecutor(client));
	}
}
