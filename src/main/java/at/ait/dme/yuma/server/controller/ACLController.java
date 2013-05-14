package at.ait.dme.yuma.server.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;

@Path("ACL")
public class ACLController extends AbstractAnnotationController {
	
	@POST
	@Consumes("application/json")
	@Path("create")
	public Response editAnnotation(String annotation)
		throws AnnotationDatabaseException, InvalidAnnotationException, AnnotationModifiedException {
		
		//return super.createAnnotation(annotation, new JSONFormatHandler());
		return null;
	}
}
