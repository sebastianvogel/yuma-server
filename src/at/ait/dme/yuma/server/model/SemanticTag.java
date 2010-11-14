package at.ait.dme.yuma.server.model;

import java.util.Map;
import java.util.HashMap;

/**
 * A semantic tag which is part of an annotation. A semantic
 * tag is a link to a data resource (denoted by a dereferencable
 * URI), plus various metadata about his resource (such as title,
 * alternative labels, description, etc.)
 * 
 * TODO implement this (re-using code from front-end)
 * 
 * @author Rainer Simon
 */
public class SemanticTag {
	
	private static final String TITLE = "title";
	
	private HashMap<String, String> thisTag= new HashMap<String, String>();

	public void setTitle(String title) {
		thisTag.put(TITLE, title);
	}

	public String getTitle() {
		return thisTag.get(TITLE);
	}
	
	public Map<String, String> toMap() {
		return thisTag;
	}

}
