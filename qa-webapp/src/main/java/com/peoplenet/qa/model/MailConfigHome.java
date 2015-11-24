package com.peoplenet.qa.model;

// Generated Nov 4, 2015 10:37:05 AM by Hibernate Tools 4.3.1

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

/**
 * Home object for domain model class MailConfig.
 * 
 * @see com.peoplenet.qa.model.MailConfig
 * @author Hibernate Tools
 */
@Stateless
public class MailConfigHome {

	private static final Logger log = Logger.getLogger(MailConfigHome.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(MailConfig transientInstance) {
		log.debug("persisting MailConfig instance");
		try {
			entityManager.persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void remove(MailConfig persistentInstance) {
		log.debug("removing MailConfig instance");
		try {
			entityManager.remove(persistentInstance);
			log.debug("remove successful");
		} catch (RuntimeException re) {
			log.error("remove failed", re);
			throw re;
		}
	}

	public MailConfig merge(MailConfig detachedInstance) {
		log.debug("merging MailConfig instance");
		try {
			MailConfig result = entityManager.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public MailConfig findById(String id) {
		log.debug("getting MailConfig instance with id: " + id);
		try {
			MailConfig instance = entityManager.find(MailConfig.class, id);
			log.debug("get successful");
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MailConfig> findAll(Integer startPosition, Integer maxResult) {
		log.debug("getting All MailConfig instances");
		try {
			TypedQuery<MailConfig> findAllQuery = entityManager.createQuery(
					"SELECT DISTINCT c FROM MailConfig c", MailConfig.class);
			if (startPosition != null) {
				findAllQuery.setFirstResult(startPosition);
			}
			if (maxResult != null) {
				findAllQuery.setMaxResults(maxResult);
			}
			List<MailConfig> results = findAllQuery.getResultList();
			log.debug("get successful");
			return results;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<MailConfig> findAll() {
		log.debug("getting All MailConfig instances");
		try {
			TypedQuery<MailConfig> findAllQuery = entityManager.createQuery(
					"SELECT DISTINCT c FROM MailConfig c", MailConfig.class);
			List<MailConfig> results = findAllQuery.getResultList();
			log.debug("get successful");
			return results;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
}
