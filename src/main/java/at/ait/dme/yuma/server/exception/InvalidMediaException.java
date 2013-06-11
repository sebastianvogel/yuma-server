package at.ait.dme.yuma.server.exception;

/**
 * This Exception indicates a problem when parsing/processing an 
 * media object, or is thrown in case a media object is internally
 * inconsistent.
 * 
 * @author Sebastian Vogel
 */
public class InvalidMediaException extends Exception {

	private static final long serialVersionUID = 5343849139804628693L;

	public InvalidMediaException() {
		super();
	}

	public InvalidMediaException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMediaException(String message) {
		super(message);
	}

	public InvalidMediaException(Throwable cause) {
		super(cause);
	}
}
