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
	 * get all Media objects created by a specific user<br>
	 * 
	 * <p>
	 * <h5>Example:</h5>
	 * 
	 * <pre>
	 * GET /yumanji/api/media?username=exampleuser HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: another_exampleuser
	 * </pre>
	 * 
 	 * @HTTP 200 ok
	 * @HTTP 404 MediaNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @returnWrapped List&lt;Media&gt; the media objects for a given user
	 * @inputWrapped Media
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
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
	 * create a new Media object, <br>
	 * 
	 * the username passed in the "CheckPermissionsFor"-header will be used as
	 * creator and owner<br>
	 * you can also PUT an empty object, currently its only possible to set the
	 * scope property
	 * 
	 * <p>
	 * <h5>Example:</h5>
	 * 
	 * <pre>
	 * PUT /yumanji/api/media/ HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: exampleuser
	 * Content-Type: application/json
	 * 
	 * {}
	 * </pre>
	 * 
 	 * @HTTP 200 ok
	 * @HTTP 415 InvalidMediaException
	 * @returnWrapped Media the created Media object
	 * @inputWrapped Media
	 * @RequestHeader CheckPermissionsFor create media for the provided username
 	 *
	 * @param media
	 *            the Media object to create
	 * @return the new Media object and will redirect to the URL of the Media
	 *         object
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
	 * Get a Media object by ID<br>
	 * 
	 * <p>
	 * <h5>Example:</h5>
	 * 
	 * <pre>
	 * GET /yumanji/api/media/17 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: another_exampleuser
	 * </pre>
	 * 
 	 * @HTTP 200 ok
	 * @HTTP 404 MediaNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @returnWrapped Media the media object
	 * @inputWrapped Media
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
 	 *
	 * @param id the id of the Media object
	 * @return the Media 
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
	 * updates a Media object <br>
	 * 
	 * currently its only possible to change the scope property
	 * 
	 * <p>
	 * <h5>Example:</h5>
	 * 
	 * <pre>
	 * POST /yumanji/api/media/17 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: exampleuser
	 * Content-Type: application/json
	 * 
	 * {
	 * 	"id": 17,
	 * 	"scope": "PRIVATE"
	 * }
	 * </pre> 
	 * 
 	 * @HTTP 200 ok
	 * @HTTP 415 InvalidMediaException
	 * @HTTP 404 MediaNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @returnWrapped Media the updated Media object
	 * @inputWrapped Media
	 * @RequestHeader CheckPermissionsFor create media for the provided username
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
	 * create a new MediaContentVersion<br>
	 * 
	 * use this to create an actual content for your Media!<br>
	 * to upload the file use the multipart/form-data content-type
	 * 
	 * <p>
	 * <h5>Example:</h5>
	 * 
	 * <pre>
	 * PUT /yumanji/api/media/17/content HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: exampleuser
	 * Content-Type: multipart/form-data; boundary=----ExampleBoundarysKBwBxXzKvp8rA98
	 * 
	 * ------ExampleBoundarysKBwBxXzKvp8rA98
	 * Content-Disposition: form-data; name="file"; filename="Test File.pdf"
	 * Content-Type: application/pdf
	 * 
	 * ------ExampleBoundarysKBwBxXzKvp8rA98--
	 * </pre>
	 * 
 	 * @HTTP 204 created
	 * @HTTP 404 MediaNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @HTTP 500 IOException
	 * @returnWrapped MediaContentVersion the created MediaContentVersion
	 * @inputWrapped InputStream  
	 * @RequestHeader CheckPermissionsFor upload media content version with the provided username
 	 * 
	 * @param mediaId the id of the Media
	 * @param inputStream the file to upload, multipart/form-data param "file"
	 * @param fileInfo the content-disposition file-part (provided by jersey)
	 * @param body the body of the file-part (provided by jersey)
	 * @return the created MediaContentVersion
	 * @throws IOException
	 * @throws MediaNotFoundException
	 * @throws PermissionDeniedException
	 */
	@PUT
	@Path("{id}/content")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createMediaContentVersion(@PathParam("id") Long mediaId,
			@FormDataParam("file") FormDataContentDisposition fileInfo,
			@FormDataParam("file") FormDataBodyPart body,
			@FormDataParam("file") InputStream inputStream) throws IOException,
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
	 * Get the URIs of all MediaContentVersions for a Media<br> 
	 * 
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/media/17/content HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: another_exampleuser
	 * </pre>
	 * 
	 * <h5>Example Response:</h5>
	 * <pre>
	 * [
	 * 	"http://service.tequnix.org/yumanji/api/media/17/content/7/Test+File.pdf",
	 * 	"http://service.tequnix.org/yumanji/api/media/17/content/8/%C3%84nderung_Test_File.pdf"
	 * ]
	 * </pre>
	 * 
 	 * @HTTP 200 ok
	 * @HTTP 404 MediaNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @returnWrapped List&lt;URI&gt; a list of uris to the MediaContentVersions
	 * @RequestHeader CheckPermissionsFor check view permission on Media with the provided username
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
	 * returns the saved Metadata for MediaContentVersion<br>
	 * 
	 * <p>
	 * <h5>Example:</h5>
	 * 
	 * <pre>
	 * GET /yumanji/api/media/17/content/7 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * </pre>
	 * 
 	 * @HTTP 200 ok
	 * @HTTP 404 MediaNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @returnWrapped MediaContentVersion the MediaContentVersion
	 * @RequestHeader CheckPermissionsFor check view permission on Media with the provided username
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
	 * gets the actual file content of a Media <br>
	 * 
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/media/17/content/7/Test+File.pdf HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * </pre>
	 * 
	 * the Content-Type Header of the response will be set depending
	 * on the files actual content type
	 * 
 	 * @HTTP 200 ok
	 * @HTTP 404 MediaNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @returnWrapped Byte[] the binary content of the MediaContentVersion
	 * @RequestHeader CheckPermissionsFor check view permission on Media with the provided username
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
