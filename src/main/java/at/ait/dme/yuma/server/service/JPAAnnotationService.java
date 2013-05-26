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

import at.ait.dme.yuma.server.db.IAnnotationDAO;
import at.ait.dme.yuma.server.db.IAppClientDAO;
import at.ait.dme.yuma.server.db.IUserDAO;
import at.ait.dme.yuma.server.db.entities.AnnotationEntity;
import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
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
	
	@Autowired
	IAnnotationDAO annotationDAO;
	
	@Autowired
	ICheckService checkService;

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
	@Transactional(propagation=Propagation.REQUIRES_NEW, 
		rollbackFor={AnnotationHasReplyException.class, AnnotationNotFoundException.class, PermissionDeniedException.class})
	public String updateAnnotation(String annotationId, Annotation annotation, String client) 
			throws AnnotationHasReplyException, AnnotationNotFoundException, PermissionDeniedException {
	
		AnnotationEntity entity = new AnnotationEntity(annotation);
		
		//check appClient:
		AppClientEntity appClient = appClientDAO.getAppClient(client);
		
		//find user:
		UserEntity user = userDAO.findUser(annotation.getCreatedBy(), appClient);
		entity.setCreatedBy(user);
		
		deleteAnnotation(annotationId, client, user.getUsername());
		em.persist(entity);			
		return Long.toString(entity.getAnnotationId());
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, 
		rollbackFor={AnnotationHasReplyException.class, AnnotationNotFoundException.class, PermissionDeniedException.class})
	public void deleteAnnotation(String annotationId, String client, String username) 
			throws AnnotationHasReplyException, AnnotationNotFoundException, PermissionDeniedException {

		appClientDAO.getAppClient(client);
		AnnotationEntity entity = annotationDAO.findAnnotationByIdentifier(annotationId);
		if (entity==null) {
			throw new AnnotationNotFoundException();
		}
		
		//check if given username is listed as owner:
		if (!entity.getCreatedBy().getUsername().equals(username)) {
			throw new PermissionDeniedException();
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
	public AnnotationTree findAnnotationsForObject(String objectUri, String client, String username) {
		List<AnnotationEntity> annotations = annotationDAO.findAnnotationsForURI(objectUri);
		return new AnnotationTree(checkPermission(username, annotations));
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
		List<AnnotationEntity> annotations = query.getResultList();
		return checkPermission(username, annotations);
	}	
	
	@Override
	@Transactional(
			readOnly=true,
			propagation=Propagation.SUPPORTS, 
			rollbackFor= {AnnotationNotFoundException.class, PermissionDeniedException.class})
	public Annotation findAnnotationById(String annotationId, String client, String username)
			throws AnnotationNotFoundException, PermissionDeniedException {

		AnnotationEntity entity = annotationDAO.findAnnotationByIdentifier(annotationId);
		if (entity == null) {
			throw new AnnotationNotFoundException();
		}
		
		Annotation annotation = entity.toAnnotation();
		if (checkService.hasReadPermission(username, annotation)) {
			return annotation;
		} else {
			throw new PermissionDeniedException();
		}
	}

	/**
	 * Retrieves the number of replies that exist for
	 * the given annotation
	 * @param annotationId the annotation ID
	 * @return the number of replies
	 * @throws AnnotationNotFoundException if the annotation was not found
	 */
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	private long countReplies(String annotationId)
			throws AnnotationDatabaseException {
		
		TypedQuery<Long> query = em.createNamedQuery("annotationentity.count.replies", Long.class);
		query.setParameter("id", Long.parseLong(annotationId));
		return query.getSingleResult();
	}

	@Override
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public AnnotationTree getReplies(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException, PermissionDeniedException {

		Annotation a = findAnnotationById(annotationId, null, null);
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
		List<AnnotationEntity> annotations = query.getResultList();
		List<AnnotationEntity> filtered = filterReplies(annotations, annotationId);
		return new AnnotationTree(checkPermission(null, filtered));
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
		return checkPermission(null, mostRecent);
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
		return checkPermission(null, entities);
	}
	
	/**
	 * check permissions for each annotation in list, 
	 * only return annotations for which the given user has read permissions
	 * @param username
	 * @param annotations
	 * @return
	 */
	private List<Annotation> checkPermission(String username, List<AnnotationEntity> annotations) {
		List<Annotation> ret = new ArrayList<Annotation>();
		for (AnnotationEntity entity : annotations) {
			Annotation a = entity.toAnnotation();
			if (checkService.hasReadPermission(username, a)) {
				ret.add(a);
			}
		}
		return ret;		
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
}
