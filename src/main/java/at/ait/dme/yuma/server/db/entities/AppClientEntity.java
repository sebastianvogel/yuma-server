package at.ait.dme.yuma.server.db.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@NamedQuery(name="find.client", query="SELECT a FROM AppClientEntity a WHERE a.clientToken=:clientToken")
@Table(name="app_client", 
	uniqueConstraints= @UniqueConstraint( columnNames = {"client_token"}))
public class AppClientEntity {
	
	public static final String QUERY_FIND = "find.client";
	public static final String PARAM_FIND = "clientToken";
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="client_token")
	private String clientToken;
	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date modified;
	
	/**
	 * get the name of the client
	 * @return
	 */
	public String getClientToken() {
		return clientToken;
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
		return String.format("AppClientEntity[id=%d,clientToken=%s]", 
				id, getClientToken());
	}
}
