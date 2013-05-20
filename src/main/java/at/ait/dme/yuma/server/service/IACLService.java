package at.ait.dme.yuma.server.service;

import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.URISource;

public interface IACLService {
	
	String createACL(ACL acl, String clientToken);
	String updateACL(String identifier, Annotation acl, String clientToken);
	Annotation findACLByObjectId(String identifier, URISource source) throws AnnotationNotFoundException;
	Annotation findACLById(String identifier) throws AnnotationNotFoundException;
}
