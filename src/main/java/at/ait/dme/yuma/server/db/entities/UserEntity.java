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

import at.ait.dme.yuma.server.model.User;

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
	
	@Column(name="uri")
	private String uri;
	
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
		this.setUri(user.getUri());
		setCreated(new Date());
		setModified(new Date());
	}
	
	public User toUser() {
		User user = new User(username);
		user.setGravatarHash(gravatarHash);
		user.setUri(uri);
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

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
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
