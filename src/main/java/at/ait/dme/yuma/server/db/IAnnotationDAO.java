package at.ait.dme.yuma.server.db;

import java.net.URI;
import java.util.List;

import at.ait.dme.yuma.server.db.entities.AnnotationEntity;
import at.ait.dme.yuma.server.model.MediaType;

public interface IAnnotationDAO {
	AnnotationEntity findAnnotationByIdentifier(String identifier);
	List<AnnotationEntity> findAnnotationsForURI(String URI);
	List<AnnotationEntity> findAnnotationsForURI(URI objectUri, MediaType type);
}
