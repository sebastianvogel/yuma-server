package at.ait.dme.yuma.server.exception;

public class InvalidApplicationClientException extends RuntimeException {
	private static final long serialVersionUID = 1849222851165325166L;
	
	public InvalidApplicationClientException(String message) {
		super(message);
	}
}
