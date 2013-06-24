package at.ait.dme.yuma.server.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import at.ait.dme.yuma.server.exception.MediaNotFoundException;

@Provider
public class MediaNotFoundExceptionMapper 
	implements ExceptionMapper<MediaNotFoundException> {

	@Override
	public Response toResponse(MediaNotFoundException e) {
		return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
	}
}
