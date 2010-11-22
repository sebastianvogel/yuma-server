package at.ait.dme.yuma.server.db.mongodb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.AnnotationType;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.SemanticTag;

/**
 * Utility class that maps between model classes and maps used by
 * MongoDB.
 * 
 * @author Rainer Simon
 */
public class MongoDBMapper {
		
	public Map<String, Object> toMap(Annotation annotation) {
		return null;
	}
	
	public Map<String, Object> toMap(SemanticTag tag) {
		return null;
	}

	public Annotation toAnnotation(Map<String, Object> map) throws InvalidAnnotationException {
		// MongoDB mapping compatible (although not quite identical) to model mapping
		return null;
	}
	
	public SemanticTag toSemanticTag(Map<String, Object> map) throws InvalidAnnotationException {
		// MongoDB mapping compatible (although not quite identical) to model mapping
		return null;
	}
	
	
}
