package at.ait.dme.yuma.server.db;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.exception.InvalidApplicationClientException;

@Repository
public class AppClientDAO implements IAppClientDAO {
	
	private static Logger log = Logger.getLogger(AppClientDAO.class);
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public AppClientEntity getAppClient(String clientToken) throws InvalidApplicationClientException {
		TypedQuery<AppClientEntity> query = 
				em.createNamedQuery(AppClientEntity.QUERY_FIND, AppClientEntity.class);
		query.setParameter(AppClientEntity.PARAM_FIND, clientToken);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			log.info("no result for appClient: " + clientToken);
			throw new InvalidApplicationClientException(String.format("AppClient %s does not exist", clientToken));
		}
	}
}
