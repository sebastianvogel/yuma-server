package at.ait.dme.yuma.server.controller.json;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mongodb.util.JSON;

import at.ait.dme.yuma.server.controller.FormatHandler;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;
import at.ait.dme.yuma.server.model.AnnotationTree;
import at.ait.dme.yuma.server.model.AnnotationType;
import at.ait.dme.yuma.server.model.Annotation;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.MapKeys;
import at.ait.dme.yuma.server.model.SemanticRelation;
import at.ait.dme.yuma.server.model.SemanticTag;

/**
 * Format handler for JSON.
 * 
 * @author Rainer Simon
 */
public class JSONFormatHandler implements FormatHandler {
	
	private static final String KEY_REPLIES = "replies";
	
	@Override
	public Annotation parse(String serialized)
		throws InvalidAnnotationException {
		
		try {
			Object obj = JSON.parse(serialized);
			
			if (obj instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) obj;
				if (list.size() > 0) {
					return toAnnotation(list.get(0));
				} else {
					throw new InvalidAnnotationException();
				}
			} else if (obj instanceof Map<?, ?>) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) obj;
				return toAnnotation(map);
			} else {
				throw new InvalidAnnotationException();
			}
		} catch (Throwable t) {
			throw new InvalidAnnotationException(t);
		}
	}

	/**
	 * Converts a JSON-formatted map to an annotation.
	 * @param map the JSON-formatted map
	 * @return the annotation
	 * @throws InvalidAnnotationException if the map does not contain valid annotation data
	 */
	public Annotation toAnnotation(Map<String, Object> map) throws InvalidAnnotationException {	
		String type = (String) map.get(MapKeys.ANNOTATION_TYPE);
		if (type != null)
			map.put(MapKeys.ANNOTATION_TYPE, AnnotationType.valueOf(type.toUpperCase()));
		
		String scope = (String) map.get(MapKeys.ANNOTATION_SCOPE);
		if (scope != null)
			map.put(MapKeys.ANNOTATION_SCOPE, Scope.valueOf(scope.toUpperCase()));
		
		try {
			Date created = new Date((Long) map.get(MapKeys.ANNOTATION_CREATED));
			map.put(MapKeys.ANNOTATION_CREATED, created);
			
			Date lastModified = new Date((Long) map.get(MapKeys.ANNOTATION_LAST_MODIFIED));
			map.put(MapKeys.ANNOTATION_LAST_MODIFIED, lastModified);
		} catch (Throwable t) {
			throw new InvalidAnnotationException(t);
		}

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> tags = (List<Map<String, Object>>) map.get(MapKeys.ANNOTATION_SEMANTIC_TAGS);
		if (tags != null)
			map.put(MapKeys.ANNOTATION_SEMANTIC_TAGS, new JSONFormatHandler().toSemanticTags(tags));
			
		return new Annotation(map);
	}	

	private List<SemanticTag> toSemanticTags(List<Map<String, Object>> maps) throws InvalidAnnotationException {
		ArrayList<SemanticTag> tags = new ArrayList<SemanticTag>();
		
		for (Map<String, Object> map : maps) {
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
	
	@Override
	public String serialize(Annotation annotation) {
		return JSON.serialize(toJSONFormat(annotation));
	}

	@Override
	public String serialize(AnnotationTree tree) {
		return JSON.serialize(toJSONFormat(tree));
	}
	
	@Override
	public String serialize(List<Annotation> annotations)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Converts an annotation to a JSON-formatted map
	 * @param annotation the annotation
	 * @return the JSON-formatted map
	 */
	public Map<String, Object> toJSONFormat(Annotation annotation) {
		Map<String, Object> map = annotation.toMap();
		
		map.put(MapKeys.ANNOTATION_TYPE, annotation.getType().toString());
		map.put(MapKeys.ANNOTATION_SCOPE, annotation.getScope().toString());
		map.put(MapKeys.ANNOTATION_CREATED, annotation.getCreated().getTime());
		map.put(MapKeys.ANNOTATION_LAST_MODIFIED, annotation.getLastModified().getTime());
		map.put(MapKeys.ANNOTATION_SEMANTIC_TAGS, toJSONFormat(annotation.getTags()));
		
		return map;
	}
	
	private ArrayList<Map<String, Object>> toJSONFormat(List<SemanticTag> tags) {
		ArrayList<Map<String, Object>> maps = new ArrayList<Map<String,Object>>();
		
		if (tags != null) {
			for (SemanticTag tag : tags) {
				Map<String, Object> map = tag.toMap();
				map.put(MapKeys.TAG_URI, tag.getURI().toString());
				
				SemanticRelation relation = tag.getRelation();
				if (relation != null)
					map.put(MapKeys.TAG_RELATION, relation.toMap());
				
				maps.add(map);
			}
		}
		
		return maps;
	}
	
	public List<Map<String, Object>> toJSONFormat(AnnotationTree tree) {
		List<Map<String, Object>> jsonTree = new ArrayList<Map<String,Object>>();
		
		for (Annotation a : tree.getRootAnnotations()) {
			Map<String, Object> root = toJSONFormat(a);
			buildTree(root, tree);
			jsonTree.add(root);
		}
		
		return jsonTree;
	}
	
	private void buildTree(Map<String, Object> parent, AnnotationTree tree) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> repliesList = (List<Map<String, Object>>) parent.get(KEY_REPLIES);
		if (repliesList == null) {
			repliesList = new ArrayList<Map<String, Object>>();
		}
		
		String parentId = (String) parent.get(MapKeys.ANNOTATION_ID);
		for (Annotation a : tree.getChildren(parentId)) {
			// Append each reply to the list...
			Map<String, Object> reply = toJSONFormat(a); 
			repliesList.add(reply);
			
			// ...and continue recursively
			buildTree(reply, tree);
		}
		
		parent.put(KEY_REPLIES, repliesList);
	}
		
}
