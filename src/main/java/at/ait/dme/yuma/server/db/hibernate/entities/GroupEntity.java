package at.ait.dme.yuma.server.db.hibernate.entities;

import at.ait.dme.yuma.server.db.hibernate.entities.UserEntity;
import java.io.Serializable;
import java.lang.String;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: GroupEntity
 *
 */
@Entity
@Table(name="usergroup", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class GroupEntity implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;
	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column
    @Temporal(TemporalType.TIMESTAMP)
	private Date modified;
	
	@ManyToOne
	@JoinColumn(name="owner")
	private UserEntity createdBy;
	
	@ManyToMany
	private List<UserEntity> member;
	
	private static final long serialVersionUID = 1L;

	public GroupEntity() {
		super();
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}   
	public Date getModified() {
		return this.modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}   
	public UserEntity getOwner() {
		return this.createdBy;
	}

	public void setOwner(UserEntity owner) {
		this.createdBy = owner;
	}
   
}
