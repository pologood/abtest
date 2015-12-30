package com.softexpert.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.softexpert.business.FeatureService;
import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.Feature;

import lombok.extern.log4j.Log4j;

@Path("features")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class FeatureResource {

	@Inject
	private FeatureService service;

	@GET
	public List<Feature> get(@QueryParam("search") String search) {
		return service.list(search);
	}

	@POST
	public Feature save(Feature entity) throws AppException {
		return service.create(entity);
	}

}
