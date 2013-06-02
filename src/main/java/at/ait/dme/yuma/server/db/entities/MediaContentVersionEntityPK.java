package at.ait.dme.yuma.server.db.entities;

public class MediaContentVersionEntityPK {
	private Long version;
	private Long mediaEntity;
	
	
	public MediaContentVersionEntityPK() {
	}
	
	public MediaContentVersionEntityPK(Long mediaEntity, Long version) {
		this.mediaEntity = mediaEntity;
		this.version = version;
	}
	/**
	 * @return the mediaContentVersionId
	 */
	public Long getVersion() {
		return version;
	}
	/**
	 * @param mediaContentVersionId the mediaContentVersionId to set
	 */
	public void setVersion(Long version) {
		this.version = version;
	}
	/**
	 * @return the mediaEntity
	 */
	public Long getMediaEntity() {
		return mediaEntity;
	}
	/**
	 * @param mediaEntity the mediaEntity to set
	 */
	public void setMediaEntity(Long mediaEntity) {
		this.mediaEntity = mediaEntity;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return mediaEntity.hashCode() ^ version.hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof MediaContentVersionEntityPK)) {
			return false;
		}
		MediaContentVersionEntityPK pk = (MediaContentVersionEntityPK) obj;
		return pk.getVersion().equals(version) && pk.getMediaEntity().equals(mediaEntity);
	}
	
	

}
