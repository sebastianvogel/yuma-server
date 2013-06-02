package at.ait.dme.yuma.server.db;

import java.net.URI;
import java.util.List;

import at.ait.dme.yuma.server.db.entities.MediaContentVersionEntity;
import at.ait.dme.yuma.server.db.entities.MediaEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.MediaNotFoundException;
import at.ait.dme.yuma.server.model.Media;
import at.ait.dme.yuma.server.model.MediaContentVersion;

public interface IMediaDAO {
	
	public MediaEntity findMedia(Long id) throws MediaNotFoundException;
	public List<MediaEntity> findMediaForUser(UserEntity user);
	public MediaEntity createMedia(Media media, UserEntity user);	

	public MediaContentVersionEntity createMediaContentVersion(MediaEntity mediaEntity,
			MediaContentVersion mediaContent);

	public MediaContentVersionEntity findMediaContentVersion(Long mediaId,
			Long version);
	
	/*
	public List<MediaEntity> findMediaForUser(UserEntity user);
	public MediaEntity createMedia(Media media, UserEntity user);*/
	
	public List<Media> toMedia(List<MediaEntity> mediaEntities);
	public List<URI> toMediaContentUris(List<MediaContentVersionEntity> mediaContentVersionEntities,
			boolean relative);
	public List<String> getMediaContentList(MediaEntity mediaEntity);
	
	

	
}
