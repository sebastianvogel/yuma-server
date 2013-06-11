package at.ait.dme.yuma.server.service;

import java.net.URI;
import java.util.List;

import at.ait.dme.yuma.server.controller.AuthContext;
import at.ait.dme.yuma.server.exception.InvalidMediaException;
import at.ait.dme.yuma.server.exception.MediaNotFoundException;
import at.ait.dme.yuma.server.exception.PermissionDeniedException;
import at.ait.dme.yuma.server.model.MediaContentVersion;
import at.ait.dme.yuma.server.model.Media;

public interface IMediaService {
	/**
	 * 
	 * @param media
	 * @param clientToken
	 * @return the URI of the new mediaObject
	 * @throws InvalidMediaException 
	 */
	public Media createMedia(Media media, AuthContext auth) throws InvalidMediaException;
	
	public Media updateMedia(Media media, AuthContext auth) throws MediaNotFoundException, PermissionDeniedException;
	
	public List<Media> findMediaByUsers(String username, AuthContext auth) throws PermissionDeniedException;

	public URI createMediaContentVersion(Long mediaId,
			MediaContentVersion mediaContent, AuthContext auth) throws MediaNotFoundException, PermissionDeniedException;
	
	public Media findMedia(Long id) throws MediaNotFoundException;
	
	public MediaContentVersion findMediaContentVersion(Long mediaId, Long version) throws MediaNotFoundException;
	
	public List<URI> findMediaContentVersionsByMedia(Long mediaId, boolean relative) throws MediaNotFoundException;
}
