package com.softexpert.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.softexpert.business.RaffleVariationService;
import com.softexpert.dto.ExperimentDTO;
import com.softexpert.dto.UserDTO;

@Path("public/users")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UserResource {

	@Inject
	private RaffleVariationService service;

	@POST
	public List<ExperimentDTO> raffle(UserDTO user) {
		return service.raffle(user);
	}

}
