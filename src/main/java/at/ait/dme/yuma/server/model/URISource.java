package at.ait.dme.yuma.server.model;

public enum URISource {
	ANNOTATION, MEDIA, USER, GROUP;
	
	public String toURIFragment() {
		return this.toString().toLowerCase().concat("/");
	}
}
