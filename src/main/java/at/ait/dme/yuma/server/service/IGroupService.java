package at.ait.dme.yuma.server.service;

import java.net.URI;

import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Group;

public interface IGroupService {
	
	Group findGroup(String groupName);
	Group findGroup(URI uri);
	void addToGroup(String groupName, String username, AuthContext auth) throws PermissionDeniedException;
	void removeFromGroup(String groupName, String username, AuthContext auth) throws PermissionDeniedException;
	void deleteGroup(String groupName, AuthContext auth) throws PermissionDeniedException;
}
