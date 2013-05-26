package at.ait.dme.yuma.server.service;

import java.net.URI;

import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.URISource;

public interface IACLService {
	
	String createACL(URI uri, String clientToken, String username);
	String updateACL(String identifier, Annotation acl, String clientToken);
	ACL findACLByObjectId(String identifier, URISource source) throws AnnotationNotFoundException;
	ACL findACLById(String identifier) throws AnnotationNotFoundException;
	ACL findACLByObjectURI(URI uri) throws AnnotationNotFoundException;
}
