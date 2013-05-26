package at.ait.dme.yuma.server.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.exception.InvalidAnnotationException;

public class User implements Serializable {
	
	private static final long serialVersionUID = 4246122693082846836L;

	private String username = null;
	
	private String gravatarHash = null;
	
	private String uri = null;
	
	private String client;
	
	public User(String username) {
		this.username = username;
	}
	
	public User(Map<String, String> map) throws InvalidAnnotationException {
		username = map.get(MapKeys.USER_NAME);
		if (username == null)
			throw new InvalidAnnotationException();
		
		gravatarHash = map.get(MapKeys.USER_GRAVATAR_HASH);
		uri = map.get(MapKeys.USER_URI);
	}

	public String getUsername() {
		return username;
	}

	public void setGravatarHash(String gravatarHash) {
		this.gravatarHash = gravatarHash;
	}

	public String getGravatarHash() {
		return gravatarHash;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
	
	@Override
	public String toString() {
		return String.format("User[username=%s,uri=%s]", getUsername(), getUri());
	}

	public Map<String, String> toMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put(MapKeys.USER_NAME, username);
		
		if (gravatarHash != null)
			map.put(MapKeys.USER_GRAVATAR_HASH, gravatarHash);
		
		if (uri != null)
			map.put(MapKeys.USER_URI, uri);
		
		return map;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (gravatarHash == null) {
			if (other.gravatarHash != null)
				return false;
		} else if (!gravatarHash.equals(other.gravatarHash))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result
				+ ((gravatarHash == null) ? 0 : gravatarHash.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}
	
	public AuthContext getAuthContext() {
		return new AuthContext(getUsername(), getClient());
	}

}
