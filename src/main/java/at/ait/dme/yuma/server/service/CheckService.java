package at.ait.dme.yuma.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.IOwnable;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.User;

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
	public boolean hasReadPermission(String username, IOwnable ownable) {
		Scope scope = ownable.getScope();
		if (scope==null || scope==Scope.PUBLIC) {
			return true;
		}
		
		if (username==null) {
			return false;
		}
		
		User user = ownable.getCreatedBy();
		if (user==null) {
			return false;
		}
		
		//owner always have read permissions:
		if (username.equals(user.getUsername())) {
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
		
		return acl.hasReadPermission(username);
	}

}
