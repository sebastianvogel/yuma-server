package at.ait.dme.yuma.server.db.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.db.AbstractAnnotationDB;
import at.ait.dme.yuma.server.db.hibernate.entities.AnnotationEntity;
import at.ait.dme.yuma.server.exception.AnnotationDatabaseException;
import at.ait.dme.yuma.server.exception.AnnotationHasReplyException;
import at.ait.dme.yuma.server.exception.AnnotationModifiedException;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

/**
 * DB implementation for relational databases using the
 * Hibernate persistence layer. 
 * 
 * @author Christian Sadilek
 * @author Rainer Simon
 */
public class HibernateAnnotationDB extends AbstractAnnotationDB {
	
	private static EntityManagerFactory emf;		
	private EntityManager em = null;

	@Override
	public synchronized void init() throws AnnotationDatabaseException {
		final Config config = Config.getInstance();
		Map<String, String> emfProperties = new HashMap<String, String>() {
			private static final long serialVersionUID = -1508851708183619075L;
			{
				put("javax.persistence.provider", "org.hibernate.ejb.HibernatePersistence");
				put("hibernate.hbm2ddl.auto", "update");	
				put("hibernate.show_sql","false");
				put("hibernate.connection.driver_class", config.getDbDriver());
				put("hibernate.connection.username", config.getDbUser());
				put("hibernate.connection.password", config.getDbPass());
				put("hibernate.connection.url", config.getDbDriverProtocol() + "://"
					+ config.getDbHost() + ":" + config.getDbPort() + "/" + config.getDbName());
			}};
		
		emf = Persistence.createEntityManagerFactory("annotation", emfProperties);
	}

	@Override
	public void shutdown() {
		if(emf!=null) emf.close();
	}

	@Override
	public void connect(HttpServletRequest request)
			throws AnnotationDatabaseException {
		
		if(emf==null) 
			throw new AnnotationDatabaseException("entity manager factory not initialized");
		
		em=emf.createEntityManager();		
	}

	@Override
	public void disconnect() {
		if(em!=null) em.close();	
	}

	@Override
	public void commit() throws AnnotationDatabaseException {
		if(em==null||em.getTransaction()==null) 
			throw new AnnotationDatabaseException("no transaction to commit");
		
		em.getTransaction().commit();	
	}

	@Override
	public void rollback() throws AnnotationDatabaseException {
		if(em==null||em.getTransaction()==null) 
			throw new AnnotationDatabaseException("no transaction to rollback");
		
		em.getTransaction().rollback();		
	}

	@Override
	public String createAnnotation(Annotation annotation)
			throws AnnotationDatabaseException, AnnotationModifiedException,
			InvalidAnnotationException {

		try {			
			if (!em.getTransaction().isActive()) em.getTransaction().begin();
			
			// in case of a reply we have to ensure that the parent is unchanged
			AnnotationEntity entity = new AnnotationEntity(annotation);
			if (entity.getParentId()!=null) {
				// an annotation gets a new id on every update. therefore, checking for
				// existence is sufficient here.			
				AnnotationEntity parent = em.find(AnnotationEntity.class, entity.getParentId());				
				if(parent==null) 
					throw new AnnotationModifiedException(entity.getParentId());

				// parent is unchanged, lock it and make sure it wasn't modified concurrently
				try {
					em.lock(parent, LockModeType.WRITE);
				} catch(OptimisticLockException ole) {
					throw new AnnotationModifiedException(annotation.getParentId());
				}				
				em.refresh(parent);				
			}

			em.persist(entity);														
			if(isAutoCommit()) commit();
			return Long.toString(entity.getAnnotationId());
		} catch(AnnotationModifiedException e) {
			throw e;
		} catch(Throwable t) {
			rollback();
			System.out.println(FAILED_TO_SAVE_ANNOTATION + ":" + t.getMessage());
			throw new AnnotationDatabaseException(t);
		}
	}

	@Override
	public String updateAnnotation(String annotationId, Annotation annotation)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			AnnotationHasReplyException, InvalidAnnotationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAnnotation(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException,
			AnnotationHasReplyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnnotationTree findAnnotationsForObject(String objectId)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countAnnotationsForObject(String objectId)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Annotation findAnnotationById(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long countReplies(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AnnotationTree findThreadForAnnotation(String annotationId)
			throws AnnotationDatabaseException, AnnotationNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> getMostRecent(int n)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Annotation> findAnnotations(String query)
			throws AnnotationDatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}
