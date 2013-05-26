package at.ait.dme.yuma.server.model;

import java.net.URI;
import java.util.List;

import at.ait.dme.yuma.server.model.tag.SemanticTag;
import at.ait.dme.yuma.server.util.URIBuilder;


public class ACL {
	public static final String TYPE_VALUE = "ACL";
	public static final String VALUE_READ = "READ";
	
	private Annotation annotation;
	
	public ACL(Annotation annotation) {
		this.annotation = annotation;		
	}
	
	public Annotation toAnnotation() {
		return annotation;
	}
	
	public boolean hasWritePermission(String user) {
		List<SemanticTag> tags = annotation.getTags();
		URI userURI = URIBuilder.toURI(user, URISource.USER, true);
		for (SemanticTag tag : tags) {
			if (!TYPE_VALUE.equals(tag.getType())) {
				continue;
			}
			if (userURI.equals(tag.getURI())) {
				return VALUE_READ.equals(tag.getPrimaryLabel());
			}
		}
		return false;
	}
	
	public boolean hasReadPermission(String user) {
		return true;
	}
}
