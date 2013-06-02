package at.ait.dme.yuma.server.exception;

/**
 * This exception indicates that an media object has been 
 * concurrently modified and should be reread by
 * the client.
 * 
 * @author Sebastian Vogel
 */
public class MediaModifiedException extends Exception {	
	
	private static final long serialVersionUID = 46337603472498374L;

	public MediaModifiedException() {
		super();
	}

	public MediaModifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public MediaModifiedException(Long id) {
		super("media object has beed modified:"+id);
	}
	
	public MediaModifiedException(String message) {
		super("media object has beed modified:"+message);
	}

	public MediaModifiedException(Throwable cause) {
		super(cause);
	}
}
