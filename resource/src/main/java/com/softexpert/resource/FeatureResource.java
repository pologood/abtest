package com.softexpert.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.softexpert.business.FeatureEnablingService;
import com.softexpert.business.FeatureService;
import com.softexpert.business.exception.AppException;
import com.softexpert.business.exception.FeatureEnablingException;
import com.softexpert.persistence.Feature;

@Path("features")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class FeatureResource {

	@Inject
	private FeatureService service;
	@Inject
	private FeatureEnablingService featureEnablingService;

	@GET
	public List<Feature> list(@QueryParam("search") String search) {
		return service.list(search);
	}

	@POST
	public Feature save(Feature entity) throws AppException {
		return service.create(entity);
	}

	@PUT
	@Path("/{id}/enabling")
	public void enabling(@PathParam("id") Long id, Boolean enabling) throws FeatureEnablingException {
		featureEnablingService.enabling(id, enabling);
	}

}
