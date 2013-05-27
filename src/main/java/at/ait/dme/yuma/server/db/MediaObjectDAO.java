package at.ait.dme.yuma.server.db;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.ait.dme.yuma.server.db.entities.MediaObjectEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.MediaObjectNotFoundException;
import at.ait.dme.yuma.server.model.MediaObject;

@Repository
public class MediaObjectDAO implements IMediaObjectDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	@Override
	public MediaObjectEntity findMediaObjectByUri(String uri) throws MediaObjectNotFoundException {
		if (uri==null) {
			throw new MediaObjectNotFoundException("URI is null");
		}
		TypedQuery<MediaObjectEntity> query = em.createNamedQuery("media.findByUri", MediaObjectEntity.class);
		query.setParameter("uri", uri);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			throw new MediaObjectNotFoundException(e.getMessage());
		}
	}


	@Override
	public List<MediaObjectEntity> findMediaObjectsForUser(UserEntity user) {
		TypedQuery<MediaObjectEntity> query = em.createNamedQuery("media.findForUser", MediaObjectEntity.class);
		query.setParameter("user", user);
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<MediaObjectEntity>();
		}
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MediaObjectEntity createMediaObject(MediaObject mediaObject,
			UserEntity user) {
		MediaObjectEntity entity = new MediaObjectEntity(mediaObject);
		entity.setCreatedBy(user);
		em.persist(entity);
		return entity;
	}


	@Override
	public List<MediaObject> toMediaObjects(
			List<MediaObjectEntity> mediaObjectEntities) {
		List<MediaObject> mediaObjects = new ArrayList<MediaObject>();
		for(MediaObjectEntity e: mediaObjectEntities) {
			mediaObjects.add(e.toMediaObject());
		}
		return mediaObjects;
	}
}
