package at.ait.dme.yuma.server.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.json.JSONFormatHandler;
import at.ait.dme.yuma.server.controller.rdf.SerializationLanguage;
import at.ait.dme.yuma.server.controller.rdf.oac.OACFormatHandler;
import at.ait.dme.yuma.server.controller.rdf.pelagios.NotAPelagiosAnnotationException;
import at.ait.dme.yuma.server.controller.rdf.pelagios.PelagiosFormatHandler;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.MediaNotFoundException;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Annotation;

/**
 * The primary annotation controller which consumes and produces JSON
 * representations of annotations and annotation threads.
 * 
 * @author Rainer Simon
 */
@Path("annotation")
public class AnnotationController extends AbstractAnnotationController {

	public AnnotationController() {
		super.setAnnotationService(Config.getInstance().getAnnotationService());
	}

	/**
	 * Create a new annotation<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * PUT /yumanji/api/annotation HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: exampleuser
	 * Content-Type: application/json
	 * 
	 * { "text": "lorem ipsum", [..] }
	 * </pre>
	 * 
	 * @HTTP 201 created
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 409 AnnotationModifiedException
	 * @HTTP 415 InvalidAnnotationException
	 * @returnWrapped Annotation the updated annotation representation
	 * @inputWrapped Annotation
	 * @RequestHeader CheckPermissionsFor the username with which the annotation is created
	 * 
	 * @param annotation the JSON representation of the annotation
	 * @return status code 201 and new annotation representation
	 * @throws AnnotationDatabaseException (500)
	 * @throws InvalidAnnotationException (415)
	 * @throws AnnotationModifiedException (409)
	 */
	@PUT
	@Consumes("application/json")
	public Response createAnnotation(String annotation)
			throws InvalidAnnotationException, AnnotationModifiedException,
			PermissionDeniedException {

		return super.createAnnotation(annotation, new JSONFormatHandler());
	}

	/**
	 * Update an existing annotation<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * POST /yumanji/api/annotation/123 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: exampleuser
	 * Content-Type: application/json
	 * 
	 * { "text": "lorem ipsum dolor sit amet", [..] }
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 500 UnsupportedEncodingException
	 * @HTTP 409 AnnotationHasReplyException
	 * @HTTP 404 AnnotationNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @HTTP 415 InvalidAnnotationException
	 * @returnWrapped Annotation the updated annotation representation
	 * @inputWrapped Annotation
	 * @RequestHeader CheckPermissionsFor check update permissions for the provided username
	 * 
	 * @param annotationId the annotation ID 
	 * @param annotation the JSON representation of the annotation
	 * @return status code 200 and updated annotation representation
	 * @throws AnnotationDatabaseException (500)
	 * @throws InvalidAnnotationException (415)
	 * @throws AnnotationHasReplyException (409)
	 * @throws UnsupportedEncodingException (500)
	 * @throws PermissionDeniedException 
	 * @throws AnnotationNotFoundException 
	 */
	@POST
	@Consumes("application/json")
	@Path("{id}")
	public Response updateAnnotation(@PathParam("id") String id,
			String annotation) throws UnsupportedEncodingException,
			InvalidAnnotationException, AnnotationHasReplyException,
			AnnotationNotFoundException, PermissionDeniedException {
		return super.updateAnnotation(id, annotation, new JSONFormatHandler());
	}

	/**
	 * Delete an annotation<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * DELETE /yumanji/api/annotation/123 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: exampleuser
	 * </pre>
	 * 
	 * @HTTP 204 no content
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 500 UnsupportedEncodingException
	 * @HTTP 409 AnnotationHasReplyException
	 * @HTTP 404 AnnotationNotFoundException
	 * @HTTP 403 PermissionDeniedException
	 * @RequestHeader CheckPermissionsFor check delete permissions for the provided username
	 * 
	 * @param annotationId the annotation ID
	 * @return status code 204
	 * @throws PermissionDeniedException 
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500)
	 * @throws AnnotationHasReplyException (409)
	 * @throws AnnotationNotFoundException (404)
	 */
	@DELETE
	@Path("{id}")
	public Response deleteAnnotation(@PathParam("id") String id)
			throws UnsupportedEncodingException, AnnotationNotFoundException,
			AnnotationHasReplyException, PermissionDeniedException {

		return super.deleteAnnotation(id);
	}

	/**
	 * Find an annotation by its ID and return it using the JSON-format<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/annotation/123 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: someuser
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped Annotation the annotation in the json format
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param annotationId the annotation ID
	 * @return status code 200 and found annotation in JSON-format
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Produces("application/json")
	@Path("{id}")
	public Response getAnnotation(@PathParam("id") String id)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			UnsupportedEncodingException {

		return super.getAnnotation(id, new JSONFormatHandler());
	}
	/**
	 * Find an annotation by its ID and return it using the OAC-format<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/annotation/123.oac HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: someuser
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped Annotation the annotation in the oac format
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param annotationId the annotation ID
	 * @return status code 200 and found annotation in OAC-format
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Produces("application/rdf+xml")
	@Path("{id:.+\\.oac}")
	public Response getAnnotation_forceOAC(@PathParam("id") String id)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			UnsupportedEncodingException {

		return super.getAnnotation(id.substring(0, id.indexOf('.')),
				new OACFormatHandler());
	}
	
	/**
	 * Find an annotation by its ID and return it using the JSON-format<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/annotation/123.json HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: someuser
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped Annotation the annotation in the json format
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param annotationId the annotation ID
	 * @return status code 200 and found annotation in JSON-format
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Produces("application/json")
	@Path("{id:.+\\.json}")
	public Response getAnnotation_forceJSON(@PathParam("id") String id)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			UnsupportedEncodingException {

		return super.getAnnotation(id.substring(0, id.indexOf('.')),
				new JSONFormatHandler());
	}
	/**
	 * Find an annotation by its ID and return it in its RDF (xml) representation<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/annotation/123.rdf HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: someuser
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped Annotation the annotation in the rdf (xml) format
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param annotationId the annotation ID
	 * @return status code 200 and found annotation in RDF (xml)
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Produces("application/rdf+xml")
	@Path("{id:.+\\.rdf}")
	public Response getAnnotationXML(@PathParam("id") String id)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			UnsupportedEncodingException {

		try {
			return super.getAnnotation(id.substring(0, id.indexOf('.')),
					new PelagiosFormatHandler(SerializationLanguage.RDF_XML));
		} catch (NotAPelagiosAnnotationException e) {
			e.printStackTrace();
			throw new AnnotationNotFoundException();
		}
	}
	/**
	 * Find an annotation by its ID and return it in its RDF (n3) representation<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/annotation/123.n3 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: someuser
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped Annotation the annotation in the rdf (n3) format
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param annotationId the annotation ID
	 * @return status code 200 and found annotation in RDF (n3)
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Produces("text/rdf+n3")
	@Path("{id:.+\\.n3}")
	public Response getAnnotationN3(@PathParam("id") String id)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			UnsupportedEncodingException {

		try {
			return super.getAnnotation(id.substring(0, id.indexOf('.')),
					new PelagiosFormatHandler(SerializationLanguage.N3));
		} catch (NotAPelagiosAnnotationException e) {
			throw new AnnotationNotFoundException();
		}
	}

	/**
	 * Find an annotation by its ID and return it in its turtle representation<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/annotation/123.turtle HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: someuser
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped Annotation the annotation in the turtle format
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param annotationId the annotation ID
	 * @return status code 200 and found annotation in turtle
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Produces("application/x-turtle")
	@Path("{id:.+\\.turtle}")
	public Response getAnnotationTurtle(@PathParam("id") String id)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			UnsupportedEncodingException {

		try {
			return super.getAnnotation(id.substring(0, id.indexOf('.')),
					new PelagiosFormatHandler(SerializationLanguage.TURTLE));
		} catch (NotAPelagiosAnnotationException e) {
			throw new AnnotationNotFoundException();
		}
	}
	/**
	 * Returns the entire tree of annotations for a given object<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/annotation/tree?objectUri=%2Fyumanji%2Fapi%2Fmedia%2F17%2Fcontent%2F7%2FTest%2BFile.pdf HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: someuser
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 415 InvalidAnnotationException
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped List&lt;Annotation&gt; the serialized representation of the found annotations
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param objectUri the URI of the object
	 * @return status code 200 and the representation of the found annotations
	 * @throws AnnotationDatabaseException (500)
	 * @throws InvalidAnnotationException (415)
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Produces("application/json")
	@Path("tree/")
	public Response getAnnotationTree(@QueryParam("objectUri") String objectUri)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			UnsupportedEncodingException {

		return super.getAnnotationTree(objectUri, new JSONFormatHandler());
	}

	/**
	 * Returns the entire tree of annotations for a media<br>
	 * <p>
	 * <h5>Example:</h5>
	 * <pre>
	 * GET /yumanji/api/annotation/media/17/7 HTTP/1.1
	 * Host: service.tequnix.org
	 * Authorization: Basic &lt;CREDENTIALS&gt;
	 * CheckPermissionsFor: someuser
	 * </pre>
	 * 
	 * @HTTP 200 ok
	 * @HTTP 500 AnnotationDatabaseException
	 * @HTTP 415 InvalidAnnotationException
	 * @HTTP 500 UnsupportedEncodingException
	 * @returnWrapped List&lt;Annotation&gt; the serialized representation of the found annotations
	 * @RequestHeader CheckPermissionsFor check view permissions for the provided username
	 * 
	 * @param mediaId the id of the media
	 * @param version the version of the media content
	 * @return status code 200 and the representation of the found annotations
	 * @throws AnnotationDatabaseException (500)
	 * @throws InvalidAnnotationException (415)
	 * @throws UnsupportedEncodingException (500)
	 */
	@GET
	@Produces("application/json")
	@Path("media/{id}/{version}")
	public Response getAnnotationsForMedia(@PathParam("id") String mediaId, @PathParam("version") String version)
		throws AnnotationNotFoundException, UnsupportedEncodingException, 
		NumberFormatException, MediaNotFoundException, PermissionDeniedException {
		
		return super.getAnnotationTreeForMedia(mediaId, version, new JSONFormatHandler());
	}

}