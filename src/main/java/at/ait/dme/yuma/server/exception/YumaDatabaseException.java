package at.ait.dme.yuma.server.exception;

/**
 * This Exception indicates a problem with the actual database.
 * 
 * @author Christian Sadilek
 */
public class YumaDatabaseException extends Exception {
	
	private static final long serialVersionUID = 3328621278141052963L;

	public YumaDatabaseException() {
		super();
	}

	public YumaDatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public YumaDatabaseException(String message) {
		super(message);
	}

	public YumaDatabaseException(Throwable cause) {
		super(cause);
	}
}
