package at.ait.dme.yuma.server.exception;

/**
 * This Exception indicates that a media object could not be found.
 * 
 * @author Sebastian Vogel
 */
public class MediaObjectNotFoundException extends Exception {	
	
	private static final long serialVersionUID = 8350411939937353045L;

	public MediaObjectNotFoundException() {
		super();
	}

	public MediaObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MediaObjectNotFoundException(String message) {
		super(message);
	}

	public MediaObjectNotFoundException(Throwable cause) {
		super(cause);
	}
}
