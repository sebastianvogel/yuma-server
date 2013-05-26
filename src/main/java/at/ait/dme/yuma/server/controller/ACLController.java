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
				header("Location", URIBuilder.toURI(identifier, URISource.ANNOTATION)).build(); 	
	}
	
	@GET
	@Path("annotation/{id}")
	public Response getACLForAnnotation(@PathParam("id") String identifier) 
			throws AnnotationNotFoundException, UnsupportedEncodingException {
		
		String annotation = format.serialize(aclService.findACLByObjectId(
				URLDecoder.decode(identifier, URL_ENCODING), URISource.ANNOTATION));
		return Response.ok(annotation).build();	
	}
	
	@GET
	@Path("media/{id}")
	public Response getACLForMedia(@PathParam("id") String identifier) 
			throws AnnotationNotFoundException, UnsupportedEncodingException {
		
		JSONFormatHandler format = new JSONFormatHandler();
		String annotation = format.serialize(aclService.findACLByObjectId(
				URLDecoder.decode(identifier, URL_ENCODING), URISource.MEDIA));
		return Response.ok(annotation).build();		
	}
}
