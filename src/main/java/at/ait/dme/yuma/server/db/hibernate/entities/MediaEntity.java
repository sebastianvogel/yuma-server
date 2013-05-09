package at.ait.dme.yuma.server.db.hibernate.entities;

import at.ait.dme.yuma.server.db.hibernate.entities.UserEntity;
import java.io.Serializable;
import java.lang.String;
import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Media
 *
 */
@Entity
@Table(name="media", uniqueConstraints=@UniqueConstraint(columnNames={"uri"}))
public class MediaEntity implements Serializable {
	
	@Id
	private Long id;
	
	@Column(name="uri")
	private String URI;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@ManyToOne
	@JoinColumn(name="created_by")
	private UserEntity createdBy;
	
	@ManyToOne
	@JoinColumn(name="previous_version")
	private MediaEntity previousVersion;
	
	private String mimeType;
	
	private static final long serialVersionUID = 1L;

	public MediaEntity() {
		super();
	}   
	public String getURI() {
		return this.URI;
	}

	public void setURI(String URI) {
		this.URI = URI;
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
	public MediaEntity getPreviousVersion() {
		return this.previousVersion;
	}

	public void setPreviousVersion(MediaEntity previousVersion) {
		this.previousVersion = previousVersion;
	}   
	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
   
}
