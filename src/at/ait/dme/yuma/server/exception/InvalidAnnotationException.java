package at.ait.dme.yuma.server.exception;

/**
 * This Exception indicates a problem when parsing/processing an 
 * annotation.
 * 
 * @author Christian Sadilek
 */
public class InvalidAnnotationException extends Exception {
	
	private static final long serialVersionUID = -6104005265727225396L;

	public InvalidAnnotationException() {
		super();
	}

	public InvalidAnnotationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAnnotationException(String message) {
		super(message);
	}

	public InvalidAnnotationException(Throwable cause) {
		super(cause);
	}
}
