package at.ait.dme.yuma.server.model;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;

import at.ait.dme.yuma.server.config.Config;
import at.ait.dme.yuma.server.util.URIBuilder;

public class Media implements Serializable, IOwnable {


	private static final long serialVersionUID = 1375364283188282660L;
	
	private Long id;
	
	private Date createdDate = null;
	
	private Date lastUpdate = null;
	
	private User createdBy = null;
	
	private Scope scope = null;
	
	public Media() {
		
	}
	
	public Media(User createdBy) {
		this.createdBy = createdBy;
		this.setScope(Config.getInstance().getScopePolicy());
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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

	@Override
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
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setUpdatedDate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public URI getURI(boolean relative) {
		return URIBuilder.toURI(getId().toString(), URISource.MEDIA, relative);
	}
	
}
