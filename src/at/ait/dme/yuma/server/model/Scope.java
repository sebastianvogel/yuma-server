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
		if (s == null)
			return null;
		
		if (s.equalsIgnoreCase("public"))
			return PUBLIC;
		
		if (s.equalsIgnoreCase("private"))
			return PRIVATE; 
		
		return null;
	}
	
}
