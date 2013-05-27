package at.ait.dme.yuma.server.model;

import java.io.Serializable;
import java.util.Date;

public class MediaObject implements Serializable {


	private static final long serialVersionUID = 1375364283188282660L;
	
	private String URI = null;
	
	private Date createdDate = null;
	
	private User createdBy = null;
	
	private String previousVersionUri = null;
	
	private String mimeType = null;
	
	private byte[] content = null;

	public MediaObject(String uRI) {
		this.URI = uRI;
	}
	
	public MediaObject(String uRI, User createdBy, String mimeType, byte[] content) {
		super();
		this.URI = uRI;
		this.createdBy = createdBy;
		this.mimeType = mimeType;
		this.content = content;
	}
	
	/**
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}

	/**
	 * @return the uRI
	 */
	public String getURI() {
		return URI;
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
	 * @return the createdBy
	 */
	public User getCreatedBy() {
		return createdBy;
	}


	/**
	 * @param createdBy the user to set
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	
	/**
	 * @return the URI of the previousVersion
	 */
	public String getPreviousVersionUri() {
		return previousVersionUri;
	}

	/**
	 * @param previousVersion the URI of the previousVersion to set
	 */
	public void setPreviousVersionUri(String previousVersionUri) {
		this.previousVersionUri = previousVersionUri;
	}

	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}
}
