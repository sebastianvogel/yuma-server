package at.ait.dme.yuma.server.db.entities;

import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.model.MediaObject;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Entity implementation class for Entity: MediaObject
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name = "media.findByUri",
		query = "select m from MediaObjectEntity m where m.URI = :uri"),
	@NamedQuery(name = "media.findForUser",
		query = "select m from MediaObjectEntity m where m.createdBy = :user")
})
@Table(name="media", uniqueConstraints=@UniqueConstraint(columnNames={"uri"}))
public class MediaObjectEntity implements Serializable {
	
	@Id	
	@Column(name="uri")
	private String URI;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@ManyToOne
	@JoinColumn(name="created_by")
	private UserEntity createdBy;
	
	@OneToOne
	@JoinColumn(name="previous_version")
	private MediaObjectEntity previousVersion;
	
	@Column(name="mime_type")
	private String mimeType;
	
	@Lob
	private byte[] content;
	
	private static final long serialVersionUID = 1L;

	public MediaObjectEntity(MediaObject mediaObject) {
		this.setURI(mediaObject.getURI());
		this.setContent(mediaObject.getContent());
		this.setMimeType(mediaObject.getMimeType());
		this.setCreatedDate(new Date());
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
	public MediaObjectEntity getPreviousVersion() {
		return this.previousVersion;
	}

	public void setPreviousVersion(MediaObjectEntity previousVersion) {
		this.previousVersion = previousVersion;
	}   
	public String getMimeType() {
		return this.mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
	public void setContent(byte[] blob) {
		this.content = blob;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public MediaObject toMediaObject() {
		MediaObject m = new MediaObject(
				this.getURI(),
				this.getCreatedBy().toUser(), 
				this.getMimeType(),
				this.getContent());
		m.setCreatedDate(this.getCreatedDate());
		if(this.getPreviousVersion() != null) {
			m.setPreviousVersionUri(this.getPreviousVersion().getURI());
		}
		return m;
	}
}
