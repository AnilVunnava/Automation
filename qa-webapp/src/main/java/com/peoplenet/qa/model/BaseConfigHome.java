package com.peoplenet.qa.model;

// Generated Nov 4, 2015 10:37:05 AM by Hibernate Tools 4.3.1

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

/**
 * Home object for domain model class BaseConfig.
 * 
 * @see com.peoplenet.qa.model.BaseConfig
 * @author Hibernate Tools
 */
@Stateless
public class BaseConfigHome {

	private static final Logger log = Logger.getLogger(BaseConfigHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(BaseConfig transientInstance) {
		log.info("persisting BaseConfig instance");
		try {
			entityManager.persist(transientInstance);
			log.info("persist successful");
		} catch (RuntimeException re) {
			log.info("persist failed", re);
			throw re;
		}
	}

	public void remove(BaseConfig persistentInstance) {
		log.info("removing BaseConfig instance");
		try {
			entityManager.remove(persistentInstance);
			log.info("remove successful");
		} catch (RuntimeException re) {
			log.info("remove failed", re);
			throw re;
		}
	}

	public BaseConfig merge(BaseConfig detachedInstance) {
		log.info("merging BaseConfig instance");
		try {
			BaseConfig result = entityManager.merge(detachedInstance);
			log.info("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.info("merge failed", re);
			throw re;
		}
	}

	public BaseConfig findById(BaseConfigId id) {
		log.info("getting BaseConfig instance with id: " + id);
		try {
			BaseConfig instance = entityManager.find(BaseConfig.class, id);
			log.info("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.info("get failed", re);
			throw re;
		}
	}

	public List<BaseConfig> findAll(Integer startPosition, Integer maxResult) {
		log.info("getting All BaseConfig instances");
		try {
			TypedQuery<BaseConfig> findAllQuery = entityManager.createQuery(
					"SELECT DISTINCT c FROM BaseConfig c", BaseConfig.class);
			if (startPosition != null) {
				findAllQuery.setFirstResult(startPosition);
			}
			if (maxResult != null) {
				findAllQuery.setMaxResults(maxResult);
			}

			List<BaseConfig> results = findAllQuery.getResultList();
			log.info("get successful");
			return results;
		} catch (RuntimeException re) {
			log.info("get failed", re);
			throw re;
		}
	}
}
