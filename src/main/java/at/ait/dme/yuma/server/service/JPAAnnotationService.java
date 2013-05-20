package at.ait.dme.yuma.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PessimisticLockException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.ait.dme.yuma.server.db.IAppClientDAO;
import at.ait.dme.yuma.server.db.IUserDAO;
import at.ait.dme.yuma.server.db.entities.AnnotationEntity;
import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * DB implementation for relational databases using the
 * Hibernate persistence layer. 
 * 
 * @author Christian Sadilek
 * @author Rainer Simon
 */
@Service
public class JPAAnnotationService implements IAnnotationService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	IUserDAO userDAO;
	
	@Autowired
	IAppClientDAO appClientDAO;

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=AnnotationModifiedException.class)
	public String createAnnotation(Annotation annotation, String appClientToken) throws AnnotationModifiedException {

		// in case of a reply we have to ensure that the parent is unchanged
		AnnotationEntity entity = new AnnotationEntity(annotation);
		if (entity.getParentId() != null) {
			// an annotation gets a new id on every update. therefore, checking
			// for
			// existence is sufficient here.
			AnnotationEntity parent = em.find(AnnotationEntity.class,
					entity.getParentId());
			if (parent == null) {
				throw new AnnotationModifiedException(entity.getParentId());
			}

			// parent is unchanged, lock it and make sure it wasn't modified
			// concurrently
			try {
				em.lock(parent, LockModeType.PESSIMISTIC_WRITE);
			} catch (PessimisticLockException e) {
				throw new AnnotationModifiedException(annotation.getParentId());
			}
			em.refresh(parent);
		}
		
		//check if appClient exists:
		AppClientEntity appClient = appClientDAO.getAppClient(appClientToken);
		
		//check if user exists:
		UserEntity user = userDAO.findUser(annotation.getCreatedBy(), appClient);
		if (user==null) {
			user = userDAO.createUser(annotation.getCreatedBy(), appClient);
		}
		entity.setCreatedBy(user);

		em.persist(entity);
		return Long.toString(entity.getAnnotationId());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=AnnotationDatabaseException.class)
	public String updateAnnotation(String annotationId, Annotation annotation, String appClientToken)
			throws AnnotationDatabaseException, AnnotationNotFoundException, AnnotationHasReplyException {
	
		AnnotationEntity entity = new AnnotationEntity(annotation);
		
		//check appClient:
		AppClientEntity appClient = appClientDAO.getAppClient(appClientToken);
		
		//find user:
		UserEntity user = userDAO.findUser(annotation.getCreatedBy(), appClient);
		entity.setCreatedBy(user);
		
		deleteAnnotation(annotationId, appClientToken);
		em.persist(entity);			
		return Long.toString(entity.getAnnotationId());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, 
	               rollbackFor= {AnnotationHasReplyException.class, AnnotationNotFoundException.class })
	public void deleteAnnotation(String annotationId, String appClientToken) 
			throws AnnotationHasReplyException, AnnotationNotFoundException {

		appClientDAO.getAppClient(appClientToken);
		Long id = Long.parseLong(annotationId);
		AnnotationEntity entity = em.find(AnnotationEntity.class, id);
		if (entity==null) {
			throw new AnnotationNotFoundException();
		}
		em.lock(entity, LockModeType.PESSIMISTIC_WRITE);			
		em.refresh(entity);
		
		if (countReplies(annotationId) > 0) {
			throw new AnnotationHasReplyException();
		}
		em.remove(entity);			
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public AnnotationTree findAnnotationsForObject(String objectUri) throws AnnotationDatabaseException {
		TypedQuery<AnnotationEntity> query = 
				em.createNamedQuery("annotationentity.find.for.object", AnnotationEntity.class);
		query.setParameter("objectUri", objectUri);
		List<AnnotationEntity> allAnnotations = query.getResultList();
		return new AnnotationTree(toAnnotations(allAnnotations));
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public long countAnnotationsForObject(String objectUri)
			throws AnnotationDatabaseException {
		
		TypedQuery<Long> query = em.createNamedQuery("annotationentity.count.for.object", Long.class);
		query.setParameter("objectUri", objectUri);
		return query.getSingleResult();
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public List<Annotation> findAnnotationsForUser(String username) throws AnnotationDatabaseException {

		TypedQuery<AnnotationEntity> query = em.createNamedQuery(
				"annotationentity.find.for.user", AnnotationEntity.class);
		query.setParameter("username", username);
		List<AnnotationEntity> allAnnotations = query.getResultList();
		return toAnnotations(allAnnotations);
	}	
	
	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS, rollbackFor=AnnotationNotFoundException.class)
	public Annotation findAnnotationById(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {

		Long id = Long.parseLong(annotationId);
		AnnotationEntity entity = em.find(AnnotationEntity.class, id);
		if (entity == null) {
			throw new AnnotationNotFoundException();
		}
			
		return entity.toAnnotation();
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public long countReplies(String annotationId)
			throws AnnotationDatabaseException {
		
		TypedQuery<Long> query = em.createNamedQuery("annotationentity.count.replies", Long.class);
		query.setParameter("id", Long.parseLong(annotationId));
		return query.getSingleResult();
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public AnnotationTree getReplies(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {

		Annotation a = findAnnotationById(annotationId);
		if (a==null) {
			throw new AnnotationNotFoundException();
		}
		
		String rootId;
		if (a.getRootId() == null) {
			rootId = a.getAnnotationID();
		} else {
			rootId = a.getRootId();
		}
		
		TypedQuery<AnnotationEntity> query = 
				em.createNamedQuery("annotationentity.find.thread", AnnotationEntity.class);
		query.setParameter("rootId", Long.parseLong(rootId));
		List<AnnotationEntity> thread = query.getResultList();		
		return new AnnotationTree(toAnnotations(filterReplies(thread, annotationId)));
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public List<Annotation> getMostRecent(int n, boolean publicOnly)
			throws AnnotationDatabaseException {
		
		TypedQuery<AnnotationEntity> query;
		if (publicOnly) {
			query = em.createNamedQuery("annotationentity.mostrecent.public", AnnotationEntity.class);
		} else {
			query = em.createNamedQuery("annotationentity.mostrecent.all", AnnotationEntity.class);
		}
		query.setMaxResults(n);
			
		List<AnnotationEntity> mostRecent = query.getResultList();
		return toAnnotations(mostRecent);
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public List<Annotation> findAnnotations(String q)
			throws AnnotationDatabaseException {
		
		TypedQuery<AnnotationEntity> query = 
				em.createNamedQuery("annotationentity.searchTextTitleAndTags", AnnotationEntity.class);
		query.setParameter("term", q.toLowerCase());
		
		List<AnnotationEntity> entities = 
				new ArrayList<AnnotationEntity>(new HashSet<AnnotationEntity>(query.getResultList()));
			
		Collections.sort(entities, new Comparator<AnnotationEntity>() {
			@Override
			public int compare(AnnotationEntity a, AnnotationEntity b) {
				if (a.getLastModified().before(b.getLastModified())) {
					return 1;
				} else {				
					return -1;
				}
			}
		});	
		return toAnnotations(entities);
	}
	
	/**
	 * Filters an entire annotation thread so that only (direct or indirect)
	 * children of the annotation with the specified ID are returned. 
	 * @param thread the entire annotation thread
	 * @param parentId the ID of the parent
	 * @return the filtered thread
	 */
	private List<AnnotationEntity> filterReplies(List<AnnotationEntity> thread, String parentId) {		
		List<AnnotationEntity> replies = new ArrayList<AnnotationEntity>();
		getChildren(thread, replies, Long.parseLong(parentId));
		return replies;
	}
	
	private void getChildren(List<AnnotationEntity> all, List<AnnotationEntity> children, Long parentId) {
		for (AnnotationEntity a : all) {
			if (a.getParentId().equals(parentId)) {
				children.add(a);
				getChildren(all, children, a.getAnnotationId());
			}
		}
	}
	
	private List<Annotation> toAnnotations(List<AnnotationEntity> entities) throws AnnotationDatabaseException {
		List<Annotation> annotations = new ArrayList<Annotation>();
		for (AnnotationEntity entity : entities) {
			annotations.add(entity.toAnnotation());
		}
		return annotations;
	}

}
