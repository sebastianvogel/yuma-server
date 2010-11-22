package at.ait.dme.yuma.server.db.mongodb;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.MapKeys;
import at.ait.dme.yuma.server.model.SemanticRelation;
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
		Map<String, Object> map = annotation.toMap();

		map.put(MapKeys.ANNOTATION_TYPE, annotation.getType().toString());
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
		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		
		for (SemanticTag tag : tags) {
			Map<String, Object> map = tag.toMap();
			map.put(MapKeys.TAG_URI, tag.getURI().toString());
			
			SemanticRelation relation = tag.getRelation();
			if (relation != null)
				map.put(MapKeys.TAG_RELATION, relation.toMap());
			
			maps.add(map);
		}
		
		return maps;
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

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> tags = (List<Map<String, Object>>) map.get(MapKeys.ANNOTATION_SEMANTIC_TAGS);
			if (tags != null)
				map.put(MapKeys.ANNOTATION_SEMANTIC_TAGS, toSemanticTags(tags));
				
			return new Annotation(map);
		} catch (Throwable t) {
			throw new InvalidAnnotationException(t);
		}
	}	
	
	private List<SemanticTag> toSemanticTags(List<Map<String, Object>> jsonFormat) throws InvalidAnnotationException {
		ArrayList<SemanticTag> tags = new ArrayList<SemanticTag>();
		
		for (Map<String, Object> map : jsonFormat) {
			String uri = (String) map.get(MapKeys.TAG_URI);
			if (uri != null) {
				try {
					map.put(MapKeys.TAG_URI, new URI(uri));
				} catch (URISyntaxException e) {
					throw new InvalidAnnotationException(e);
				}
			}

			@SuppressWarnings("unchecked")
			Map<String, String> relation = (Map<String, String>) map.get(MapKeys.TAG_RELATION);
			if (relation != null)
				map.put(MapKeys.TAG_RELATION, new SemanticRelation(relation));
			
			tags.add(new SemanticTag(map));
		}
		
		return tags;
	}	
	
}
