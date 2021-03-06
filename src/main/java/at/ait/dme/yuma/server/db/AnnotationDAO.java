package at.ait.dme.yuma.server.db;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import at.ait.dme.yuma.server.db.entities.AnnotationEntity;
import at.ait.dme.yuma.server.model.MediaType;
import at.ait.dme.yuma.server.util.URIBuilder;

@Repository
public class AnnotationDAO implements IAnnotationDAO {
	
	private static Logger log = Logger.getLogger(AnnotationDAO.class);
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public List<AnnotationEntity> findAnnotationsForURI(URI objectUri, MediaType type) {
		TypedQuery<AnnotationEntity> query = 
				em.createNamedQuery("annotationentity.find.for.object_and_type", AnnotationEntity.class);
		query.setParameter("objectUri", objectUri.toString());
		query.setParameter("type", type);
		return query.getResultList();
	}
	
	/**
	 * persist an AnnotationEntity
	 * @param entity
	 */
	public void persist(AnnotationEntity entity) {
		em.persist(entity);
	}
	
	@Override
	public List<AnnotationEntity> findAnnotationsForURI(String objectUri) {
		if (objectUri==null) {
			return null;
		}
		
		TypedQuery<AnnotationEntity> query = 
				em.createNamedQuery("annotationentity.find.for.object", AnnotationEntity.class);
		query.setParameter("objectUri", objectUri);
		return query.getResultList();
	}

	@Override
	public AnnotationEntity findAnnotationByIdentifier(String identifier) {
		if (identifier==null) {
			return null;
		}
		Long id = null;
		try {
			id = new Long(identifier);
		} catch (NumberFormatException e) {
			log.info("annotationId is not a number, invocate findByURI method");
		}
		if (id!=null) {
			return em.find(AnnotationEntity.class, id);
		} else {
			return findAnnotationByURI(identifier);
		}
	}

	private AnnotationEntity findAnnotationByURI(String annotationURI) {
		if (annotationURI==null) {
			return null;
		}
		
		try {
			String ident = URIBuilder.toID(annotationURI);
			Long id = new Long(ident);
			return em.find(AnnotationEntity.class, id);
		} catch (URISyntaxException e) {
			log.info("cannot retrieve id from given uri " + annotationURI);
			return null;
		} catch (NumberFormatException e) {
			log.info(String.format("cannot retrieve id from given uri %s: %s", 
					annotationURI, e.getMessage()));
			return null;
		}
	}
}
