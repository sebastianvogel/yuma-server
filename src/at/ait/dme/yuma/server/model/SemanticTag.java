package at.ait.dme.yuma.server.model;

import java.net.URI;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * A semantic tag which is part of an annotation. A semantic
 * tag is a link to a data resource (denoted by a dereferencable
 * URI), plus various metadata about his resource (such as title,
 * alternative labels, description, etc.)
 * 
 * @author Rainer Simon
 */
public class SemanticTag {

	private static final String URI = "uri";
	private static final String LANG = "lang";
	private static final String LABEL = "label";
	private static final String ALT_LABELS = "alt-labels";
	private static final String DESCRIPTION = "description";
	private static final String ALT_DESCRIPTIONS = "alt-descriptions";
	
	/**
	 * Primary SemanticTag properties
	 */
	private HashMap<String, Object> thisTag = new HashMap<String, Object>();
	
	/**
	 *  Labels in alternative languages
	 */
	private HashMap<String, String> altLabels = new HashMap<String, String>();
	
	/**
	 * Descriptions in alternative languages 
	 */
	private HashMap<String, String> altDescriptions = new HashMap<String, String>();
		
	public SemanticTag() {
		thisTag.put(ALT_LABELS, altLabels);
		thisTag.put(ALT_DESCRIPTIONS, altDescriptions);
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

}
