package at.ait.dme.yuma.server.model;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import at.ait.dme.yuma.server.model.tag.SemanticTag;
import at.ait.dme.yuma.server.util.URIBuilder;

public class ACL {
	public static final String TYPE_VALUE = "ACL";
	public static final String CATCHALL = "*";
	
	private enum VALUE { READ, WRITE }
	private Annotation annotation;
	
	public ACL(Annotation annotation) {
		this.annotation = annotation;		
	}
	
	public Annotation toAnnotation() {
		return annotation;
	}
	
	public class Entity {
		private final URI subject;
		private final boolean readPermission;
		private final boolean writePermission;
		
		public Entity(URI subject, boolean readPermission, boolean writePermission) {
			this.subject = subject;
			this.readPermission = readPermission;
			this.writePermission = writePermission;
		}
		public URI getSubject() {
			return subject;
		}
		public boolean hasReadPermission() {
			return readPermission;
		}
		public boolean hasWritePermission()  {
			return writePermission;
		}
		public boolean isCatchAll() {
			return subject.toString().equals(CATCHALL);
		}
		public boolean isGroupSubject() {
			return URIBuilder.isURISource(URISource.GROUP, getSubject().toString());
		}
	}
	
	/**
	 * get ACL entities
	 * @return
	 */
	public Set<Entity> entities() {
		Set<Entity> set = new HashSet<ACL.Entity>();
		for (SemanticTag tag : annotation.getTags()) {
			if (!isACL(tag)) {
				continue;
			}
			set.add(new Entity(tag.getURI(), isRead(tag), isWrite(tag)));			
		}
		return set;
	}
	
	private boolean isACL(SemanticTag tag) {
		return TYPE_VALUE.equals(tag.getType());
	}
	
	private boolean isWrite(SemanticTag tag) {
		return VALUE.WRITE.toString().equals(tag.getPrimaryLabel());				
	}
	
	private boolean isRead(SemanticTag tag) {
		return VALUE.READ.toString().equals(tag.getPrimaryLabel());				
	}
}
