package at.ait.dme.yuma.server.model;

import java.net.URI;
import java.util.List;

import at.ait.dme.yuma.server.model.tag.SemanticTag;

public class ACL {
	public static final String TYPE_VALUE = "ACL";
	public static enum VALUES { READ, WRITE }
	public static final String CATCHALL = "*";
	
	private Annotation annotation;
	
	public ACL(Annotation annotation) {
		this.annotation = annotation;		
	}
	
	public Annotation toAnnotation() {
		return annotation;
	}
	
	public boolean hasReadPermission(URI userURI) {
		List<SemanticTag> tags = annotation.getTags();
		for (SemanticTag tag : tags) {
			if (!isACL(tag)) {
				continue;
			}
			if (isCatchAll(tag) && isRead(tag)) {
				return true;								
			}
			if (userURI.equals(tag.getURI())) {
				return isRead(tag);
			}
		}
		return false;
	}
	
	public boolean hasWritePermission(URI userURI) {
		List<SemanticTag> tags = annotation.getTags();
		for (SemanticTag tag : tags) {
			if (!isACL(tag)) {
				continue;
			}
			if (isCatchAll(tag) && isWrite(tag)) {
				return true;								
			}
			if (userURI.equals(tag.getURI())) {
				return isWrite(tag);
			}
		}
		return false;
	}
	
	public boolean isACL(SemanticTag tag) {
		return TYPE_VALUE.equals(tag.getType());
	}
	
	public boolean isCatchAll(SemanticTag tag) {
		return CATCHALL.equals(tag.getURI());
	}
	
	private boolean isWrite(SemanticTag tag) {
		return VALUES.WRITE.toString().equals(tag.getPrimaryLabel());				
	}
	
	private boolean isRead(SemanticTag tag) {
		return VALUES.READ.toString().equals(tag.getPrimaryLabel());				
	}
}
