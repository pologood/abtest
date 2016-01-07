package com.softexpert.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.dto.ExperimentDTO;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;

@Stateless
public class RandomVariationService {

	@Inject
	private AvailableExperimentsService availableExperimentsService;
	@Inject
	private RandomUserExperimentService randomVariationRuleService;
	
	public List<ExperimentDTO> random(UserDTO userDTO) {
		User user = getUser(userDTO);
		Collection<Experiment> experiment = availableExperimentsService.getAvailableExperiments();
		List<UserExperiment> userExperiments = random(user, experiment);
		return toDto(userExperiments);
	}

	private List<ExperimentDTO> toDto(List<UserExperiment> userExperiments) {
		List<ExperimentDTO> experiments = new ArrayList<>();
		userExperiments.stream().forEach(userExperiment -> experiments.add(toDto(userExperiment)));
		return experiments;
	}

	private ExperimentDTO toDto(UserExperiment userExperiment) {
		return ExperimentDTO.builder()
				.name(userExperiment.experiment.name)
				.variationName(userExperiment.variation.name)
				.build();
	}

	private List<UserExperiment> random(User user, Collection<Experiment> experiments) {
		List<UserExperiment> randomExperiments = new ArrayList<UserExperiment>();
		experiments.stream().forEach(experiment -> 
				randomExperiments.add(randomVariationRuleService.random(user, experiment))
		);
		return randomExperiments;
	}

	private User getUser(UserDTO userDTO) {
		return User.builder()
				.id(1L)
				.code(userDTO.code)
				.group(userDTO.group)
				.host(userDTO.host)
				.login(userDTO.login)
				.name(userDTO.name)
				.build();
	}
}
