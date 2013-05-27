package at.ait.dme.yuma.server.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wicket.util.io.IOUtils;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.exception.MediaObjectNotFoundException;
import at.ait.dme.yuma.server.model.MediaObject;
import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.model.User;
import at.ait.dme.yuma.server.service.IMediaObjectService;
import at.ait.dme.yuma.server.util.URIBuilder;

@Path("media")
public class MediaObjectController {
	
	private IMediaObjectService mediaObjectService;
	
	@Context
	protected HttpServletRequest request;
	
	@Context
	protected HttpServletResponse response;
	
	public MediaObjectController() {
		mediaObjectService = Config.getInstance().getMediaObjectService();
	}
	
	@GET
	@Path("{username}")
	public Response getMediaObjectsForUser(@PathParam("username") String username) {
		return null;
	}
	
	@PUT
	@Path("{username}")
	public Response createMediaObject(@PathParam("username") String username) throws IOException {
		String mimeType = request.getContentType();
		byte[] content = IOUtils.toByteArray(request.getInputStream());
		String hash = DigestUtils.md5Hex(content);
		User user = new User(username);
		MediaObject mediaObject = new MediaObject(username + "/" + hash, user, mimeType, content);
		String uri = mediaObjectService.createMediaObject(mediaObject, request.getRemoteUser());
		return Response.ok().entity(uri.toString()).
				header("Location", URIBuilder.toURI(uri, URISource.MEDIA)).build();
	}
	@GET
	@Path("{username}/{hash}")
	public Response getMediaObject(@PathParam("username") String username, @PathParam("hash") String hash) throws MediaObjectNotFoundException {
		MediaObject mediaObject = mediaObjectService.findMediaObjectByUri(username + "/" + hash);
		return Response.ok(mediaObject.getContent()).type(mediaObject.getMimeType()).build();
	}
}
