package at.ait.dme.yuma.server.model;

/**
 * Annotation Scope (private or public)
 * 
 * @author Rainer Simon
 */
public enum AnnotationType {
	
	IMAGE ("image"),
	MAP   ("map"),
	VIDEO ("video"),
	AUDIO ("audio");

	private String s;
	
	private AnnotationType(String s) {
		this.s = s;
	}

	public String toString() {
		return s;
	}
	
	static AnnotationType fromString(String s) {
		if (s == null)
			return null;
		
		if (s.equals(IMAGE.s))
			return IMAGE;
			
		if (s.equals(MAP.s))
			return MAP;
		
		if (s.equals(VIDEO.s))
			return VIDEO;
		
		if (s.equals(AUDIO.s))
			return AUDIO;
		
		return null; 
	}
}
