package at.ait.dme.yuma.server.db.fast;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.client.ClientResponse;

/**
 * RESTeasy client interface to FAST
 * 
 * @author Christian Sadilek
 */
@Path("")
public interface FastService {

	// methods to manage namespaces
	
	@GET
	@Produces("application/xml")
	@Path("/namespace")
	public ClientResponse<String> listNamespaces();
	
	@GET
	@Produces("application/xml")
	@Path("/namespace/{ns-id}")
	public ClientResponse<String> retrieveNamespace(@PathParam("ns-id") String nsId);
	
	@POST
	@Consumes("application/xml")
	@Path("/namespace")
	public ClientResponse<String> createNamespace(String namespace);

	@POST
	@Consumes("application/xml")	
	@Path("/namespace/{ns-id}")
	public ClientResponse<String> addTrustedNamespace(@PathParam("ns-id") String nsId, 
			String trustedNamespace);

	@DELETE
	@Path("/namespace/{ns-id}")
	public ClientResponse<String> deleteNamespace(@PathParam("ns-id") String nsId);	
	
	// methods to manage annotations
	
	@GET
	@Produces("application/xml")
	@Path("/annotation")
	public ClientResponse<String> listAnnotations();
	
	@GET
	@Produces("application/xml")
	@Path("/annotation/{ann-id}")
	public ClientResponse<String> retrieveAnnotation(@PathParam("ann-id") @Encoded String annId);
	
	@POST
	@Consumes("application/xml")    
	@Path("/annotation")
	public ClientResponse<String> createAnnotation(String annotation);

	@PUT
	@Consumes("application/xml")
	@Path("/annotation/{ann-id}")
	public ClientResponse<String> updateAnnotation(@PathParam("ann-id") @Encoded  String annId, 
			String anntotation);
	
	@DELETE
	@Path("/annotation/{ann-id}")
	public ClientResponse<String> deleteAnnotation(@PathParam("ann-id") @Encoded String annId);
	
	// methods to manage meanings
	
	@GET
	@Produces("application/xml")
	@Path("/meaning")
	public ClientResponse<String> listMeanings();
	
	@GET
	@Produces("application/xml")
	@Path("/meaning/{meaning-id}")
	public ClientResponse<String> retrieveMeaning(@PathParam("meaning-id") 
			@Encoded String meaningId);
	
	@POST
	@Consumes("application/xml")
	@Path("/meaning")
	public ClientResponse<String> createMeaning(String meaning);


	@DELETE
	@Path("/meaning/{meaning-id}")
	public ClientResponse<String> deleteMeaning(@PathParam("meaning-id") 
			@Encoded String meaningId);	
	
	// methods to manager users
	
	@GET
	@Produces("application/xml")
	@Path("/user")
	public ClientResponse<String> listUsers();
	
	@GET
	@Produces("application/xml")
	@Path("/user/{user-id}")
	public ClientResponse<String> retrieveUser(@PathParam("user-id") @Encoded 
			String userId);
	
	@POST
	@Consumes("application/xml")
	@Path("/user")
	public ClientResponse<String> createUser(String user);

	@DELETE
	@Path("/user/{user-id}")
	public ClientResponse<String> deleteUser(@PathParam("user-id") @Encoded 
			String userId);	
	
	// methods to search
	
	@GET
	@Produces("application/xml")
	@Path("/search")
	public ClientResponse<String> search(@QueryParam("query") @Encoded String query);
	
}
