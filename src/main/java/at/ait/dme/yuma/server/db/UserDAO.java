package at.ait.dme.yuma.server.db;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;
import at.ait.dme.yuma.server.model.User;

@Repository
public class UserDAO implements IUserDAO {
	
	@PersistenceContext
	private EntityManager em;
	
	private static Logger log = Logger.getLogger(UserDAO.class);
	
	/**
	 * find a UserEntity by given username and appclient
	 * @param name
	 * @param appClient
	 * @return UserEntity
	 */
	@Transactional(readOnly=true, propagation=Propagation.SUPPORTS)
	public UserEntity findUser(User user, AppClientEntity appClient) {
		if (user==null || user.getUsername()==null) {
			return null;
		}
		
		TypedQuery<UserEntity> query = em.createNamedQuery("user.find", UserEntity.class);
		query.setParameter("username", user.getUsername());
		query.setParameter("appclient", appClient);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			log.info("no result for user: " + user);
			return null;
		}
	}
	
	/**
	 * create new UserEntity
	 * @param user
	 * @param appClient
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public UserEntity createUser(User user, AppClientEntity appClient) {
		
		UserEntity entity = new UserEntity(user);
		entity.setAppClient(appClient);
		entity.setUri(createURI(user, appClient));
		em.persist(entity);
		return entity;
	}
	
	/**
	 * TODO: refactor! (put into user)
	 * @param user
	 * @param appClient
	 * @return
	 */
	private String createURI(User user, AppClientEntity appClient) {
		String appPart;
		if (appClient==null || appClient.getClientToken()==null) {
			appPart = "";
		} else {
			appPart = appClient.getClientToken().concat("/");
		}
		return appPart.concat(user.getUsername());
	}
}
