package at.ait.dme.yuma.server.model;

import java.util.Map;
import java.util.HashMap;

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
