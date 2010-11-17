package at.ait.dme.yuma.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import at.ait.dme.yuma.server.exception.AnnotationFormatException;

import com.mongodb.util.JSON;

/**
 * The annotation.
 * 
 * @author Rainer Simon
 */
public class Annotation extends AbstractModelEntity {
	
	public static final String ROOT_ID = "root-id";
	public static final String PARENT_ID = "parent-id";
	public static final String OBJECT_ID = "object-id";
	public static final String CREATED = "created";
	public static final String LAST_MODIFIED = "last-modified";
	public static final String CREATED_BY = "created-by";
	public static final String TITLE = "title";
	public static final String TEXT = "text";
	public static final String TYPE = "type";
	public static final String FRAGMENT = "fragment";
	public static final String SCOPE = "scope";
	public static final String SEMANTIC_TAGS = "tags";
	
	private String annotationId;

	private Map<String, Object> thisAnnotation = new HashMap<String, Object>();
	
	private List<Map<String, Object>> tags = new ArrayList<Map<String, Object>>();
	
	private Logger log = Logger.getLogger(Annotation.class);
	
	public Annotation() {
		thisAnnotation.put(SEMANTIC_TAGS, tags);
	}
	
	public Annotation(String json) throws AnnotationFormatException {
		this((Map<?, ?>) JSON.parse(json));
	}
	
	@SuppressWarnings("unchecked")
	public Annotation(Map<?, ?> map) throws AnnotationFormatException {
		try {
			thisAnnotation = (Map<String, Object>) map;
			
			if (thisAnnotation.get(SEMANTIC_TAGS) != null) {
				tags = (List<Map<String, Object>>) thisAnnotation.get(SEMANTIC_TAGS);
			} else {
				thisAnnotation.put(SEMANTIC_TAGS, tags);
			}
		} catch (Throwable t) {
			throw new AnnotationFormatException(t.getMessage());
		}
		
		// Verify mandatory fields
		if (this.getObjectID() == null)
			throw new AnnotationFormatException("ObjectID may not be null");

		if (this.getCreated() == null)
			throw new AnnotationFormatException("Creation timestamp may not be null");
		
		if (this.getLastModified() == null)
			throw new AnnotationFormatException("Last modification timestamp may not be null");
		
		if (this.getCreatedBy() == null)
			throw new AnnotationFormatException("Creator user name may not be null");
		
		if (this.getType() == null)
			throw new AnnotationFormatException("Annotation type may not be null");

		if (this.getScope() == null)
			throw new AnnotationFormatException("Annotation scope may not be null");	
	}

	public void setAnnotationID(String annotationId) {
		this.annotationId = annotationId;
	}

	public String getAnnotationID() {
		return annotationId;
	}

	public void setRootId(String rootId) {
		thisAnnotation.put(ROOT_ID, rootId);
	}

	public String getRootId() {
		return (String) thisAnnotation.get(ROOT_ID);
	}

	public void setParentId(String parentId) {
		thisAnnotation.put(PARENT_ID, parentId);
	}

	public String getParentId() {
		return (String) thisAnnotation.get(PARENT_ID);
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
	
	public void setType(AnnotationType type) {
		thisAnnotation.put(TYPE, type.toString());
	}
	
	public AnnotationType getType() {
		return AnnotationType.fromString((String) thisAnnotation.get(TYPE));
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
		ArrayList<SemanticTag> tagList = new ArrayList<SemanticTag>();
		if (tags != null) {
			for (int i=0; i<tags.size(); i++) {
				try {
					tagList.add(new SemanticTag(tags.get(i)));
				} catch (AnnotationFormatException e) {
					// Should never happen
					log.warn("Could not deserialize semantic tag: " + tags.get(i) + " in annotation " + annotationId);
				}
			}
		}
		return tagList;
	}
	
	public Map<String, Object> toMap() {
		return thisAnnotation;
	}
	
	@Override
	public String toString() {
		return JSON.serialize(thisAnnotation);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Annotation))
			return false;
		
		Annotation a = (Annotation) other;
		
		// Compare mandatory properties
		if (!a.getObjectID().equals(this.getObjectID()))
			return false;

		if (!a.getCreated().equals(this.getCreated()))
			return false;
		
		if (!a.getLastModified().equals(this.getLastModified()))
			return false;
		
		if (!a.getCreatedBy().equals(this.getCreatedBy()))
			return false;
		
		if (!a.getType().equals(this.getType()))
			return false;

		if (!a.getScope().equals(this.getScope()))
			return false;
		
		// Compare optional properties (may be null!)
		if (!equalsNullable(a.getRootId(), this.getRootId()))
			return false;
	
		if (!equalsNullable(a.getParentId(), this.getParentId()))
			return false;
						
		if (!equalsNullable(a.getTitle(), this.getTitle()))
			return false;
		
		if (!equalsNullable(a.getText(), this.getText()))
			return false;
		
		if (!equalsNullable(a.getFragment(), this.getFragment()))
			return false;
				
		
		List<SemanticTag> myTags = this.getTags();
		List<SemanticTag> othersTags = a.getTags();
		
		if (myTags.size() != othersTags.size())
			return false;
	
		if (!myTags.containsAll(othersTags))
			return false;
		
		if (!othersTags.containsAll(myTags))
			return false;
		
		return true;
	}
		
}
