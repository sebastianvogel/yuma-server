package at.ait.dme.yuma.server.exception;

/**
 * This Exception indicates a problem when parsing/processing an 
 * media object, or is thrown in case a media object is internally
 * inconsistent.
 * 
 * @author Sebastian Vogel
 */
public class InvalidMediaObjectException extends Exception {

	private static final long serialVersionUID = 5343849139804628693L;

	public InvalidMediaObjectException() {
		super();
	}

	public InvalidMediaObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidMediaObjectException(String message) {
		super(message);
	}

	public InvalidMediaObjectException(Throwable cause) {
		super(cause);
	}
}
