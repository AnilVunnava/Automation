package com.peoplenet.qa.model;

// Generated Nov 10, 2015 10:56:08 AM by Hibernate Tools 4.3.1

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Home object for domain model class MobileConfig.
 * 
 * @see org.gen.MobileConfig
 * @author Hibernate Tools
 */
@Stateless
public class MobileConfigHome {

	private static final Log log = LogFactory.getLog(MobileConfigHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(MobileConfig transientInstance) {
		log.debug("persisting MobileConfig instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(MobileConfig persistentInstance) {
		log.debug("removing MobileConfig instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public MobileConfig merge(MobileConfig detachedInstance) {
		log.debug("merging MobileConfig instance");
		try {
			MobileConfig result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public MobileConfig findById(Long id) {
		log.debug("getting MobileConfig instance with id: " + id);
		try {
			MobileConfig instance = entityManager.find(MobileConfig.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MobileConfig> findAll(Integer startPosition, Integer maxResult) {
		log.debug("getting All MobileConfig instances");
		try {
			TypedQuery<MobileConfig> findAllQuery = entityManager
					.createQuery("SELECT DISTINCT c FROM MobileConfig c",
							MobileConfig.class);
			if (startPosition != null) {
				findAllQuery.setFirstResult(startPosition);
			}
			if (maxResult != null) {
				findAllQuery.setMaxResults(maxResult);
			}
			List<MobileConfig> results = findAllQuery.getResultList();
			log.debug("get successful");
			return results;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MobileConfig> findAll() {
		log.debug("getting All MobileConfig instances");
		try {
			TypedQuery<MobileConfig> findAllQuery = entityManager
					.createQuery("SELECT DISTINCT c FROM MobileConfig c",
							MobileConfig.class);
			List<MobileConfig> results = findAllQuery.getResultList();
			log.debug("get successful");
			return results;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
