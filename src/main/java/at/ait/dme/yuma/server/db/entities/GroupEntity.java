package at.ait.dme.yuma.server.db.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import at.ait.dme.yuma.server.model.Group;
import at.ait.dme.yuma.server.model.Scope;
import at.ait.dme.yuma.server.model.User;

/**
 * Entity implementation class for Entity: GroupEntity
 *
 */
@Entity

@NamedQueries({
@NamedQuery(name = "group.find",
	query = "select g from GroupEntity g where g.name = :name"), 

@NamedQuery(name = "group.get",
	query = "select g from GroupEntity g WHERE g.createdBy.appClient = :appClient")
})

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
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<UserEntity> member = new ArrayList<UserEntity>();
	
	@Enumerated(EnumType.STRING)
	private Scope scope;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * convert to group object
	 * @return
	 */
	public Group toGroup() {
		Group g = new Group(name, getCreatedBy().toUser(), scope);
		List<User> members = new ArrayList<User>();
		for (UserEntity u : member) {
			members.add(u.toUser());
		}
		g.setMembers(members);
		return g;
	}
	
	/**
	 * add given user to this group
	 * @param user
	 */
	public void addMember(UserEntity user) {
		member.add(user);		
	}
	
	/**
	 * remove given user from group
	 * @param user
	 */
	public void removeMember(UserEntity user) {
		member.remove(user);
	}
	
	/**
	 * check if given UserEntity is member of this group
	 * @param user
	 * @return
	 */
	public boolean hasMember(UserEntity user) {
		if (member==null || member.isEmpty()) {
			return false;
		}
		return member.contains(user);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}

	public List<UserEntity> getMember() {
		return member;
	}

	public void setMember(List<UserEntity> member) {
		this.member = member;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
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
}
