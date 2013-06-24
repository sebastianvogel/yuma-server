package at.ait.dme.yuma.server.service;

import java.util.List;

import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.MediaNotFoundException;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * Base class for annotation databases.
 * <p>
 * Note on concurrent modifications: We use an optimistic locking strategy.
 * <p>
 * Since only the author of an annotation is allowed to update and delete we do
 * not have to handle concurrent modifications. If any client allowed users to
 * have more than one active session, updates could be lost but we do not
 * consider this to be a problem since the most recent update would be effective.
 * <p>
 * What we do have to handle is annotation creation with interfering/concurrent
 * updates/removals. If a user creates an annotation with a reference to
 * another annotation it has to be ensured that the referenced annotation has
 * not been changed in the time between the last read (see {@link AnnotationModifiedException}). 
 * Further, on update and delete it has to be ensured that the corresponding annotation 
 * is still unreferenced (see {@link AnnotationHasReplyException}). A referenced
 * annotation cannot be updated or deleted.
 *
 * @author Christian Sadilek
 * @author Rainer Simon
 */
public interface IAnnotationService {	
	
	/**
	 * Create a new annotation
	 * @param annotation the annotation
	 * @param auth
	 * @return the ID of the new annotation
	 * @throws AnnotationDatabaseException if anything goes wrong
	 * @throws AnnotationModifiedException if the parent annotation was modified in the mean time
	 */
	 String createAnnotation(Annotation annotation, AuthContext auth) 
			throws AnnotationModifiedException, PermissionDeniedException;

	/**
	 * Update an annotation
	 * @param annotationId the ID of the annotation
	 * @param annotation the annotation
	 * @param auth
	 * @return the (new) annotation ID after the update (may change depending on DB implementation!)
	 * @throws AnnotationDatabaseException if anything goes wrong
	 * @throws AnnotationNotFoundException if there is no annotation with the given ID
	 * @throws AnnotationHasReplyException if this annotation has already been replied to
	 */
	 String updateAnnotation(String annotationId, Annotation annotation, AuthContext auth)
		throws AnnotationHasReplyException, AnnotationNotFoundException, PermissionDeniedException;

	/**
	 * Delete an annotation
	 * @param annotationId the annotation ID
	 * @param auth
	 * @throws AnnotationDatabaseException  if anything goes wrong
	 * @throws AnnotationNotFoundException if the annotation does not exist in the DB
	 * @throws AnnotationHasReplyException if this annotation has already been replied to
	 */
	 void deleteAnnotation(String annotationId, AuthContext auth)
		throws AnnotationNotFoundException, AnnotationHasReplyException, PermissionDeniedException;

	/**
	 * Returns all annotations for a given object
	 * @param objectUri the object URI
	 * @param auth
	 * @return the annotation tree for the object
	 * @throws AnnotationDatabaseException if anything goes wrong
	 */
	 AnnotationTree findAnnotationsForObject(String objectUri, AuthContext auth) throws AnnotationDatabaseException;
	 
	 /**
	  * Returns all annotations for given media id
	  * @param mediaId
	  * @param auth
	  * @return
	  * @throws AnnotationDatabaseException
	  */
	 AnnotationTree findAnnotationsForMedia(String mediaId, String version, AuthContext auth) 
			 throws MediaNotFoundException, NumberFormatException, PermissionDeniedException;

	/**
	 * Retrieves the number of annotations for the given object
	 * @param objectUri the object ID
	 * @return the number of annotations for this object
	 * @throws AnnotationDatabaseException if anything goes wrong
	 */
	 long countAnnotationsForObject(String objectUri)throws AnnotationDatabaseException; 
		
	/**
	 * Retrieves the annotations for the given user
	 * @param username the user name
	 * @param auth
	 * @return the annotations
	 * @throws AnnotationDatabaseException if anything goes wrong
	 */
	 List<Annotation> findAnnotationsForUser(String username, AuthContext auth)	throws AnnotationDatabaseException;

	/**
	 * Retrieve an annotation by ID
	 * @param annotationId the annotation ID
	 * @param auth
	 * @return the annotation
	 * @throws AnnotationDatabaseException if anything goes wrong
	 * @throws AnnotationNotFoundException if the annotation was not found
	 */
	 Annotation findAnnotationById(String annotationId, AuthContext auth) 
			 throws AnnotationNotFoundException, PermissionDeniedException;
	
	/**
	 * Retrieve the replies for the given annotation
	 * @param annotationId the annotation ID
	 * @return the replies
	 * @throws AnnotationDatabaseException if anything goes wrong
	 * @throws AnnotationNotFoundException if the annotation was not found
	 */
	 AnnotationTree getReplies(String annotationId, AuthContext auth) throws AnnotationNotFoundException, PermissionDeniedException;

	/**
	/**
	 * Retrieves the N most recent annotations from the database.
	 * @param n the number of annotations to retrieve
	 * @param publicOnly if true, only annotations with public scope will be returned
	 * @return the annotations (as a flat list)
	 * @throws AnnotationDatabaseException if anything goes wrong
	 */
	 List<Annotation> getMostRecent(int n, boolean publicOnly, AuthContext auth)	throws AnnotationDatabaseException;

	
	/**
	 * Find annotations that match the given search term
	 * @param query the query term
	 * @return the list of matching annotations
	 * @throws AnnotationDatabaseException if anything goes wrong
	 */
	 List<Annotation> findAnnotations(String query, AuthContext auth) throws AnnotationDatabaseException;
	
}