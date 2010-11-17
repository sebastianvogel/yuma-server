package at.ait.dme.yuma.server.model;

import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import at.ait.dme.yuma.server.exception.AnnotationFormatException;

/**
 * A semantic tag which is part of an annotation. A semantic
 * tag is a link to a data resource (denoted by a dereferencable
 * URI), plus various metadata about his resource (such as title,
 * alternative labels, description, etc.)
 * 
 * @author Rainer Simon
 */
public class SemanticTag extends AbstractModelEntity {

	private static final String URI = "uri";
	private static final String LANG = "lang";
	private static final String LABEL = "label";
	private static final String ALT_LABELS = "alt-labels";
	private static final String DESCRIPTION = "description";
	private static final String ALT_DESCRIPTIONS = "alt-descriptions";
	
	/**
	 * Primary SemanticTag properties
	 */
	private Map<String, Object> thisTag = new HashMap<String, Object>();
	
	/**
	 *  Labels in alternative languages
	 */
	private Map<String, String> altLabels = new HashMap<String, String>();
	
	/**
	 * Descriptions in alternative languages 
	 */
	private Map<String, String> altDescriptions = new HashMap<String, String>();
		
	public SemanticTag() {
		thisTag.put(ALT_LABELS, altLabels);
		thisTag.put(ALT_DESCRIPTIONS, altDescriptions);
	}
	
	@SuppressWarnings("unchecked")
	public SemanticTag(Map<String, Object> map) throws AnnotationFormatException {
		thisTag = map;
		try {
			if (map.get(ALT_LABELS) != null) {
				altLabels = (Map<String, String>) map.get(ALT_LABELS);
			} else {
				thisTag.put(ALT_LABELS, altLabels);
			}
			
			if (map.get(ALT_DESCRIPTIONS) != null) {
				altDescriptions = (Map<String, String>) map.get(ALT_DESCRIPTIONS);
			} else {
				thisTag.put(ALT_DESCRIPTIONS, altDescriptions);
			}
		} catch (Throwable t) {
			throw new AnnotationFormatException(t.getMessage());
		}
		
		// Verify mandatory fields
		if (this.getURI() == null)
			throw new AnnotationFormatException("Semantic tag URI may not be null");

		if (this.getPrimaryLabel() == null)
			throw new AnnotationFormatException("Semantic tag label may not be null");
	}
	
	public void setURI(URI uri) {
		thisTag.put(URI, uri);
	}
	
	public URI getURI() {
		return (URI) thisTag.get(URI);
	}
	
	public void setPrimaryLanguage(String language) {
		thisTag.put(LANG, language);
	}
	
	public String getPrimaryLanguage() {
		return (String) thisTag.get(LANG);
	}
	
	public void setPrimaryLabel(String label) {
		thisTag.put(LABEL, label);
	}

	public String getPrimaryLabel() {
		return (String) thisTag.get(LABEL);
	}
	
	public void addAlternativeLabel(String label, String language) {
		altLabels.put(language, label);
	}
	
	public String getAlternativeLabel(String language) {
		return altLabels.get(language);
	}
	
	public Set<String> getAlternativeLabelLanguages() {
		return altLabels.keySet();
	}
	
	public void setPrimaryDescription(String description) {
		thisTag.put(DESCRIPTION, description);
	}
	
	public String getPrimaryDescription() {
		return (String) thisTag.get(DESCRIPTION);
	}
	
	public void addAlternativeDescription(String description, String language) {
		altDescriptions.put(language, description);
	}
	
	public String getAlternativeDescription(String language) {
		return altDescriptions.get(language);
	}
	
	public Set<String> getAlternativeDescriptionLanguages() {
		return altDescriptions.keySet();
	}

	public Map<String, Object> toMap() {
		return thisTag;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof SemanticTag))
			return false;
		
		SemanticTag t = (SemanticTag) other;
	
		// Compare mandatory properties
		if (!t.getURI().equals(this.getURI()))
			return false;
		
		if (!t.getPrimaryLabel().equals(this.getPrimaryLabel()))
			return false;
		
		// Compare optional properties (may be null!)
		if (!equalsNullable(t.getPrimaryLanguage(), this.getPrimaryLanguage()))
			return false;
			
		if (!equalsNullable(t.getPrimaryDescription(), this.getPrimaryDescription()))
			return false;
		
		if (!equals(t.altLabels, this.altLabels))
			return false;
		
		if (!equals(t.altDescriptions, this.altDescriptions))
			return false;

		return true;
	}

	private boolean equals(Map<String, String> a, Map<String, String> b) {
		if (a.size() != b.size())
			return false;
		
		Set<String> aKeys = a.keySet();
		Set<String> bKeys = b.keySet();
		
		if (!aKeys.containsAll(bKeys))
			return false;
		
		for (String key : aKeys) {
			if (!a.get(key).equals(b.get(key)))
				return false;		
		}
		
		return true;
	}
	
}
