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

import com.softexpert.business.PersistenceService;
import com.softexpert.business.exception.AppException;
import com.softexpert.business.exception.ExperimentEnablingException;
import com.softexpert.business.experiment.ExperimentEnablingService;
import com.softexpert.business.experiment.ExperimentLoaderService;
import com.softexpert.persistence.Experiment;

@Path("experiments")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ExperimentResource {

	@Inject
	private ExperimentLoaderService service;
	@Inject
	private ExperimentEnablingService experimentEnablingService;
	@Inject
	private PersistenceService<Experiment> experimentPersistenceService;

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
	public void enabling(@PathParam("id") Long id, Boolean enabling) throws ExperimentEnablingException {
		experimentEnablingService.enabling(id, enabling);
	}	

}
