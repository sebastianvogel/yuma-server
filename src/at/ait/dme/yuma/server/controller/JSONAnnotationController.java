package at.ait.dme.yuma.server.controller;

import java.io.UnsupportedEncodingException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationFormatException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;

/**
 * Annotation controller that works with JSON.
 *  
 * @author Rainer Simon
 */
@Path("/json")
public class JSONAnnotationController extends AbstractAnnotationController {

	@POST
	@Consumes("application/json")
	@Override
	public Response createAnnotation(String annotation)
		throws AnnotationDatabaseException, AnnotationFormatException, AnnotationModifiedException {
		
		return super.createAnnotation(annotation);
	}
	
	@GET
	@Produces("application/json")
	@Path("/{id}")
	@Override	
	public Response findAnnotationById(@PathParam("id") String id)
		throws AnnotationDatabaseException, AnnotationNotFoundException, UnsupportedEncodingException {
		
		return super.findAnnotationById(id);
	}
	
	@PUT
	@Consumes("application/json")
	@Path("/{id}")
	@Override
	public Response updateAnnotation(@PathParam("id") String id, String annotation) 
			throws AnnotationDatabaseException, AnnotationFormatException, AnnotationHasReplyException, UnsupportedEncodingException {
		
		return super.updateAnnotation(id, annotation);
	}
	
	@DELETE
	@Path("/{id}")
	@Override
	public Response deleteAnnotation(@PathParam("id") String id) 
			throws AnnotationDatabaseException, AnnotationHasReplyException, UnsupportedEncodingException {
		
		return super.deleteAnnotation(id);
	}
	
}
