package at.ait.dme.yuma.server.exception;

public class PermissionDeniedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public PermissionDeniedException(String message) {
		super(message);
	}

	public PermissionDeniedException() {
		super();
	}

}
