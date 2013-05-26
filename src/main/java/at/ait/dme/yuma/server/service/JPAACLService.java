package at.ait.dme.yuma.server.service;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.ait.dme.yuma.server.db.IAnnotationDAO;
import at.ait.dme.yuma.server.db.IAppClientDAO;
import at.ait.dme.yuma.server.db.IUserDAO;
import at.ait.dme.yuma.server.db.entities.AnnotationEntity;
import at.ait.dme.yuma.server.db.entities.MediaEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.MediaType;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.util.URIBuilder;

@Service
public class JPAACLService implements IACLService {
	
	private static Logger log = Logger.getLogger(JPAACLService.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	IUserDAO userDAO;
	
	@Autowired
	IAppClientDAO appClientDAO;
	
	@Autowired
	IAnnotationDAO annotationDAO;


	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ACL createACL(String targetIdentifier, URISource targetUriSource) {
		URI targetURI = URIBuilder.toURI(targetIdentifier, targetUriSource, true);
		UserEntity owner = null;
		//first find target for ACL:
		switch (targetUriSource) {
		case ANNOTATION:
			AnnotationEntity ae = annotationDAO.findAnnotationByIdentifier(targetIdentifier);
			if (ae==null) {
				return null;
			}
			owner = ae.getCreatedBy();
			break;
		case MEDIA:
			MediaEntity me = new MediaEntity(); //TODO: replace by service method!!
			owner = me.getCreatedBy();
			break;
		default:
			log.info("cannot create an ACL for type " + targetUriSource);
			return null;
		}
		
		AnnotationEntity entity = new AnnotationEntity();
		entity.setCreated(new Date());
		entity.setCreatedBy(owner);
		entity.setObjectUri(targetURI.toString());
		entity.setLastModified(new Date());
		entity.setScope(Scope.PUBLIC);
		entity.setTitle(String.format("ACL for %s with id %s", targetUriSource.toString(), targetIdentifier));
		entity.setType(MediaType.ACL);
		
		//store entity:
		annotationDAO.persist(entity);
		
		return new ACL(entity.toAnnotation());
	}
	
	@Override
	public String updateACL(String identifier, Annotation acl, String clientToken) {
		
		return null;
	}

	@Override
	public ACL findACLByObjectId(String identifier, URISource source) throws AnnotationNotFoundException {
		List<AnnotationEntity> list = annotationDAO.findAnnotationsForURI(URIBuilder.toURI(identifier, source, true), MediaType.ACL);
		if (list == null || list.isEmpty()) {
			throw new AnnotationNotFoundException();
		}
		return new ACL(list.get(0).toAnnotation());
	}
	
	@Override
	public ACL findACLByObjectURI(URI uri) throws AnnotationNotFoundException {
		List<AnnotationEntity> list = annotationDAO.findAnnotationsForURI(uri, MediaType.ACL);
		if (list == null || list.isEmpty()) {
			throw new AnnotationNotFoundException();
		}
		return new ACL(list.get(0).toAnnotation());
	}

	@Override
	public ACL findACLById(String identifier)
			throws AnnotationNotFoundException {
		AnnotationEntity entity = annotationDAO.findAnnotationByIdentifier(identifier);
		if (entity == null) {
			throw new AnnotationNotFoundException();
		}
		return new ACL(entity.toAnnotation());
	}
}
