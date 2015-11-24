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

import com.peoplenet.qa.model.BaseConfig;
import com.peoplenet.qa.model.BaseConfigHome;
import com.peoplenet.qa.model.BaseConfigId;

/**
 * 
 */
@Stateless
@Path("forge/config")
public class ConfigurationEndpoint {

	private static final Logger log = Logger.getLogger(BaseConfigHome.class);

	@Inject
	private BaseConfigHome configDAO;

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response findById(@PathParam("id") String id) {
		log.info("Get Request with Id : " + id);
		String buildVersion = "";
		String project = "";
		String env = "";
		String[] params = id.split("-");

		if (params.length >= 3) {
			buildVersion = params[0];
			project = params[1];
			env = params[2];
		}

		BaseConfig entity;
		try {
			entity = configDAO.findById(new BaseConfigId(project, env,
					buildVersion));
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
	public Response create(BaseConfig conf) {

		log.info("POST with Values : " + conf.getId().getProject());
		log.info("POST with Values : " + conf.getId().getBuildVersion());
		log.info("POST with Values : " + conf.getId().getEnvironment());
		try {
			configDAO.persist(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.created(
				UriBuilder
						.fromResource(ConfigurationEndpoint.class)
						.path(String.valueOf(conf.getId().getBuildVersion()
								+ "-" + conf.getId().getProject() + "-"
								+ conf.getId().getEnvironment())).build())
				.build();
	}

	@GET
	@Produces("application/json")
	public List<BaseConfig> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		final List<BaseConfig> results = configDAO.findAll(startPosition,
				maxResult);
		log.info("All Config Records : " + results.size());
		return results;
	}

	@PUT
	@Consumes("application/json")
	public Response update(BaseConfig config) {
		BaseConfig entity;
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
