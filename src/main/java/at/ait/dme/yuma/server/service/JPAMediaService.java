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
		em.persist(mediaEntity);
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
	public URI createMediaContentVersion(Long mediaId,
			MediaContentVersion mediaContent, AuthContext auth)
			throws MediaNotFoundException, PermissionDeniedException {
		MediaEntity mediaEntity = mediaDao.findMedia(mediaId);
		User owner = mediaEntity.getCreatedBy().toUser();
		if (!owner.getAuthContext().equals(auth)) {
			throw new PermissionDeniedException();
		}
		return mediaDao.createMediaContentVersion(mediaEntity, mediaContent).toMediaObjectContent().getUri(true);
	}

	@Override
	public Media findMedia(Long id)
			throws MediaNotFoundException {
		MediaEntity mediaEntity = mediaDao.findMedia(id);
		if (mediaEntity.getCreatedBy() != null) {
			throw new MediaNotFoundException();
		}
		return mediaEntity.toMedia();
	}

	@Override
	public MediaContentVersion findMediaContentVersion(Long mediaId,
			Long version) throws MediaNotFoundException {
		return mediaDao.findMediaContentVersion(mediaId, version).toMediaObjectContent();
	}

	@Override
	public List<URI> findMediaContentVersionsByMedia(
			Long mediaId, boolean relative) throws MediaNotFoundException {
		List<URI> results = new ArrayList<URI>();
		MediaEntity mediaEntity = mediaDao.findMedia(mediaId);
		List<String> mediaContentList = mediaDao.getMediaContentList(mediaEntity);
		for(String e: mediaContentList) {
			results.add(URIBuilder.toURI(mediaEntity.getId().toString() + "/content/" + e, URISource.MEDIA, relative));
		}
		return null;
	}

	private UserEntity retrieveUserEntity(String username, AuthContext auth) {
		return userDao.findUser(new User(username), appClientDao.getAppClient(auth.getClient()));
	}


}
