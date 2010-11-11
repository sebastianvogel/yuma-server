package at.ait.dme.yuma.server.model;

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
