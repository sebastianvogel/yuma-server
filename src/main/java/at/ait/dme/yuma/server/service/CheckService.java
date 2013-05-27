package at.ait.dme.yuma.server.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.db.entities.MediaObjectEntity;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.IOwnable;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.model.User;
import at.ait.dme.yuma.server.util.URIBuilder;

@Service
public class CheckService implements ICheckService {
	
	@Autowired
	IACLService aclService;
	
	/**
	 * check if given user has read-permissions
	 * @param username
	 * @param ownable
	 * @return
	 */
	public boolean hasReadPermission(AuthContext auth, IOwnable ownable) {
		Scope scope = ownable.getScope();
		if (scope==null || scope==Scope.PUBLIC) {
			return true;
		}
		
		if (auth==null || auth.getUsername()==null) {
			return false;
		}
		
		User user = ownable.getCreatedBy();
		if (user==null) {
			return false;
		}
		
		//owner always have read permissions:
		if (auth.equals(user.getAuthContext())) {
			return true;
		}
		
		//check acl's:
		ACL acl;
		try {
			acl = aclService.findACLByObjectURI(ownable.getURI(true));
		} catch (AnnotationNotFoundException e) {
			return false;
		}
		
		if (acl==null) {
			return false;
		}
		
		URI userURI = URIBuilder.toURI(auth.getUsername(), URISource.USER, true);
		return acl.hasReadPermission(userURI);
	}
	
	/**
	 * check if user, passed via AuthContext has the right to create an annotation
	 */
	public boolean hasRightToCreateAnnotation(AuthContext auth, Annotation annotation) {
		if (auth==null || auth.getClient()==null || auth.getUsername()==null) {
			return false;
		}
		
		//check if annotation-target is public url:
		if (!URIBuilder.isPublic(annotation.getObjectUri())) {
			return true;	
		}
		
		//find media for annotation:
		//TODO: implement!!
		MediaObjectEntity media = null;
		User owner = media.getCreatedBy().toUser();
		
		//owner always may create annotations:
		if (owner.getAuthContext().equals(auth)) {
			return true;
		}
		
		//check if there are acls for media:
		ACL acl;
		try {
			acl = aclService.findACLByObjectURI(/** TODO: implement media.getURI **/ null);
		} catch (AnnotationNotFoundException e) {
			return false;
		}
		
		if (acl==null) {
			return false;
		}
		
		URI userURI = URIBuilder.toURI(auth.getUsername(), URISource.USER, true);
		return acl.hasWritePermission(userURI);
	}
}
