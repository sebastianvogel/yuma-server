package at.ait.dme.yuma.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ait.dme.yuma.server.exception.AnnotationFormatException;

import com.mongodb.util.JSON;

/**
 * The annotation.
 * 
 * @author Rainer Simon
 */
public class Annotation {
	
	private static final String ANNOTATION_ID = "id";
	private static final String ROOT_ID = "root-id";
	private static final String PARENT_ID = "parent-id";
	private static final String OBJECT_ID = "object-id";
	private static final String CREATED = "created";
	private static final String LAST_MODIFIED = "last-modified";
	private static final String CREATED_BY = "created-by";
	private static final String TITLE = "title";
	private static final String TEXT = "text";
	private static final String FRAGMENT = "fragment";
	private static final String SCOPE = "scope";
	private static final String SEMANTIC_TAGS = "tags";
	private static final String REPLIES = "replies";

	private Map<String, Object> thisAnnotation = new HashMap<String, Object>();
	
	private List<Map<String, String>> tags = new ArrayList<Map<String, String>>();
	
	private List<String> replies = new ArrayList<String>();
	
	public Annotation() {
		thisAnnotation.put(SEMANTIC_TAGS, tags);
		thisAnnotation.put(REPLIES, replies);
	}
	
	@SuppressWarnings("unchecked")
	public Annotation(String json) throws AnnotationFormatException {
		try {
			thisAnnotation = (Map<String, Object>) JSON.parse(json);
			tags = (List<Map<String, String>>) thisAnnotation.get(SEMANTIC_TAGS);
			replies = (List<String>) thisAnnotation.get(REPLIES);
		} catch (Throwable t) {
			throw new AnnotationFormatException(t.getMessage());
		}
	}

	public void setAnnotationID(Long annotationID) {
		thisAnnotation.put(ANNOTATION_ID, annotationID);
	}

	public Long getAnnotationID() {
		return (Long) thisAnnotation.get(ANNOTATION_ID);
	}

	public void setRootId(Long rootId) {
		thisAnnotation.put(ROOT_ID, rootId);
	}

	public Long getRootId() {
		return (Long) thisAnnotation.get(ROOT_ID);
	}

	public void setParentId(Long parentId) {
		thisAnnotation.put(PARENT_ID, parentId);
	}

	public Long getParentId() {
		return (Long) thisAnnotation.get(PARENT_ID);
	}

	public void setObjectID(String objectID) {
		thisAnnotation.put(OBJECT_ID, objectID);
	}

	public String getObjectID() {
		return (String) thisAnnotation.get(OBJECT_ID);
	}

	public void setCreated(Date created) {
		thisAnnotation.put(CREATED, created);
	}

	public Date getCreated() {
		return (Date) thisAnnotation.get(CREATED);
	}

	public void setLastModified(Date lastModified) {
		thisAnnotation.put(LAST_MODIFIED, lastModified);
	}

	public Date getLastModified() {
		return (Date) thisAnnotation.get(LAST_MODIFIED);
	}

	public void setCreatedBy(String createdBy) {
		thisAnnotation.put(CREATED_BY, createdBy);
	}

	public String getCreatedBy() {
		return (String) thisAnnotation.get(CREATED_BY);
	}

	public void setTitle(String title) {
		thisAnnotation.put(TITLE, title);
	}

	public String getTitle() {
		return (String) thisAnnotation.get(TITLE);
	}

	public void setText(String text) {
		thisAnnotation.put(TEXT, text);
	}

	public String getText() {
		return (String) thisAnnotation.get(TEXT);
	}

	public void setFragment(String fragment) {
		thisAnnotation.put(FRAGMENT, fragment);
	}

	public String getFragment() {
		return (String) thisAnnotation.get(FRAGMENT);
	}

	public void setScope(Scope scope) {
		thisAnnotation.put(SCOPE, scope.toString());
	}

	public Scope getScope() {
		return Scope.fromString((String) thisAnnotation.get(SCOPE));
	}

	public void addTag(SemanticTag tag) {
		tags.add(tag.toMap());
	}

	public List<SemanticTag> getTags() {
		return null;
	}

	public void addReply(String annotationID) {
		replies.add(annotationID);
	}

	public List<String> getReplies() {
		return replies;
	}
	
	public Map<String, Object> toMap() {
		return thisAnnotation;
	}
	
	@Override
	public String toString() {
		return JSON.serialize(thisAnnotation);
	}
	
}
