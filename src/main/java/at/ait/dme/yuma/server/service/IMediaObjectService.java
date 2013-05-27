package at.ait.dme.yuma.server.service;

import java.util.List;

import at.ait.dme.yuma.server.exception.MediaObjectNotFoundException;
import at.ait.dme.yuma.server.model.MediaObject;

public interface IMediaObjectService {
	/**
	 * 
	 * @param mediaObject
	 * @param clientToken
	 * @return the URI of the new mediaObject
	 */
	public String createMediaObject(MediaObject mediaObject, String clientToken);
	
	public MediaObject findMediaObjectByUri(String uri) throws MediaObjectNotFoundException;
	
	public List<MediaObject> findMediaObjectsByUsers(String username, String clientToken);
	

}
