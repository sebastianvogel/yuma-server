package at.ait.dme.yuma.server.model;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

import at.ait.dme.yuma.server.util.URIBuilder;

public class Group implements Serializable, IOwnable {
	
	private static final long serialVersionUID = 3401085022089424952L;
	
	String name;
	User owner;
	Scope scope;
	
	List<User> members;
	
	
	public Group(String name, User owner, Scope scope) {
		this.name = name;
		this.owner = owner;
		this.scope = scope;
	}
	
	/**
	 * check if object, represented by given uri, is a member of this group
	 * @param uri
	 * @return
	 */
	public boolean hasMember(String uri) {
		if (members==null) {
			return false;
		}
		for (User user : members) {
			if (user.getUri().equals(uri)) {
				return true;
			}
		}
		return false;
	}
	
	public void setMembers(List<User> members) {
		this.members = members;
	}

	@Override
	public Scope getScope() {
		return scope;
	}

	@Override
	public User getCreatedBy() {
		return owner;
	}

	@Override
	public URI getURI(boolean relative) {
		return URIBuilder.toURI(name, URISource.GROUP, relative);
	}

}
