package at.ait.dme.yuma.server.db.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="app_client", 
	uniqueConstraints= @UniqueConstraint( columnNames = {"client_token"}))
public class AppClientEntity {
	
	@Id
	private Long id;
	
	@Column(name="client_token")
	private String clientToken;
	
	/**
	 * get the name of the client
	 * @return
	 */
	public String getClientToken() {
		return clientToken;
	}
}
