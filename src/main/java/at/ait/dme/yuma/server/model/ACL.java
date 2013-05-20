package at.ait.dme.yuma.server.model;

import java.util.Date;
import java.util.Map;

import at.ait.dme.yuma.server.exception.InvalidAnnotationException;

public class ACL extends Annotation {
	
	private static final long serialVersionUID = -6672573554698821188L;
	
	
	public ACL(String annotationId, String objectUri, User createdBy,
			Date created, Date lastModified, MediaType type, Scope scope) {
		
		super(annotationId, objectUri, createdBy, created, lastModified, type, scope);
	}
	
	public ACL(Map<String, Object> map) throws InvalidAnnotationException {
		super(map);		
	}	
}
