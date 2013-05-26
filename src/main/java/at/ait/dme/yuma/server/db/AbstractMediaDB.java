package at.ait.dme.yuma.server.db;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.ait.dme.yuma.server.model.MediaObject;
import at.ait.dme.yuma.server.exception.YumaDatabaseException;
import at.ait.dme.yuma.server.exception.MediaObjectModifiedException;
import at.ait.dme.yuma.server.exception.MediaObjectNotFoundException;
import at.ait.dme.yuma.server.exception.InvalidMediaObjectException;

/**
 * Base class for media object databases.
 * <p>
 * Note on concurrent modifications: We use an optimistic locking strategy.
 * <p>
 * Since only the creator of a media object  is allowed to update and delete we do
 * not have to handle concurrent modifications. If any client allowed users to
 * have more than one active session, updates could be lost but we do not
 * consider this to be a problem since the most recent update would be effective.
 *
 * @author Sebastian Vogel
 */
public abstract class AbstractMediaDB {	
	
	protected static final String UNEXPECTED_RESPONSE = "unexpected response";		
	protected static final String FAILED_TO_READ_ANNOTATION = "failed to read media object";	
	protected static final String FAILED_TO_SAVE_ANNOTATION = "failed to save media object";
	protected static final String FAILED_TO_DELETE_ANNOTATION = "failed to delete media object";
	protected static final String FAILED_TO_PARSE_ANNOTATION = "failed to parse media object";

	private boolean autoCommit = true;
	
	/**
	 * Check if auto commit is on
	 * @return true if auto commit is on
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}
	
	/**
	 * Set the auto commit mode
	 * @param autoCommit auto commit mode
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}
	
	/**
	 * Initialize 
	 * @throws AnnotationDabaseException if anything goes wrong
	 */
	public abstract void init()
		throws YumaDatabaseException;
	
	/**
	 * Shutdown 
	 */
	public abstract void shutdown();
	
	/**
	 * Connect to the DB
	 * @throws YumaDatabaseException if anything goes wrong
	 */
	public void connect()
		throws YumaDatabaseException {
		
		connect(null, null);
	}

	/**
	 * Connect to the DB and provide a request object to access
	 * parameters, cookies, etc.
	 * @throws YumaDatabaseException if anything goes wrong
	 */
	public abstract void connect(HttpServletRequest request, HttpServletResponse response)
		throws YumaDatabaseException;
	
	/**
	 * Disconnect the DB 
	 * @throws YumaDatabaseException if anything goes wrong
	 */
	public abstract void disconnect();

	/**
	 * Commit all changes
	 * @throws YumaDatabaseException if anything goes wrong
	 */
	public abstract void commit()
		throws YumaDatabaseException;
	
	/**
	 * Roll back all changes
	 * @throws YumaDatabaseException if anything goes wrong
	 */
	public abstract void rollback()
		throws YumaDatabaseException;
	
	/**
	 * Create a new MediaObject
	 * @param mediaObject the media object
	 * @return the URI of the new media object
	 * @throws YumaDatabaseException if anything goes wrong
	 */
	public abstract String createMediaObject(MediaObject mediaObject) 
			throws YumaDatabaseException, MediaObjectModifiedException, InvalidMediaObjectException;

	/**
	 * Update a media object
	 * @param uri the URI of the media object
	 * @param mediaObject the mediaObject
	 * @return the (new) media object URI after the update (may change depending on DB implementation!)
	 * @throws YumaDatabaseException if anything goes wrong
	 * @throws MediaObjectNotFoundException if there is no media object with the given uri
	 */
	public abstract String updateMediaObject(String uri, MediaObject mediaObject)
		throws YumaDatabaseException, MediaObjectNotFoundException, InvalidMediaObjectException;

	/**
	 * Delete a media object
	 * @param uri the media object URI
	 * @throws YumaDatabaseException  if anything goes wrong
	 * @throws MediaObjectNotFoundException if the media object does not exist in the DBto
	 */
	public abstract void deleteMediaObject(String uri)
		throws YumaDatabaseException, MediaObjectNotFoundException;
		
	/**
	 * Retrieves the media objects for the given user
	 * @param username the user name
	 * @return the media objects
	 * @throws YumaDatabaseException if anything goes wrong
	 */
	public abstract List<MediaObject> findMediaObjectsForUser(String username)
		throws YumaDatabaseException;

	/**
	 * Retrieve an media object by URI
	 * @param uri the media object URI
	 * @return the media object
	 * @throws YumaDatabaseException if anything goes wrong
	 * @throws MediaObjectNotFoundException if the mediaobject was not found
	 */
	public abstract MediaObject findMediaObjectByUri(String uri)
		throws YumaDatabaseException, MediaObjectNotFoundException;
	
}