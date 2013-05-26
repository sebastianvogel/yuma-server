package at.ait.dme.yuma.server.service;

import org.springframework.stereotype.Service;

import at.ait.dme.yuma.server.controller.AuthContext;

@Service
public class JPAGroupService implements IGroupService {

	@Override
	public String createGroup(String groupName, AuthContext auth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addToGroup(String groupName, String username,
			AuthContext auth) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeFromGroup(String groupName, String username,
			AuthContext auth) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteGroup(String groupName, AuthContext auth) {
		// TODO Auto-generated method stub
	}

}
