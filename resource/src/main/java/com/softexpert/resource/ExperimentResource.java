package com.softexpert.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.softexpert.business.ExperimentEnablingService;
import com.softexpert.business.ExperimentLoadService;
import com.softexpert.business.ExperimentPersistenceService;
import com.softexpert.business.exception.AppException;
import com.softexpert.business.exception.FeatureEnablingException;
import com.softexpert.persistence.Experiment;

@Path("experiments")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ExperimentResource {

	@Inject
	private ExperimentLoadService service;
	@Inject
	private ExperimentEnablingService experimentEnablingService;
	@Inject
	private ExperimentPersistenceService experimentPersistenceService;

	@GET
	public List<Experiment> list(@QueryParam("search") String search) {
		return service.list(search);
	}

	@POST
	public Experiment save(Experiment entity) throws AppException {
		return experimentPersistenceService.create(entity);
	}

	@GET
	@Path("/{id}")
	public Experiment find(@PathParam("id") Long id)throws AppException {
		return service.find(id);
	}
	
	@PUT
	@Path("/{id}/enabling")
	public void enabling(@PathParam("id") Long id, Boolean enabling) throws FeatureEnablingException {
		experimentEnablingService.enabling(id, enabling);
	}	

}
