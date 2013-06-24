package at.ait.dme.yuma.server.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.ait.dme.yuma.server.exception.PermissionDeniedException;

@Provider
public class PermissionDeniedExceptionMapper 
	implements ExceptionMapper<PermissionDeniedException> {

	@Override
	public Response toResponse(PermissionDeniedException e) {
		return Response.status(Status.FORBIDDEN).entity(e.getMessage()).build();
	}
}
