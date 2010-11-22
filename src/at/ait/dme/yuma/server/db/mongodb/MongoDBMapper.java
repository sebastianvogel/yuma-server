package at.ait.dme.yuma.server.db.mongodb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.MapKeys;
import at.ait.dme.yuma.server.model.SemanticTag;
import at.ait.dme.yuma.server.model.AnnotationType;
import at.ait.dme.yuma.server.model.Scope;

/**
 * Utility class that maps between model classes and the (just slightly
 * different) maps used by MongoDB.
 * 
 * @author Rainer Simon
 */
public class MongoDBMapper {
	
	/**
	 * Turns an annotation into a map suitable for storage inside MongoDB
	 * @param annotation the annotation
	 * @return the map
	 */
	public DBObject toDBObject(Annotation annotation) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put(MapKeys.ANNOTATION_ROOT_ID, annotation.getRootId());
		map.put(MapKeys.ANNOTATION_PARENT_ID, annotation.getParentId());
		map.put(MapKeys.ANNOTATION_OBJECT_ID, annotation.getObjectID());
		map.put(MapKeys.ANNOTATION_CREATED, annotation.getCreated());
		map.put(MapKeys.ANNOTATION_LAST_MODIFIED, annotation.getLastModified());
		map.put(MapKeys.ANNOTATION_CREATED_BY, annotation.getCreatedBy());
		map.put(MapKeys.ANNOTATION_TITLE, annotation.getTitle());
		map.put(MapKeys.ANNOTATION_TEXT, annotation.getText());
		map.put(MapKeys.ANNOTATION_TYPE, annotation.getType().toString());
		map.put(MapKeys.ANNOTATION_FRAGMENT, annotation.getFragment());
		map.put(MapKeys.ANNOTATION_SCOPE, annotation.getScope().toString());
		map.put(MapKeys.ANNOTATION_SEMANTIC_TAGS, toMap(annotation.getTags()));

		return new BasicDBObject(map);
	}
	
	/**
	 * Turns a list of tags into a map suitable for storage inside MongoDB
	 * @param tags the list of tags
	 * @return the map
	 */
	private List<Map<String, Object>> toMap(List<SemanticTag> tags) {
		return null;
	}
	
	/**
	 * Creates an annotation from a map retrieved from MongoDB.
	 * @param map the map
	 * @return the annotation
	 * @throws InvalidAnnotationException if something is wrong with the annotation
	 */
	public Annotation toAnnotation(DBObject dbo) throws InvalidAnnotationException {
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map = (HashMap<String, Object>) dbo.toMap();
			
			map.put(MapKeys.ANNOTATION_ID, dbo.get(MongoAnnotationDB.OID).toString());
			map.remove(MongoAnnotationDB.OID);

			String type = (String) map.get(MapKeys.ANNOTATION_TYPE);
			if (type != null)
				map.put(MapKeys.ANNOTATION_TYPE, AnnotationType.fromString(type));
			
			String scope = (String) map.get(MapKeys.ANNOTATION_SCOPE);
			if (scope != null)
				map.put(MapKeys.ANNOTATION_SCOPE, Scope.fromString(scope));
			
			return new Annotation(map);
		} catch (Throwable t) {
			throw new InvalidAnnotationException(t);
		}
	}	
	
}
