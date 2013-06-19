package at.ait.dme.yuma.server.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.Group;
import at.ait.dme.yuma.server.model.IOwnable;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.User;
import at.ait.dme.yuma.server.util.URIBuilder;

@Service
public class CheckService implements ICheckService {
	
	@Autowired
	IACLService aclService;
	
	@Autowired
	IGroupService groupService;
	
	private enum PERMISSION_TYPE { READ, WRITE }
	
	/**
	 * check if given user has read-permissions
	 * @param username
	 * @param ownable
	 * @return
	 */
	public boolean hasReadPermission(AuthContext auth, IOwnable ownable) {
		Scope scope = ownable.getScope();
		if (scope==null) {
			scope = Config.getInstance().getScopePolicy(); 
		}
		
		if (scope==Scope.PUBLIC) {
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
		
		return hasPermission(acl, PERMISSION_TYPE.READ, auth);
	}
	
	/**
	 * check if user, given by auth context, has write permissions
	 * write permissions are granted if:
	 *  - user is owner
	 *  - there is an acl that grants write permission
	 */
	public boolean hasWritePermission(AuthContext auth, IOwnable ownable) {
		if (auth==null || auth.getClient()==null || auth.getUsername()==null) {
			return false;
		}
		
		//everybody may write to objects with an public url:
		if (URIBuilder.isPublic(ownable.getURI(false))) {
			return true;	
		}
		
		User owner = ownable.getCreatedBy();
		if (owner.getClient()==null) {
			//if we do not have a client, set given client:
			owner.setClient(auth.getClient());
		}
		
		//owner always may create annotations:
		if (owner.getAuthContext().equals(auth)) {
			return true;
		}
		
		//check if there are acls for media:
		ACL acl;
		try {
			acl = aclService.findACLByObjectURI(ownable.getURI(true));
		} catch (AnnotationNotFoundException e) {
			return false;
		}
		
		if (acl==null) {
			return false;
		}
		
		return hasPermission(acl, PERMISSION_TYPE.WRITE, auth);
	}
	
	/**
	 * check if an acl permits read for a user within a given auth context
	 * @param acl
	 * @param auth
	 * @return
	 */
	private boolean hasPermission(ACL acl, PERMISSION_TYPE type, AuthContext auth) {
		URI userURI = auth.toURI(true);
		for (ACL.Entity entity : acl.entities()) {
			
			URI subject = entity.getSubject();
			
			//just check for read permissions:
			
			switch (type) {
			case READ:
				if (!entity.hasReadPermission()) {
					continue;
				}
				break;
			case WRITE:
				if (!entity.hasWritePermission()) {
					continue;
				}
				break;
			}
			
			//if a catch all directive is found, grant permission:
			if (entity.isCatchAll()) {
				return true;
			}
			
			//direct permissions:
			if (userURI.equals(subject)) {
				return true;
			}
			
			//group permission:
			if (entity.isGroupSubject()) {
				Group group = groupService.findGroup(subject);
				if (group!=null && group.hasMember(userURI.toString())) {
					return true;
				}
			}
		}
		return false;
	}
}
