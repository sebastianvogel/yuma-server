package at.ait.dme.yuma.server.db.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.model.Media;
import at.ait.dme.yuma.server.model.Scope;

/**
 * Entity implementation class for Entity: Media
 *
 */
@Entity
@Table(name="media")
public class MediaEntity implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne
	@JoinColumn(name="created_by")
	private UserEntity createdBy;
	
	@Enumerated(EnumType.STRING)
	private Scope scope;
	
	private static final long serialVersionUID = 1L;

	public MediaEntity(Media media) {
		if (media.getScope() != null) {
			this.setScope(Config.getInstance().getScopePolicy());
		}
		this.setCreatedDate(new Date());
	}
	
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}   
	public UserEntity getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(UserEntity createdBy) {
		this.createdBy = createdBy;
	}   
	
	/**
	 * @return the scope
	 */
	public Scope getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	public Media toMedia() {
		Media m = new Media(
				this.getCreatedBy().toUser());
		m.setCreatedDate(this.getCreatedDate());
		m.setScope(getScope());
		m.setUpdatedDate(this.getUpdatedDate());
		return m;
	}

	public void update(Media media) {
		if (media.getScope() != null) {
			this.setScope(media.getScope());
		}
		this.setUpdatedDate(updatedDate);
	}
}