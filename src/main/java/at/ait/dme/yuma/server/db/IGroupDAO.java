package at.ait.dme.yuma.server.db;

import at.ait.dme.yuma.server.db.entities.GroupEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;

public interface IGroupDAO {
	
	GroupEntity createGroup(String name, UserEntity owner);
	GroupEntity findGroup(String name);
	void delete(GroupEntity group);
	void update(GroupEntity group);
}
