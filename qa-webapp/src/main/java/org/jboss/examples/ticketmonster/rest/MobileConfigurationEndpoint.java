package org.jboss.examples.ticketmonster.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.jboss.logging.Logger;

import com.peoplenet.qa.model.MobileConfig;
import com.peoplenet.qa.model.MobileConfigHome;

/**
 * 
 */
@Stateless
@Path("forge/mobileconfig")
public class MobileConfigurationEndpoint {

	private static final Logger log = Logger
			.getLogger(MobileConfigurationEndpoint.class);

	@Inject
	private MobileConfigHome configDAO;

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response findById(@PathParam("id") Long id) {
		log.info("Get Request with Id : " + id);

		MobileConfig entity;
		try {
			entity = configDAO.findById(id);
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		return Response.ok(entity).build();
	}

	@POST
	@Consumes("application/json")
	public Response create(MobileConfig conf) {

		log.info("POST with Values : " + conf.getProject());
		log.info("POST with Values : " + conf.getBuildVersion());
		log.info("POST with Values : " + conf.getEnvironment());
		log.info("POST with Values : " + conf.getDeviceName());
		log.info("POST with Values : " + conf.getPlatform());
		try {
			configDAO.persist(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.created(
				UriBuilder.fromResource(MobileConfigurationEndpoint.class)
						.path(String.valueOf(conf.getId())).build()).build();
	}

	@GET
	@Produces("application/json")
	public List<MobileConfig> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		final List<MobileConfig> results = configDAO.findAll(startPosition,
				maxResult);
		log.info("All Config Records : " + results.size());
		return results;
	}

	@PUT
	@Consumes("application/json")
	public Response update(MobileConfig config) {
		MobileConfig entity;
		try {
			entity = configDAO.findById(config.getId());
		} catch (NoResultException nre) {
			entity = null;
		}
		try {
			entity = configDAO.merge(config);
		} catch (OptimisticLockException e) {
			return Response.status(Response.Status.CONFLICT)
					.entity(e.getEntity()).build();
		}
		return Response.ok(entity).build();
	}

}
