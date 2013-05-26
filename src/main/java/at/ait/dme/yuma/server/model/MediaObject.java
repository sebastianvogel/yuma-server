package at.ait.dme.yuma.server.model;

import java.io.Serializable;
import java.util.Date;


import at.ait.dme.yuma.server.db.entities.MediaObjectEntity;

public class MediaObject implements Serializable {


	private static final long serialVersionUID = 1375364283188282660L;

	private Long id;
	
	private String URI;
	
	private Date createdDate;
	
	private User createdBy;
	
	private MediaObjectEntity previousVersion;
	
	private String mimeType;
	
	private byte[] media;

	public MediaObject(Long id, String uRI, Date createdDate, User createdBy,
			MediaObjectEntity previousVersion, String mimeType, byte[] media) {
		super();
		this.id = id;
		URI = uRI;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.previousVersion = previousVersion;
		this.mimeType = mimeType;
		this.media = media;
	}

	/**
	 * @return the previousVersion
	 */
	public MediaObjectEntity getPreviousVersion() {
		return previousVersion;
	}

	/**
	 * @param previousVersion the previousVersion to set
	 */
	public void setPreviousVersion(MediaObjectEntity previousVersion) {
		this.previousVersion = previousVersion;
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
	 * @return the media
	 */
	public byte[] getMedia() {
		return media;
	}

	/**
	 * @param media the media to set
	 */
	public void setMedia(byte[] media) {
		this.media = media;
	}
	
	

}
