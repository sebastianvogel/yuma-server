package at.ait.dme.yuma.server.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.db.IAppClientDAO;
import at.ait.dme.yuma.server.db.IGroupDAO;
import at.ait.dme.yuma.server.db.IUserDAO;
import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.entities.GroupEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Group;
import at.ait.dme.yuma.server.model.User;
import at.ait.dme.yuma.server.util.URIBuilder;

@Service
public class JPAGroupService implements IGroupService {
	
	@Autowired
	ICheckService checkService;
	
	@Autowired
	IGroupDAO groupDao;
	
	@Autowired
	IUserDAO userDao;
	
	@Autowired
	IAppClientDAO appClientDAO;
	
	private static Logger log = Logger.getLogger(JPAGroupService.class);
	
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	@Override
	public Group findGroup(String groupName) {
		GroupEntity group = groupDao.findGroup(groupName);
		if (group==null) {
			return null;
		} else {
			return group.toGroup();
		}
	}
	
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	@Override
	public Group findGroup(URI uri) {
		try {
			return findGroup(URIBuilder.toID(uri.toString()));
		} catch (URISyntaxException e) {
			log.warn("findGroupByURI - invalid uri: " + uri + ", " + e.getMessage());
			return null;
		}
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void addToGroup(String groupName, String username, AuthContext auth) throws PermissionDeniedException {
		AppClientEntity appClient = appClientDAO.getAppClient(auth.getClient());
		UserEntity user = userDao.getUser(new User(auth.getUsername()), appClient);
		GroupEntity group = authorize(groupName, auth);
		if (group==null) {
			group = groupDao.createGroup(username, user);
		}
		if (!group.hasMember(user)) {
			group.addMember(user);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void removeFromGroup(String groupName, String username, AuthContext auth) throws PermissionDeniedException {
		AppClientEntity appClient = appClientDAO.getAppClient(auth.getClient());
		UserEntity user = userDao.getUser(new User(auth.getUsername()), appClient);
		GroupEntity group = authorize(groupName, auth);
		if (group==null) {
			group = groupDao.createGroup(username, user);
		}
		if (group.hasMember(user)) {
			group.removeMember(user);
		}
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public void deleteGroup(String groupName, AuthContext auth) throws PermissionDeniedException {
		GroupEntity group = authorize(groupName, auth);
		groupDao.delete(group);
	}
	
	/**
	 * find group by given group-name and check if user, given by auth context is owner
	 * @param groupName
	 * @param auth
	 * @return
	 * @throws PermissionDeniedException
	 */
	@Transactional(propagation=Propagation.SUPPORTS, readOnly=true)
	private GroupEntity authorize(String groupName, AuthContext auth) throws PermissionDeniedException {
		GroupEntity group = groupDao.findGroup(groupName);
		if (group==null) {
			return null;
		}
		
		if (auth==null) {
			log.warn("authorize: auth is null!");
			throw new PermissionDeniedException();			
		}
		
		if (!checkService.hasWritePermission(auth, group.toGroup())) {
			log.warn("authorize: no write permissions for group ".concat(groupName).concat(", auth=") + auth);
			throw new PermissionDeniedException();			
		}
		return group;
	}
}
