package at.ait.dme.yuma.server.db;

import java.net.URI;
import java.util.List;

import at.ait.dme.yuma.server.db.entities.AnnotationEntity;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationTree;

public interface IAnnotationDAO {
	AnnotationEntity findAnnotationByIdentifier(String identifier);
	AnnotationTree findAnnotationsForURI(String URI);
	AnnotationTree findAnnotationsForURI(URI objectUri);
	List<Annotation> toAnnotations(List<AnnotationEntity> entities);
}
