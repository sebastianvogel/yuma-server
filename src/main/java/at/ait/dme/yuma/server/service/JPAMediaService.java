package at.ait.dme.yuma.server.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.db.IAppClientDAO;
import at.ait.dme.yuma.server.db.IMediaDAO;
import at.ait.dme.yuma.server.db.IUserDAO;
import at.ait.dme.yuma.server.db.entities.MediaEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.InvalidMediaException;
import at.ait.dme.yuma.server.exception.MediaNotFoundException;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.Media;
import at.ait.dme.yuma.server.model.MediaContentVersion;
import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.model.User;
import at.ait.dme.yuma.server.util.URIBuilder;

public class JPAMediaService implements IMediaService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	IUserDAO userDao;
	
	@Autowired
	IAppClientDAO appClientDao;
	
	@Autowired
	IMediaDAO mediaDao;
	
	@Autowired
	ICheckService checkService;

	@Override
	public Media createMedia(Media media, AuthContext auth) throws InvalidMediaException {
		UserEntity owner = retrieveUserEntity(auth.getUsername(), auth);
		if (owner != null) {
			return mediaDao.createMedia(media, owner).toMedia();
		} else {
			throw new InvalidMediaException("Cannot create media without a user");
		}
	}

	@Override
	public Media updateMedia(Media media, AuthContext auth)
			throws MediaNotFoundException, PermissionDeniedException {
		MediaEntity mediaEntity = mediaDao.findMedia(media.getId());
		User owner = mediaEntity.getCreatedBy().toUser();
		if (!owner.getAuthContext().equals(auth)) {
			throw new PermissionDeniedException();
		}
		mediaEntity.update(media);
		em.merge(mediaEntity);
		return mediaEntity.toMedia();
	}

	@Override
	public List<Media> findMediaByUsers(String username, AuthContext auth) throws PermissionDeniedException {
		UserEntity user = retrieveUserEntity(username, auth);
		if (user != null) {
			return mediaDao.toMedia(mediaDao.findMediaForUser(user));
		} else {
			throw new PermissionDeniedException("Username not found in auth context");
		}
	}

	@Override
	public MediaContentVersion createMediaContentVersion(Long mediaId,
			MediaContentVersion mediaContent, AuthContext auth)
			throws MediaNotFoundException, PermissionDeniedException {
		MediaEntity mediaEntity = mediaDao.findMedia(mediaId);
		User owner = mediaEntity.getCreatedBy().toUser();
		if (!owner.getAuthContext().equals(auth)) {
			throw new PermissionDeniedException();
		}
		return mediaDao.createMediaContentVersion(mediaEntity, mediaContent).toMediaContentVersion();
	}

	@Override
	public Media findMedia(Long id, AuthContext auth)
			throws MediaNotFoundException, PermissionDeniedException {
		MediaEntity mediaEntity = mediaDao.findMedia(id);
		if (mediaEntity == null) {
			throw new MediaNotFoundException();
		}
		if (!checkService.hasReadPermission(auth, mediaEntity.toMedia())) {
			throw new PermissionDeniedException(); 
		}
		return mediaEntity.toMedia();
	}

	@Override
	public MediaContentVersion findMediaContentVersion(Long mediaId,
			Long version, AuthContext auth) throws MediaNotFoundException, PermissionDeniedException {
		MediaContentVersion mediaContent = mediaDao.findMediaContentVersion(mediaId, version).toMediaContentVersion();
		if(!checkService.hasReadPermission(auth, mediaContent.getMedia())) {
			throw new PermissionDeniedException();
		}
		return mediaContent;
	}

	@Override
	public List<URI> findMediaContentVersionsByMedia(
			Long mediaId, boolean relative, AuthContext auth) throws MediaNotFoundException, PermissionDeniedException {
		List<URI> results = new ArrayList<URI>();
		MediaEntity mediaEntity = mediaDao.findMedia(mediaId);
		if(!checkService.hasReadPermission(auth, mediaEntity.toMedia())) {
			throw new PermissionDeniedException();
		}
		List<String> mediaContentList = mediaDao.getMediaContentList(mediaEntity);
		for(String e: mediaContentList) {
			results.add(URIBuilder.toURI(mediaEntity.getId().toString() + "/content/" + e, URISource.MEDIA, relative));
		}
		return results;
	}

	private UserEntity retrieveUserEntity(String username, AuthContext auth) {
		return userDao.findUser(new User(username), appClientDao.getAppClient(auth.getClient()));
	}


}
