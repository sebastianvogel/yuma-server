package at.ait.dme.yuma.server.db.entities;

import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.model.Scope;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Media
 *
 */
@Entity
@Table(name="media", uniqueConstraints=@UniqueConstraint(columnNames={"name", "created_by", "version"}))
public class MediaEntity implements Serializable {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@ManyToOne
	@JoinColumn(name="created_by", nullable=false)
	private UserEntity createdBy;

	@OneToOne
	@JoinColumn(name="previous_version")
	private MediaEntity previousVersion;
	
	@Column(name="mime_type")
	private String mimeType;
	
	@Enumerated(EnumType.STRING)
	private Scope scope;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="version", nullable=false)
	private Integer version = 1;
	
	@Lob
	private byte[] media;
	
	private static final long serialVersionUID = 1L;

	public MediaEntity() {
		super();
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
	
	public void setMedia(byte[] blob) {
		this.media = blob;
	}
	
	public byte[] getMedia() {
		return media;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
