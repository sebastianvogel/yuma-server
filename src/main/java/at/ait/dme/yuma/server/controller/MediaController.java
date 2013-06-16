package at.ait.dme.yuma.server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.service.IMediaService;
import at.ait.dme.yuma.server.util.URIBuilder;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;

/**
 * 
 * @author Sebastian Vogel <s.vogel@gentics.com>
 * 
 */
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

	/**
	 * get all {@link Media} objects created by a specific user
	 * 
	 * <h4>Example:</h4>
	 * 
	 * <pre>
	 * GET /yumanji/api/media?username=exampleuser HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic <CREDENTIALS>
	 * CheckPermissionsFor: another_exampleuser
	 * </pre>
	 * 
	 * @param username
	 *            the username
	 * @return a list of MediaObjects
	 * @throws MediaNotFoundException
	 * @throws PermissionDeniedException
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMediaForUser(@QueryParam("username") String username)
			throws MediaNotFoundException, PermissionDeniedException {
		if (username == null) {
			throw new MediaNotFoundException("No username provided");
		}
		List<Media> resultList = mediaService.findMediaByUsers(username,
				new AuthContext(request));
		return Response.ok().entity(resultList).build();
	}

	/**
	 * create a new {@link Media} object, <br>
	 * the username passed in the "CheckPermissionsFor"-header will be used as
	 * creator and owner<br>
	 * you can also PUT an empty object, currently its only possible to set the
	 * scope property
	 * 
	 * <h4>Example:</h4>
	 * 
	 * <pre>
	 * PUT /yumanji/api/media/ HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic <CREDENTIALS>
	 * CheckPermissionsFor: exampleuser
	 * Content-Type: application/json
	 * 
	 * {}
	 * </pre>
	 * 
	 * @param media
	 *            the {@link Media} object to create
	 * @return the new {@link Media} object and will redirect to the URL of the Media
	 *         object
	 * @throws IOException
	 * @throws InvalidMediaException
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMediaObject(Media media) throws InvalidMediaException {
		Media me = mediaService.createMedia(media, new AuthContext(request));
		return Response
				.created(
						URIBuilder.toURI(me.getId().toString(),
								URISource.MEDIA, false)).entity(me).build();
	}

	/**
	 * Get a {@link Media} object by ID
	 * 
	 * <h4>Example</h4>
	 * 
	 * <pre>
	 * GET /yumanji/api/media/17 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic <CREDENTIALS>
	 * CheckPermissionsFor: another_exampleuser
	 * </pre>
	 * 
	 * @param id the id of the {@link Media} object
	 * @return the {@link Media} 
	 * @throws MediaNotFoundException
	 * @throws PermissionDeniedException
	 */
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMedia(@PathParam("id") Long id)
			throws MediaNotFoundException, PermissionDeniedException {
		Media me = mediaService.findMedia(id, new AuthContext(request));
		return Response.ok().entity(me).build();
	}

	/**
	 * updates a {@link Media} object <br>
	 * currently its only possible to change the scope property
	 * 
	 * <h4>Example:</h4>
	 * 
	 * <pre>
	 * POST /yumanji/api/media/17 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic <CREDENTIALS>
	 * CheckPermissionsFor: exampleuser
	 * Content-Type: application/json
	 * 
	 * {
	 * 	"id": 17,
	 * 	"scope": "PRIVATE"
	 * }
	 * </pre>
	 * 
	 * @param media
	 *            the media object
	 * @return the updated media object
	 * @throws MediaNotFoundException
	 * @throws PermissionDeniedException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response updateMediaObject(Media media)
			throws MediaNotFoundException, PermissionDeniedException {
		Media me = mediaService.updateMedia(media, new AuthContext(request));
		return Response.ok().entity(me).build();
	}

	/**
	 * create a new {@link MediaContentVersion}<br>
	 * use this to create an actual content for your {@link Media}!<br>
	 * to upload the file use the multipart/form-data content-type
	 * 
	 * 
	 * <h4>Example:</h4>
	 * 
	 * <pre>
	 * PUT /yumanji/api/media/ HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic <CREDENTIALS>
	 * CheckPermissionsFor: exampleuser
	 * Content-Type: multipart/form-data; boundary=----WebKitFormBoundarysKBwBxXzKvp8rA98
	 * 
	 * ------WebKitFormBoundarysKBwBxXzKvp8rA98
	 * Content-Disposition: form-data; name="file"; filename="Test File.pdf"
	 * Content-Type: application/pdf
	 * 
	 * ------WebKitFormBoundarysKBwBxXzKvp8rA98--
	 * </pre>
	 * 
	 * @param mediaId the id of the {@link Media}
	 * @param inputStream the file to upload file-part (provided by jersey)
	 * @param fileInfo the content-disposition file-part (provided by jersey)
	 * @param body the body of the file-part (provided by jersey)
	 * @return the created {@link MediaContentVersion}
	 * @throws IOException
	 * @throws MediaNotFoundException
	 * @throws PermissionDeniedException
	 */
	@PUT
	@Path("{id}/content")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMediaContentVersion(@PathParam("id") Long mediaId,
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileInfo,
			@FormDataParam("file") FormDataBodyPart body) throws IOException,
			MediaNotFoundException, PermissionDeniedException {
		String mimeType = body.getMediaType().toString();
		String fileName = fileInfo.getFileName();
		byte[] content = IOUtils.toByteArray(inputStream);
		MediaContentVersion mcv = mediaService.createMediaContentVersion(
				mediaId, new MediaContentVersion(mimeType, fileName, content),
				new AuthContext(request));
		return Response.created(mcv.getUri(false, false)).entity(mcv).build();
	}

	/**
	 * Get the URIs of all {@link MediaContentVersion}s for a {@link Media} 
	 * 
	 * <h4>Example:</h4>
	 * <pre>
	 * GET /yumanji/api/media/17/content HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic <CREDENTIALS>
	 * CheckPermissionsFor: another_exampleuser
	 * </pre>
	 * 
	 * <h4>Example Response:</h4>
	 * <pre>
	 * [
	 * 	"http://service.tequnix.org/yumanji/api/media/17/content/7/Test+File.pdf",
	 * 	"http://service.tequnix.org/yumanji/api/media/17/content/8/%C3%84nderung_Test_File.pdf"
	 * ]
	 * </pre>
	 * 
	 * @param mediaId the Media id
	 * @return a list of URIs
	 * @throws MediaNotFoundException
	 * @throws PermissionDeniedException
	 */
	@GET
	@Path("{id}/content")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listMediaContentVersionUris(@PathParam("id") Long mediaId)
			throws MediaNotFoundException, PermissionDeniedException {
		List<URI> resultList = mediaService.findMediaContentVersionsByMedia(
				mediaId, false, new AuthContext(request));
		return Response.ok().entity(resultList).build();
	}
	/**
	 * returns the saved Metadata for {@link MediaContentVersion}
	 * 
	 * <h4>Example</h4>
	 * 
	 * <pre>
	 * GET /yumanji/api/media/17/content/7 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic <CREDENTIALS>
	 * </pre>
	 * 
	 * @param mediaId the Media id
	 * @param version the version of the MediaContent
	 * @return the Metadata
	 * @throws MediaNotFoundException
	 * @throws PermissionDeniedException
	 */
	@GET
	@Path("{id}/content/{version}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMediaContentVersionInfo(@PathParam("id") Long mediaId,
			@PathParam("version") Long version) throws MediaNotFoundException,
			PermissionDeniedException {
		MediaContentVersion mediaContent = mediaService
				.findMediaContentVersion(mediaId, version, new AuthContext(
						request));
		return Response.ok().entity(mediaContent).build();
	}

	/**
	 * gets the actual file content of a {@link Media} 
	 * 
	 * <h4>Example:</h4>
	 * <pre>
	 * GET /yumanji/api/media/17/content/7/Test+File.pdf HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic <CREDENTIALS>
	 * </pre>
	 * 
	 * the Content-Type Header of the response will be set depending
	 * on the files actual content type
	 * 
	 * @param mediaId the Media id
	 * @param version the version of the MediaContent
	 * @param fileName the urlencoded filename
	 * @return the binary content 
	 * @throws MediaNotFoundException
	 * @throws PermissionDeniedException
	 */
	@GET
	@Path("{id}/content/{version}/{filename}")
	public Response getMediaContentVersionFile(@PathParam("id") Long mediaId,
			@PathParam("version") Long version,
			@PathParam("filename") String fileName)
			throws MediaNotFoundException, PermissionDeniedException {
		try {
			fileName = URLDecoder.decode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// this should not happen!!
		}
		MediaContentVersion mediaContent = mediaService
				.findMediaContentVersion(mediaId, version, new AuthContext(
						request));
		if (!mediaContent.getFileName().equals(fileName)) {
			throw new MediaNotFoundException("No content found for " + fileName);
		}
		return Response.ok().entity(mediaContent.getContent())
				.type(mediaContent.getMimeType()).build();
	}
}
