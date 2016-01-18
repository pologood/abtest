package com.softexpert.business.experiment;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.softexpert.business.exception.AppException;
import com.softexpert.business.sortition.ExperimentsSortitionService;
import com.softexpert.business.user.UserSaveService;
import com.softexpert.business.user.UserSearchService;
import com.softexpert.dto.ExperimentDTO;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;

@Stateless
public class XService {

	@Inject
	private UserSaveService userSaveService;
	@Inject
	private UserSearchService userExperimentService;
	@Inject
	private ExperimentsSortitionService experimentsSortitionService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ExperimentDTO> sortionOrSearch(UserDTO userDTO) throws AppException {
		User user = userExperimentService.search(userDTO);
		if (user == null)
			return sortitionNewUser(userDTO);
		return toDto(user.experiments);
	}

	private List<ExperimentDTO> sortitionNewUser(UserDTO userDTO) throws AppException {
		User user = getUser(userDTO);
		List<UserExperiment> experiments = experimentsSortitionService.random(user);
		userSaveService.save(user, experiments);
		return toDto(experiments);
	}

	private ExperimentDTO toDto(UserExperiment userExperiment) {
		return ExperimentDTO.builder().name(userExperiment.experiment.name).variationName(userExperiment.variation.name)
				.build();
	}

	private User getUser(UserDTO userDTO) {
		return User.builder().code(userDTO.code).department(userDTO.department).host(userDTO.host).login(userDTO.login)
				.name(userDTO.name).build();
	}

	private List<ExperimentDTO> toDto(List<UserExperiment> userExperiments) {
		List<ExperimentDTO> experiments = new ArrayList<>();
		userExperiments.stream().forEach(userExperiment -> experiments.add(toDto(userExperiment)));
		return experiments;
	}
}
