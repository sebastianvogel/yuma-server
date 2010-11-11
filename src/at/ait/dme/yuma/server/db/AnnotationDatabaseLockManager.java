package at.ait.dme.yuma.server.db;

import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationLockedException;

/**
 * Used by annotation databases to implement optimistic locking on
 * a per annotation base.
 * 
 * @author Christian Sadilek
 */
public interface AnnotationDatabaseLockManager {
	
	/**
	 * Acquire lock for the given annotation
	 * @param annotationId the annotation ID
	 * @param timeout timeout in milliseconds
	 * @return the locked annotation ID
	 * @throws AnnotationDatabaseException if anything goes wrong
	 * @throws AnnotationLockedException if the lock could not be acquired within the given time
	 */
	public String acquireLock(String annotationId, long timeout)
		throws AnnotationDatabaseException, AnnotationLockedException;
	
	/**
	 * Acquire lock for the given annotation
	 * @param annotationId the annotation ID
	 * @return true in case lock was created, false otherwise
	 */	
	public boolean tryAcquireLock(String annotationId);
		
	/**
	 * Release lock for the given annotation
	 * @param annotationId the annotation ID
	 * @throws AnnotationDatabaseException if anything goes wrong
	 */
	public void releaseLock(String annotationId)
		throws AnnotationDatabaseException;
	
	/**
	 * Set the connection this instance is working on
	 * @param connection the connection
	 * @throws AnnotationDatanaseException if anything goes wrong
	 */
	public void setConnection(Object connection)
		throws AnnotationDatabaseException;
	
	/**
	 * Close the underlying connection
	 * @throws AnnotationDatabaseException if anything goes wrong
	 */
	public void closeConnection()
		throws AnnotationDatabaseException;
	
}
