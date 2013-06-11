package at.ait.dme.yuma.server.db.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import at.ait.dme.yuma.server.model.MediaContentVersion;

@Entity
@Table(name="mediaversion")
public class MediaContentVersionEntity {
	
	@Id
	@GeneratedValue
	private Long version;
	
	@Column(name="filename")
	private String filename;
	
	@ManyToOne
	@JoinColumn(name="media_id")
	private MediaEntity mediaEntity;
		
	@Column(name="mime_type")
	private String mimeType;
	
	@Column(name="created_date")
	private Date createdDate;
		
	@Lob
	private byte[] content;

	public MediaContentVersionEntity(MediaContentVersion mediaContent) {
		this.setContent(mediaContent.getContent());
		this.setMimeType(mediaContent.getMimeType());
	}

	/**
	 * @return the mediaEntity
	 */
	public MediaEntity getMediaEntity() {
		return mediaEntity;
	}

	/**
	 * @param mediaEntity the mediaEntity to set
	 */
	public void setMediaEntity(MediaEntity mediaEntity) {
		this.mediaEntity = mediaEntity;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @return the content
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public MediaContentVersion toMediaContentVersion() {
		MediaContentVersion mc = new MediaContentVersion(
				getVersion(), 
				getMediaEntity().toMedia(), 
				getMimeType(), 
				getCreatedDate(),
				getFilename(),
				getContent());
		return mc;
	}

}