package at.ait.dme.yuma.server.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import at.ait.dme.yuma.server.db.IAppClientDAO;
import at.ait.dme.yuma.server.db.IMediaObjectDAO;
import at.ait.dme.yuma.server.db.IUserDAO;
import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.MediaObjectNotFoundException;
import at.ait.dme.yuma.server.model.MediaObject;
import at.ait.dme.yuma.server.model.User;

public class JPAMediaObjectService implements IMediaObjectService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	IUserDAO userDao;
	
	@Autowired
	IAppClientDAO appClientDao;
	
	@Autowired
	IMediaObjectDAO mediaObjectDao;

	@Override
	public String createMediaObject(MediaObject mediaObject,
			String clientToken) {
		AppClientEntity appClient = appClientDao.getAppClient(clientToken);
		UserEntity user = userDao.findUser(mediaObject.getCreatedBy(), appClient);
		
		return mediaObjectDao.createMediaObject(mediaObject, user).getURI();
	}

	@Override
	public MediaObject findMediaObjectByUri(String uri) throws MediaObjectNotFoundException {
		return mediaObjectDao.findMediaObjectByUri(uri).toMediaObject();
	}

	@Override
	public List<MediaObject> findMediaObjectsByUsers(String username,
			String clientToken) {
		AppClientEntity appClient = appClientDao.getAppClient(clientToken);
		UserEntity user = userDao.findUser(new User(username), appClient);
		return mediaObjectDao.toMediaObjects(mediaObjectDao.findMediaObjectsForUser(user));
	}

}
