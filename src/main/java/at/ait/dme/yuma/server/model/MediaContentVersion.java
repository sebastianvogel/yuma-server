package at.ait.dme.yuma.server.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

import at.ait.dme.yuma.server.util.URIBuilder;

public class MediaContentVersion implements Serializable{
	
	private static final long serialVersionUID = -7725874338168919952L;
	
	private Long version;
	
	private Media media;

	private String mimeType;
	
	private Date createdDate;
	
	private String fileName;
	
	private byte[] content = null;
	
	
	public MediaContentVersion(Long version, Media media,
			String mimetype, Date createdDate, String filename,
			byte[] content) {
		setVersion(version);
		setMedia(media);
		setMimeType(mimetype);
		setCreatedDate(createdDate);
		setFileName(filename);
		setContent(content);
	}
	
	public MediaContentVersion(String mimeType, String fileName,
			byte[] content) {
		setMimeType(mimeType);
		setFileName(fileName);
		setContent(content);
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}

	/**
	 * @param media the media to set
	 */
	public void setMedia(Media media) {
		this.media = media;
	}

	/**
	 * @return the media
	 */
	public Media getMediaObject() {
		return media;
	}
	/**
	 * @param media the media to set
	 */
	public void setMediaObject(Media media) {
		this.media = media;
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
	public void setMimeType(String mimetype) {
		this.mimeType = mimetype;
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
	 * @param content the content to set
	 */
	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String filename) {
		this.fileName = filename;
	}

	public URI getUri(boolean relative) {
		return URIBuilder.toURI(getMedia().getId().toString() + "/content/"
				+ getVersion().toString() + "/" + getFileName(),
				URISource.MEDIA, relative);
	}
	
}