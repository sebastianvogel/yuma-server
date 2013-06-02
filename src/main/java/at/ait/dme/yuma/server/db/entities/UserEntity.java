package at.ait.dme.yuma.server.db.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import at.ait.dme.yuma.server.model.URISource;
import at.ait.dme.yuma.server.model.User;
import at.ait.dme.yuma.server.util.URIBuilder;

@Entity
@NamedQuery(name = "user.find",
			query = "select u from UserEntity u where u.username = :username and u.appClient=:appclient")

@Table(name="user", uniqueConstraints=@UniqueConstraint(columnNames= {"username","app_client_id"}))
public class UserEntity {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(name="username")
	private String username;
	
	@Column(name="gravatar_hash")
	private String gravatarHash;
	
	@ManyToOne
	@JoinColumn(name="app_client_id")
	private AppClientEntity appClient;
	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date created;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date modified;
	
	public UserEntity() { }
	

	public UserEntity(User user) {
		this.setUsername(user.getUsername());
		this.setGravatarHash(user.getGravatarHash());
		setCreated(new Date());
		setModified(new Date());
	}
	
	public User toUser() {
		User user = new User(username);
		user.setGravatarHash(gravatarHash);
		user.setUri(getUri());
		if (appClient!=null && appClient.getClientToken()!=null) {
			user.setClient(appClient.getClientToken());
		}
		return user;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getUri() {
		String identifier = getAppClient().getClientToken().concat("/").concat(getUsername());
		return URIBuilder.toURI(identifier, URISource.USER, false).toString();
	}

	public AppClientEntity getAppClient() {
		return appClient;
	}

	public void setAppClient(AppClientEntity appClient) {
		this.appClient = appClient;
	}
	
	public Date getCreated() {
		return created;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appClient == null) ? 0 : appClient.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((gravatarHash == null) ? 0 : gravatarHash.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((modified == null) ? 0 : modified.hashCode());
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
		UserEntity other = (UserEntity) obj;
		if (appClient == null) {
			if (other.appClient != null)
				return false;
		} else if (!appClient.equals(other.appClient))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (gravatarHash == null) {
			if (other.gravatarHash != null)
				return false;
		} else if (!gravatarHash.equals(other.gravatarHash))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (modified == null) {
			if (other.modified != null)
				return false;
		} else if (!modified.equals(other.modified))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}


	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}
	
	public String toString() {
		return String.format("UserEntity[id=%d,username=%s,appClient=%s,uri=%s]",
				id, getUsername(), getAppClient(), getUri());
	}
}
