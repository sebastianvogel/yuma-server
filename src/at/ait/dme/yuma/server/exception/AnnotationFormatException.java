package at.ait.dme.yuma.server.exception;

/**
 * This Exception indicates a problem when parsing/processing an 
 * annotation.
 * 
 * @author Christian Sadilek
 */
public class AnnotationFormatException extends Exception {
	
	private static final long serialVersionUID = -6104005265727225396L;

	public AnnotationFormatException() {
		super();
	}

	public AnnotationFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnnotationFormatException(String message) {
		super(message);
	}

	public AnnotationFormatException(Throwable cause) {
		super(cause);
	}
}
