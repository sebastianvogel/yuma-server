package at.ait.dme.yuma.server.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ait.dme.yuma.server.db.IAnnotationDAO;
import at.ait.dme.yuma.server.db.IAppClientDAO;
import at.ait.dme.yuma.server.db.IUserDAO;
import at.ait.dme.yuma.server.db.entities.AnnotationEntity;
import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;
import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.util.URIBuilder;

@Service
public class JPAACLService implements IACLService {
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	IUserDAO userDAO;
	
	@Autowired
	IAppClientDAO appClientDAO;
	
	@Autowired
	IAnnotationDAO annotationDAO;

	@Override
	public String createACL(ACL acl, String appClientToken) {
		AnnotationEntity entity = new AnnotationEntity(acl);
		//check if appClient exists:
		AppClientEntity appClient = appClientDAO.getAppClient(appClientToken);
				
		//check if user exists:
		UserEntity user = userDAO.findUser(acl.getCreatedBy(), appClient);
		if (user==null) {
			user = userDAO.createUser(acl.getCreatedBy(), appClient);
		}
		entity.setCreatedBy(user);
		
		em.persist(entity);
		return Long.toString(entity.getAnnotationId());
	}
	
	@Override
	public String updateACL(String identifier, Annotation acl, String clientToken) {
		
		return null;
	}

	@Override
	public Annotation findACLByObjectId(String identifier, URISource source) throws AnnotationNotFoundException {
		AnnotationTree tree = annotationDAO.findAnnotationsForURI(URIBuilder.toURI(identifier, source));
		if (tree == null) {
			throw new AnnotationNotFoundException();
		}
		return tree.asFlatList().get(0);
	}

	@Override
	public Annotation findACLById(String identifier)
			throws AnnotationNotFoundException {
		AnnotationEntity entity = annotationDAO.findAnnotationByIdentifier(identifier);
		if (entity == null) {
			throw new AnnotationNotFoundException();
		}
		return entity.toAnnotation();
	}
}
