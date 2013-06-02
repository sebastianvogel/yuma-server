package at.ait.dme.yuma.server.exception;

/**
 * This Exception indicates that a media object could not be found.
 * 
 * @author Sebastian Vogel
 */
public class MediaNotFoundException extends Exception {	
	
	private static final long serialVersionUID = 8350411939937353045L;

	public MediaNotFoundException() {
		super();
	}

	public MediaNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MediaNotFoundException(String message) {
		super(message);
	}

	public MediaNotFoundException(Throwable cause) {
		super(cause);
	}
}
