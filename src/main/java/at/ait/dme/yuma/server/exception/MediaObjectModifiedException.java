package at.ait.dme.yuma.server.exception;

/**
 * This exception indicates that an media object has been 
 * concurrently modified and should be reread by
 * the client.
 * 
 * @author Sebastian Vogel
 */
public class MediaObjectModifiedException extends Exception {	
	
	private static final long serialVersionUID = 46337603472498374L;

	public MediaObjectModifiedException() {
		super();
	}

	public MediaObjectModifiedException(String message, Throwable cause) {
		super(message, cause);
	}

	public MediaObjectModifiedException(Long id) {
		super("media object has beed modified:"+id);
	}
	
	public MediaObjectModifiedException(String message) {
		super("media object has beed modified:"+message);
	}

	public MediaObjectModifiedException(Throwable cause) {
		super(cause);
	}
}
