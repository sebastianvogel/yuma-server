package at.ait.dme.yuma.server.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.ait.dme.yuma.server.exception.AnnotationException;

@Provider
public class AnnotationExceptionMapper implements ExceptionMapper<AnnotationException> {

	@Override
	public Response toResponse(AnnotationException aee) {
		return Response.status(Status.UNSUPPORTED_MEDIA_TYPE).entity(aee.getMessage()).build();
	}
}
