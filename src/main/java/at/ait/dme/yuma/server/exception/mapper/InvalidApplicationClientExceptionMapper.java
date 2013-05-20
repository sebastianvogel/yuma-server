package at.ait.dme.yuma.server.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.ait.dme.yuma.server.exception.InvalidApplicationClientException;

@Provider
public class InvalidApplicationClientExceptionMapper implements ExceptionMapper<InvalidApplicationClientException> {

	@Override
	public Response toResponse(InvalidApplicationClientException e) {
		return Response.status(Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
	}
	
}
