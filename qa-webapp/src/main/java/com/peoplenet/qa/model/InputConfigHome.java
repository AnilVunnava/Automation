package com.peoplenet.qa.model;

// Generated Nov 16, 2015 10:51:39 AM by Hibernate Tools 4.3.1

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class InputConfig.
 * 
 * @see org.gen.InputConfig
 * @author Hibernate Tools
 */
@Stateless
public class InputConfigHome {

	private static final Log log = LogFactory.getLog(InputConfigHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(InputConfig transientInstance) {
		log.debug("persisting InputConfig instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(InputConfig persistentInstance) {
		log.debug("removing InputConfig instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public InputConfig merge(InputConfig detachedInstance) {
		log.debug("merging InputConfig instance");
		try {
			InputConfig result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public InputConfig findById(Long id) {
		log.debug("getting InputConfig instance with id: " + id);
		try {
			InputConfig instance = entityManager.find(InputConfig.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<InputConfig> findAll(Integer startPosition, Integer maxResult) {
		log.debug("getting All InputConfig instances");
		try {
			TypedQuery<InputConfig> findAllQuery = entityManager.createQuery(
					"SELECT DISTINCT c FROM InputConfig c", InputConfig.class);
			if (startPosition != null) {
				findAllQuery.setFirstResult(startPosition);
			}
			if (maxResult != null) {
				findAllQuery.setMaxResults(maxResult);
			}
			List<InputConfig> results = findAllQuery.getResultList();
			log.debug("get successful");
			return results;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<InputConfig> findAll() {
		log.debug("getting All InputConfig instances");
		try {
			TypedQuery<InputConfig> findAllQuery = entityManager.createQuery(
					"SELECT DISTINCT c FROM InputConfig c", InputConfig.class);
			List<InputConfig> results = findAllQuery.getResultList();
			log.debug("get successful");
			return results;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
