package at.ait.dme.yuma.server.db;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.ait.dme.yuma.server.db.entities.MediaContentVersionEntity;
import at.ait.dme.yuma.server.db.entities.MediaContentVersionEntityPK;
import at.ait.dme.yuma.server.db.entities.MediaEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.MediaNotFoundException;
import at.ait.dme.yuma.server.model.Media;
import at.ait.dme.yuma.server.model.MediaContentVersion;

@Repository
public class MediaDAO implements IMediaDAO {

	@PersistenceContext
	private EntityManager em;
	
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	@Override
	public MediaEntity findMedia(Long id) throws MediaNotFoundException {
		MediaEntity mediaEntity = em.find(MediaEntity.class, id);
		if (mediaEntity == null) {
			throw new MediaNotFoundException();
		}
		return mediaEntity;
	}


	@Override
	public List<MediaEntity> findMediaForUser(UserEntity user) {
		TypedQuery<MediaEntity> query = em.createNamedQuery("media.findForUser", MediaEntity.class);
		query.setParameter("user", user);
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<MediaEntity>();
		}
	}

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MediaEntity createMedia(Media media,
			UserEntity user) {
		MediaEntity entity = new MediaEntity(media);
		entity.setCreatedBy(user);
		em.persist(entity);
		return entity;
	}

	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public MediaContentVersionEntity createMediaContentVersion(MediaEntity mediaEntity,
			MediaContentVersion mediaContent) {
		MediaContentVersionEntity mediaContentVersionEntity = new MediaContentVersionEntity(mediaContent);
		mediaContentVersionEntity.setMediaEntity(mediaEntity);
		mediaContentVersionEntity.setCreatedDate(new Date());
		mediaEntity.setUpdatedDate(new Date());
		em.persist(mediaContentVersionEntity);
		em.persist(mediaEntity);
		return mediaContentVersionEntity;
	}
	

	@Override
	public MediaContentVersionEntity findMediaContentVersion(Long mediaId, Long version) {
		MediaContentVersionEntityPK  pk = new MediaContentVersionEntityPK(mediaId, version);
		return em.find(MediaContentVersionEntity.class, pk);
	}
	
	@Override
	public List<String> getMediaContentList(MediaEntity mediaEntity) {
		List<String> result = new ArrayList<String>();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<MediaContentVersionEntity> root = cq.from(MediaContentVersionEntity.class);
		cq.multiselect(root.get("version"), root.get("filename"));
		cq.where(cb.equal(root.get("mediaEntity"), mediaEntity));
		List<Tuple> tupleResult = em.createQuery(cq).getResultList();
		for(Tuple e: tupleResult) {
			Long version = (Long) e.get(0);
			String filename = (String) e.get(1);
			result.add(version.toString() + "/" + filename);
		}
		return result;
	}
	

	@Override
	public List<Media> toMedia(
			List<MediaEntity> mediaEntities) {
		List<Media> medias = new ArrayList<Media>();
		for(MediaEntity e: mediaEntities) {
			medias.add(e.toMedia());
		}
		return medias;
	}

	
	@Override
	public List<URI> toMediaContentUris(List<MediaContentVersionEntity> mediaContentVersionEntities, boolean relative) {
		List<URI> uris = new ArrayList<URI>();
		for(MediaContentVersionEntity e: mediaContentVersionEntities) {
			uris.add(e.toMediaObjectContent().getUri(relative));
		}
		return uris;
	}


}
