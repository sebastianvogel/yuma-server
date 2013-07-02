package at.ait.dme.yuma.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.service.IACLService;
import at.ait.dme.yuma.server.util.URIBuilder;

@Path("ACL")
public class ACLController {
	
	private static Logger log = Logger.getLogger(ACLController.class);
	
	@Context
	protected HttpServletRequest request;
	
	@Context
	protected HttpServletResponse response;
	
	private IACLService aclService;
	protected static final String URL_ENCODING = "UTF-8";
	JSONFormatHandler format;
	
	public ACLController() {
		aclService = Config.getInstance().getAclService();
		format = new JSONFormatHandler();
	}
	/**
	 * update a given acl for an annotation or media
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 UnsupportedEncodingException
	 * @HTTP 415 InvalidAnnotationException
	 * @inputWrapped Annotation
	 * @RequestHeader CheckPermissionsFor check update permissions for the provided username
	 * 
	 * @param identifier the id of the acl
	 * @param acl the acls JSON representation
	 * @return the Id of the acl
	 * @throws UnsupportedEncodingException
	 * @throws InvalidAnnotationException
	 */
	@POST
	@Consumes("application/json")
	@Path("{id}")
	public Response updateACL(@PathParam("id") String identifier, String acl) 
			throws UnsupportedEncodingException, InvalidAnnotationException {
		
		String curAclId = URLDecoder.decode(identifier, URL_ENCODING);
		Annotation in = format.parse(acl);
		try {
			identifier = aclService.updateACL(curAclId, in, request.getRemoteUser());
			aclService.findACLById(identifier); //check if exists and throw exception otherwise
		} catch(AnnotationNotFoundException anfe) {
			throw new AnnotationDatabaseException(anfe);
		}
		log.info("updated annotation with id=".concat(identifier));
		return Response.ok().entity(identifier.toString()).
				header("Location", URIBuilder.toURI(identifier, URISource.ANNOTATION, false)).build(); 	
	}
	/**
	 * Find the acl to an annotation and return it using the JSON-format<br>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped Annotation the acl in the json format
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param identifier the identifier of the annotation
	 * @return status code 200 and found acl in JSON-format
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Path("annotation/{id}")
	public Response getACLForAnnotation(@PathParam("id") String identifier) 
			throws UnsupportedEncodingException {
		
		ACL acl = null;
		try {
			acl = aclService.findACLByObjectId(
					URLDecoder.decode(identifier, URL_ENCODING), URISource.ANNOTATION);
		} catch (AnnotationNotFoundException e) {
			acl = aclService.createACL(identifier, URISource.ANNOTATION);
		}
		
		if (acl==null) {
			return Response.serverError().build();
		}
		
		String annotation = format.serialize(acl.toAnnotation());
		return Response.ok(annotation).build();	
	}
	/**
	 * Find the acl to an media object and return it using the JSON-format<br>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped Annotation the acl in the json format
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param identifier the identifier of the media object
	 * @return status code 200 and found acl in JSON-format
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Path("media/{id}")
	public Response getACLForMedia(@PathParam("id") String identifier) 
			throws UnsupportedEncodingException {
		
		JSONFormatHandler format = new JSONFormatHandler();
		ACL acl = null;
		try {
			aclService.findACLByObjectId(
				URLDecoder.decode(identifier, URL_ENCODING), URISource.MEDIA);
		} catch (AnnotationNotFoundException e) {
			acl = aclService.createACL(identifier, URISource.ANNOTATION);
		}
		
		if (acl==null) {
			return Response.serverError().build();
		}
		
		String annotation = format.serialize(acl.toAnnotation());
		return Response.ok(annotation).build();		
	}
}
