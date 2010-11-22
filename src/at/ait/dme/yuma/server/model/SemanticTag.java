package at.ait.dme.yuma.server.model;

import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import at.ait.dme.yuma.server.exception.InvalidAnnotationException;

/**
 * A semantic tag which is part of an annotation. A semantic
 * tag is a link to a data resource (denoted by a dereferencable
 * URI), plus various metadata about his resource (such as title,
 * alternative labels, description, etc.)
 * 
 * @author Rainer Simon
 */
public class SemanticTag {

	/**
	 * The tag URI
	 */
	private URI uri;

	/**
	 * The tag's primary label
	 */
	private String primaryLabel;

	/**
	 * The primary description/abstract of this tag
	 */
	private String primaryDescription;
	
	/**
	 * The language of the primary label and description
	 */
	private String primaryLang;
	
	/**
	 * The tag type (freely definable)
	 */
	private String type;
	
	/**
	 * The typed relation between annotated item and tag
	 */
	private SemanticRelation relation;

	/**
	 * Alternative labels in other languages in the form <lang, label>
	 */
	private HashMap<String, String> altLabels;
	
	/**
	 * Alternative descriptions in other languages in the form <lang, label>
	 */
	private HashMap<String, String> altDescriptions;
	
	/**
	 * Creates a semantic tag with the specified properties.
	 * @param map the semantic tag's properties
	 */
	@SuppressWarnings("unchecked")
	public SemanticTag(Map<String, Object> map) throws InvalidAnnotationException {
		try {
			this.uri = (URI) map.get(MapKeys.TAG_URI);
			this.primaryLabel = (String) map.get(MapKeys.TAG_LABEL);
			this.primaryDescription = (String) map.get(MapKeys.TAG_DESCRIPTION);
			this.primaryLang = (String) map.get(MapKeys.TAG_LANG);
			this.type = (String) map.get(MapKeys.TAG_TYPE);
			this.relation = (SemanticRelation) map.get(MapKeys.TAG_RELATION);
			this.altLabels = (HashMap<String, String>) map.get(MapKeys.TAG_ALT_LABELS);
			if (this.altLabels == null)
				this.altLabels = new HashMap<String, String>();
			this.altDescriptions = (HashMap<String, String>) map.get(MapKeys.TAG_ALT_DESCRIPTIONS);
			if (this.altDescriptions == null)
				this.altDescriptions = new HashMap<String, String>();
		} catch (Throwable t) {
			throw new InvalidAnnotationException(t.getMessage());
		}
		
		// Verify mandatory properties are set
		if (this.getURI() == null)
			throw new InvalidAnnotationException("Semantic tag URI may not be null");

		if (this.getPrimaryLabel() == null)
			throw new InvalidAnnotationException("Semantic tag label may not be null");
		
		if (this.getType() == null)
			throw new InvalidAnnotationException("Semantic tag type may not be null");
	}
		
	public URI getURI() {
		return uri;
	}

	public String getPrimaryLabel() {
		return primaryLabel;
	}

	public String getPrimaryDescription() {
		return primaryDescription;
	}
	
	public String getPrimaryLanguage() {
		return primaryLang;
	}
	
	public String getType() {
		return type;
	}
	
	public SemanticRelation getRelation() {
		return relation;
	}
		
	public String getAlternativeLabel(String language) {
		return altLabels.get(language);
	}
	
	public Set<String> getAlternativeLabelLanguages() {
		return altLabels.keySet();
	}
		
	public String getAlternativeDescription(String language) {
		return altDescriptions.get(language);
	}
	
	public Set<String> getAlternativeDescriptionLanguages() {
		return altDescriptions.keySet();
	}

	@Override
	public boolean equals(Object other) {
		// TODO revise!
		if (!(other instanceof SemanticTag))
			return false;
		
		SemanticTag t = (SemanticTag) other;
	
		// Compare mandatory properties
		if (!t.getURI().equals(this.getURI()))
			return false;
		
		if (!t.getPrimaryLabel().equals(this.getPrimaryLabel()))
			return false;
		
		if (!t.getType().equals(this.getType()))
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
	
	private boolean equalsNullable(Object a, Object b) {
		if (a == null)
			return b == null;
		
		if (b == null)
			return a == null;
		
		return a.equals(b);
	}
	
}
