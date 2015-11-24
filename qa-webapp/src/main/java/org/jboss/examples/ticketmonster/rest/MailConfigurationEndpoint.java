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

import com.peoplenet.qa.model.MailConfig;
import com.peoplenet.qa.model.MailConfigHome;

/**
 * 
 */
@Stateless
@Path("forge/mailconfig")
public class MailConfigurationEndpoint {

	@Inject
	private MailConfigHome configDAO;

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Response findById(@PathParam("id") String id) {
		String buildVersion = "";
		String project = "";
		String env = "";
		String[] params = id.split("-");

		if (params.length >= 3) {
			buildVersion = params[0];
			project = params[1];
			env = params[2];
		}

		MailConfig entity = null;
		try {
			List<MailConfig> list = configDAO.findAll();
			for (MailConfig mailConfig : list) {
				if (mailConfig.getBuildVersion().equals(buildVersion)
						&& mailConfig.getProject().equals(project)
						&& mailConfig.getEnvironment().equals(env)) {
					entity = mailConfig;
				}
			}
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
	public Response create(MailConfig conf) {
		try {
			configDAO.persist(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.created(
				UriBuilder
						.fromResource(ConfigurationEndpoint.class)
						.path(String.valueOf(conf.getBuildVersion() + "-"
								+ conf.getProject() + "-"
								+ conf.getEnvironment())).build()).build();
	}

	@GET
	@Produces("application/json")
	public List<MailConfig> listAll(@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		final List<MailConfig> results = configDAO.findAll(startPosition,
				maxResult);
		return results;
	}

	@PUT
	@Consumes("application/json")
	public Response update(MailConfig config) {
		MailConfig entity;
		try {
			entity = configDAO.findById(config.getMailType());
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
