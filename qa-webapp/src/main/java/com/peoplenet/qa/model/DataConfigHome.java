package com.peoplenet.qa.model;

// Generated Nov 5, 2015 10:41:55 AM by Hibernate Tools 4.3.1

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

/**
 * Home object for domain model class DataConfig.
 * 
 * @see com.peoplenet.qa.model.gen.DataConfig
 * @author Hibernate Tools
 */
@Stateless
public class DataConfigHome {

	private static final Logger log = Logger.getLogger(DataConfigHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(DataConfig transientInstance) {
		log.debug("persisting DataConfig instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(DataConfig persistentInstance) {
		log.debug("removing DataConfig instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public DataConfig merge(DataConfig detachedInstance) {
		log.debug("merging DataConfig instance");
		try {
			DataConfig result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DataConfig findById(Long id) {
		log.debug("getting DataConfig instance with id: " + id);
		try {
			DataConfig instance = entityManager.find(DataConfig.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<DataConfig> findAll(Integer startPosition, Integer maxResult) {
		log.debug("getting All DataConfig instances");
		try {
			TypedQuery<DataConfig> findAllQuery = entityManager.createQuery(
					"SELECT DISTINCT c FROM DataConfig c", DataConfig.class);
			if (startPosition != null) {
				findAllQuery.setFirstResult(startPosition);
			}
			if (maxResult != null) {
				findAllQuery.setMaxResults(maxResult);
			}
			List<DataConfig> results = findAllQuery.getResultList();
			log.debug("get successful");
			return results;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<DataConfig> findAll() {
		log.debug("getting All DataConfig instances");
		try {
			TypedQuery<DataConfig> findAllQuery = entityManager.createQuery(
					"SELECT DISTINCT c FROM DataConfig c", DataConfig.class);
			List<DataConfig> results = findAllQuery.getResultList();
			log.debug("get successful");
			return results;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
