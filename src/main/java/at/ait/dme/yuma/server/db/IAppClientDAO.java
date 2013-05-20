package at.ait.dme.yuma.server.db;

import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.exception.InvalidApplicationClientException;

public interface IAppClientDAO {
	
	AppClientEntity getAppClient(String clientToken) throws InvalidApplicationClientException;

}
