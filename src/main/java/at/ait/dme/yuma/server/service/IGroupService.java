package at.ait.dme.yuma.server.service;

import at.ait.dme.yuma.server.controller.AuthContext;

public interface IGroupService {
	
	String createGroup(String groupName, AuthContext auth);
	void addToGroup(String groupName, String username, AuthContext auth);
	void removeFromGroup(String groupName, String username, AuthContext auth);
	void deleteGroup(String groupName, AuthContext auth);
}
