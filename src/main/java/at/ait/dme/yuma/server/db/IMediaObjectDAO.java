package at.ait.dme.yuma.server.db;

import java.util.List;

import at.ait.dme.yuma.server.db.entities.MediaObjectEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.exception.MediaObjectNotFoundException;
import at.ait.dme.yuma.server.model.MediaObject;

public interface IMediaObjectDAO {
	
	public MediaObjectEntity findMediaObjectByUri(String uri) throws MediaObjectNotFoundException;
	public List<MediaObjectEntity> findMediaObjectsForUser(UserEntity user);
	public MediaObjectEntity createMediaObject(MediaObject mediaObject, UserEntity user);
	public List<MediaObject> toMediaObjects(List<MediaObjectEntity> mediaObjectEntities);
}
