package at.ait.dme.yuma.server.db;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import at.ait.dme.yuma.server.db.entities.AppClientEntity;
import at.ait.dme.yuma.server.db.entities.GroupEntity;
import at.ait.dme.yuma.server.db.entities.UserEntity;

@Repository
public class GroupDAO implements IGroupDAO {
	
	private static Logger log = Logger.getLogger(GroupDAO.class);
	
	@PersistenceContext
	private EntityManager em;

	/**
	 * create new group
	 * @param groupname
	 * @param owner
	 * @return
	 */
	public GroupEntity createGroup(String groupname, UserEntity owner) {
		GroupEntity entity = new GroupEntity();
		entity.setCreatedBy(owner);
		entity.setName(groupname);
		entity.setCreated(new Date());
		entity.setModified(new Date());
		em.persist(entity);
		return entity;
	}
	
	@Override
	public GroupEntity findGroup(String name) {
		if (name==null) {
			return null;
		}
		
		TypedQuery<GroupEntity> query = em.createNamedQuery("group.find", GroupEntity.class);
		query.setParameter("name", name);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			log.info("no result for group-name: " + name);
			return null;
		}		
	}
	
	@Override
	public void update(GroupEntity group) {
		em.merge(group);
	}

	@Override
	public void delete(GroupEntity group) {
		if (group==null) {
			return;
		}
		em.remove(group);
	}

	@Override
	public List<GroupEntity> getGroups(AppClientEntity appClient) {
		TypedQuery<GroupEntity> query = em.createNamedQuery("group.get", GroupEntity.class);
		query.setParameter("appClient", appClient);
		return query.getResultList();
	}
}
