package at.ait.dme.yuma.server.db;

import at.ait.dme.yuma.server.db.hibernate.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.hibernate.entities.UserEntity;
import at.ait.dme.yuma.server.model.User;

public interface IUserDAO {
	
	UserEntity findUser(User user, AppClientEntity appClient);
	UserEntity createUser(User user, AppClientEntity appClient);

}
