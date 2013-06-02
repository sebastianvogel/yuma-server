package at.ait.dme.yuma.server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.exception.InvalidMediaException;
import at.ait.dme.yuma.server.exception.MediaNotFoundException;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Media;
import at.ait.dme.yuma.server.model.MediaContentVersion;
import at.ait.dme.yuma.server.service.IMediaService;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("media")
public class MediaController {
	
	private IMediaService mediaService;
	
	@Context
	protected HttpServletRequest request;
	
	@Context
	protected HttpServletResponse response;
	
	public MediaController() {
		mediaService = Config.getInstance().getMediaService();
	}
	
	@GET
	public Response getMediaForUser(String username) throws MediaNotFoundException, PermissionDeniedException {
		if (username == null) {
			throw new MediaNotFoundException("No username provided");
		}
		List<Media> resultList = mediaService.findMediaByUsers(username, new AuthContext(request));
		return Response.ok().entity(resultList).build();
	}
	
	@PUT
	public Response createMediaObject(Media media) throws IOException, InvalidMediaException {
		Media me = mediaService.createMedia(media, new AuthContext(request));
		return Response.ok().entity(me).build();
	}
	
	@POST
	@Path("{id}")
	public Response updateMediaObject(Media media) throws MediaNotFoundException, PermissionDeniedException {
		Media me = mediaService.updateMedia(media, new AuthContext(request));
		return Response.ok().entity(me).build();
	}
	
	@PUT
	@Path("{id}/content")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createMediaContentVersion(
			@PathParam("id") Long mediaId,
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileInfo) throws IOException, MediaNotFoundException, PermissionDeniedException {
		String mimeType = fileInfo.getType();
		String fileName = fileInfo.getFileName();
		byte[] content = IOUtils.toByteArray(inputStream);
		URI uri = mediaService.createMediaContentVersion(
				mediaId, 
				new MediaContentVersion(mimeType, fileName, content),
				new AuthContext(request));
		return Response.ok().entity(uri).build();
	}
	

	@GET
	@Path("{id}/content")
	public Response listMediaContentVersionUris(@PathParam("id") Long mediaId) throws MediaNotFoundException {
		List<URI> resultList = mediaService.findMediaContentVersionsByMedia(mediaId, true);
		return Response.ok().entity(resultList).build();
	}

	
	@GET
	@Path("{id}/content/{version}/{filename}")
	public Response getMediaContentVersion(@PathParam("id") Long mediaId,
			@PathParam("version") Long version,
			@PathParam("filename") String fileName)
			throws MediaNotFoundException {
		MediaContentVersion mediaContent = mediaService
				.findMediaContentVersion(mediaId, version);
		if (!mediaContent.getFileName().equals(fileName)) {
			throw new MediaNotFoundException("No content found for " + fileName);
		}
		return Response.ok().entity(mediaContent.getContent())
				.type(mediaContent.getMimeType()).build();
	}
}
