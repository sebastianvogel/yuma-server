package at.ait.dme.yuma.server.controller;

import javax.servlet.http.HttpServletRequest;

import at.ait.dme.yuma.server.config.Config;

public class AuthContext {
	
	private String username;
	private String client;
	
	public AuthContext(HttpServletRequest request) {
		client = request.getRemoteUser();
		username = request.getHeader(Config.HeaderCheckReadPermissionsFor);		
	}
	
	public AuthContext(String username, String client) {
		this.username  = username;
		this.client = client;
	}

	public String getUsername() {
		return username;
	}

	public String getClient() {
		return client;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthContext other = (AuthContext) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AuthContext [username=" + username + ", client=" + client + "]";
	}
}
