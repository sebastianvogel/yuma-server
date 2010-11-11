package at.ait.dme.yuma.server.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;

@Provider
public class AnnotationAlreadyReferencedExceptionMapper 
	implements ExceptionMapper<AnnotationHasReplyException> {

	@Override
	public Response toResponse(AnnotationHasReplyException aare) {
		return Response.status(Status.CONFLICT).entity(aare.getMessage()).build();
	}
}
