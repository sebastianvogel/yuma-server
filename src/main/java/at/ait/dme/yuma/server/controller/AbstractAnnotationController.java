package at.ait.dme.yuma.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;
import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.service.IAnnotationService;
import at.ait.dme.yuma.server.util.URIBuilder;

/**
 * This class contains the default annotation controller logic.
 * 
 * @author Christian Sadilek
 * @author Rainer Simon 
 */
public abstract class AbstractAnnotationController {
	
	protected static final String URL_ENCODING = "UTF-8";
	private static Logger log = Logger.getLogger(AbstractAnnotationController.class);
	
	@Context
	protected HttpServletRequest request;
	
	@Context
	protected HttpServletResponse response;
	
	IAnnotationService annotationService;
	
	/**
	 * set annotation service
	 * @param service
	 */
	public void setAnnotationService(IAnnotationService service) {
		this.annotationService = service;
	}

	/**
	 * Create a new annotation
	 * @param annotation the JSON representation of the annotation
	 * @return status code 201 and new annotation representation
	 * @throws AnnotationDatabaseException (500)
	 * @throws InvalidAnnotationException (415)
	 * @throws AnnotationModifiedException (409)
	 */
	protected Response createAnnotation(String annotation, FormatHandler format)
		throws AnnotationDatabaseException, InvalidAnnotationException, AnnotationModifiedException {
		
		String annotationId = annotationService.createAnnotation(format.parse(annotation), request.getRemoteUser());
		log.info("created annotation with id=".concat(annotationId));
		return Response.created(URIBuilder.toURI(annotationId, URISource.ANNOTATION, false)).entity(annotationId).build();
	}
	
	/**
	 * Find an annotation by its ID
	 * @param annotationId the annotation ID
	 * @return status code 200 and found annotation
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500
	 */
	protected Response getAnnotation(String annotationId, FormatHandler format)
		throws AnnotationDatabaseException, AnnotationNotFoundException, UnsupportedEncodingException {
			
		annotationId = URLDecoder.decode(annotationId, URL_ENCODING);
		Annotation annotation = null;
		try {
			annotation = annotationService.findAnnotationById(annotationId, getClient(), getUsername());
		} catch (PermissionDeniedException e) {
			return Response.status(Status.FORBIDDEN).build();
		}
		if (annotation==null) {
			return Response.noContent().build();
		} else {
			return Response.ok(format.serialize(annotation)).build();
		}
		
	}
	
	/**
	 * Update an existing annotation
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
	protected Response updateAnnotation(String annotationId, String annotation, FormatHandler format) 
			throws InvalidAnnotationException, UnsupportedEncodingException,
				AnnotationHasReplyException, AnnotationNotFoundException, PermissionDeniedException	 {
		
		String annotationIdDec = URLDecoder.decode(annotationId, URL_ENCODING);
		Annotation in = format.parse(annotation);
		annotationId = annotationService.updateAnnotation(annotationIdDec, in, request.getRemoteUser());
		String username  = in.getCreatedBy()==null ? null : in.getCreatedBy().getUsername();
		try {
			//check if exists and throw exception otherwise
			annotationService.findAnnotationById(annotationId, getClient(), username); 
		} catch (PermissionDeniedException e) {
			return Response.status(Status.FORBIDDEN).build();
		}
		log.info("updated annotation with id=".concat(annotationId));
		return Response.ok().entity(annotationId.toString()).
				header("Location", URIBuilder.toURI(annotationId, URISource.ANNOTATION, false)).build(); 
	}
	
	/**
	 * Delete an annotation
	 * @param annotationId the annotation ID
	 * @return status code 204
	 * @throws PermissionDeniedException 
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500)
	 * @throws AnnotationHasReplyException (409)
	 * @throws AnnotationNotFoundException (404)
	 */
	protected Response deleteAnnotation(String annotationId) 
			throws UnsupportedEncodingException, AnnotationNotFoundException, 
			AnnotationHasReplyException, PermissionDeniedException {
		
		annotationService.deleteAnnotation(
				URLDecoder.decode(annotationId, URL_ENCODING), getClient(), getUsername());
		log.info("deleted annotation with id=".concat(annotationId));
		return Response.noContent().build();
	}
	
	/**
	 * Retrieve the thread which contains the given annotation
	 * @param annotationId the annotation ID
	 * @return status code 200 and representation of the annotation thread
	 * @throws PermissionDeniedException 
	 * @throws AnnotationNotFoundException 
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500
	 */
	protected Response getReplies(String annotationId, FormatHandler format) 
			throws UnsupportedEncodingException, AnnotationNotFoundException, PermissionDeniedException {
		
		String thread = format.serialize(annotationService.getReplies(URLDecoder.decode(annotationId, URL_ENCODING)));
		return Response.ok().entity(thread).build();
	}
	
	/**
	 * Returns the entire tree of annotations for a given object
	 * @param objectId the object ID
	 * @return status code 200 and the representation of the found annotations
	 * @throws AnnotationDatabaseException (500)
	 * @throws InvalidAnnotationException (415)
	 * @throws UnsupportedEncodingException (500)
	 */
	protected Response getAnnotationTree(String objectId, FormatHandler format)
		throws AnnotationDatabaseException, UnsupportedEncodingException {
		
		AnnotationTree tree = annotationService.findAnnotationsForObject(
				URLDecoder.decode(objectId, URL_ENCODING), getClient(), getUsername());
		String ret = tree==null ? null : format.serialize(tree);
		return Response.ok().entity(ret).build();
	}
	
	/**
	 * Retrieves the number of annotations for the given object
	 * @param objectId the object ID
	 * @return status code and count representation
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500
	 */
	protected Response countAnnotationsForObject(String objectId)
		throws AnnotationDatabaseException, UnsupportedEncodingException {
		
		long count = annotationService.countAnnotationsForObject(URLDecoder.decode(objectId, URL_ENCODING));
		return Response.ok().entity(count).build();
	}
	
	protected Response getAnnotationsForUser(String username, FormatHandler format)
		throws AnnotationDatabaseException, UnsupportedEncodingException {
		
		String annotations = format.serialize(
				annotationService.findAnnotationsForUser(URLDecoder.decode(username, URL_ENCODING)));
		return Response.ok().entity(annotations).build();	
	}
	
	protected Response getMostRecent(int n, FormatHandler format) 
		throws AnnotationDatabaseException, UnsupportedEncodingException {
		
		List<Annotation> list = annotationService.getMostRecent(n, true);
		if (list==null) {
			return Response.noContent().build();
		}
		
		String mostRecent = format.serialize(list);
		return Response.ok().entity(mostRecent).build();
	}
				
	/**
	 * Find annotations that match the given search term
	 * @param query the query term
	 * @return status code 200 and found annotations
	 * @throws AnnotationDatabaseException (500)
	 * @throws UnsupportedEncodingException (500
	 */
	protected Response searchAnnotations(String query, FormatHandler format)
		throws AnnotationDatabaseException, UnsupportedEncodingException {
		
		String annotations = format.serialize(
				annotationService.findAnnotations(URLDecoder.decode(query, URL_ENCODING)));
		return Response.ok(annotations).build();
	}
	
	protected String getClient() {
		return request.getRemoteUser();
	}
	
	/**
	 * retrieve username
	 * @return
	 */
	protected String getUsername() {
		 return request.getHeader(Config.HeaderCheckReadPermissionsFor);
	}
}
