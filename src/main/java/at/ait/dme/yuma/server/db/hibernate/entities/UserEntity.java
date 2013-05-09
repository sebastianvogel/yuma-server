package at.ait.dme.yuma.server.db.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import at.ait.dme.yuma.server.model.User;

@Entity
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
	
	public UserEntity() { }
	
	public UserEntity(User user) {
		this.setUsername(user.getUsername());
		this.setGravatarHash(user.getGravatarHash());
		this.setUri(user.getUri());
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
	
}
