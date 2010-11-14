package at.ait.dme.yuma.server.model;

/**
 * Annotation Scope (private or public)
 * 
 * @author Rainer Simon
 */
public enum Scope {
	
	PRIVATE("private"),
	PUBLIC("public");
	
	private String s;
	
	private Scope(String s) {
		this.s = s;
	}

	public String toString() {
		return s;
	}
	
	static Scope fromString(String s) {
		if (s.equalsIgnoreCase("public"))
			return PUBLIC;
		
		return PRIVATE; 
	}
}
