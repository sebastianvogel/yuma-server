package at.ait.dme.yuma.server.service;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ait.dme.yuma.server.db.IAnnotationDAO;
import at.ait.dme.yuma.server.db.IAppClientDAO;
import at.ait.dme.yuma.server.db.IUserDAO;
import at.ait.dme.yuma.server.db.entities.AnnotationEntity;
import at.ait.dme.yuma.server.exception.AnnotationNotFoundException;
import at.ait.dme.yuma.server.model.ACL;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.MediaType;
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
	public String createACL(URI uri, String clientToken, String username) {
		// TODO Auto-generated method stub
		return null;
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
