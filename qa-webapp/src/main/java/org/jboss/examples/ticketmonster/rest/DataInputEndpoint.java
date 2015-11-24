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

import org.jboss.logging.Logger;

import com.peoplenet.qa.model.BaseConfigHome;
import com.peoplenet.qa.model.InputConfig;
import com.peoplenet.qa.model.InputConfigHome;

/**
 * 
 */
@Stateless
@Path("forge/datainput")
public class DataInputEndpoint {

	private static final Logger log = Logger.getLogger(BaseConfigHome.class);

	@Inject
	private InputConfigHome configDAO;

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response findById(@PathParam("id") long id) {
		log.info("Get Request with Id : " + id);
		InputConfig entity;
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
	public Response create(InputConfig conf) {

		log.info("POST with Values : " + conf.getProject());
		log.info("POST with Values : " + conf.getVersion());
		log.info("POST with Values : " + conf.getEnvironment());
		log.info("POST with Values : " + conf.getScriptName());
		try {
			configDAO.persist(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok().build();
	}

	@GET
	@Produces("application/json")
	public List<InputConfig> listAll(
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		final List<InputConfig> results = configDAO.findAll(startPosition,
				maxResult);
		log.info("All Config Records : " + results.size());
		return results;
	}

	@PUT
	@Consumes("application/json")
	public Response update(InputConfig config) {
		InputConfig entity;
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
