package at.ait.dme.yuma.server.db;

import java.util.List;

import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.entities.GroupEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;

public interface IGroupDAO {
	
	List<GroupEntity> getGroups(AppClientEntity appClient);
	GroupEntity createGroup(String name, UserEntity owner);
	GroupEntity findGroup(String name);
	void delete(GroupEntity group);
	void update(GroupEntity group);
}
