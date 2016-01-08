package com.softexpert.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.softexpert.business.exception.AppException;
import com.softexpert.dto.ExperimentDTO;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

@Stateless
public class RandomVariationService {

	@Inject
	private AvailableExperimentsService availableExperimentsService;
	@Inject
	private RandomUserExperimentService randomVariationRuleService;
	@Inject
	private PersistenceService<User> service;
	@Inject
	private UserExperimentService userExperimentService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ExperimentDTO> random(UserDTO userDTO) throws AppException {
		User user = userExperimentService.getUser(userDTO);
		if (user == null) {
			user = getUser(userDTO);
			List<UserExperiment> experiments = randomExperiments(user);
			List<ExperimentDTO> dto = toDto(experiments);
			save(user, experiments);
			return dto;
		}
		return toDto(user.experiments);
	}

	private void save(User user, List<UserExperiment> experiments) throws AppException {
		user.experiments = new ArrayList<>();
		experiments.stream().forEach(experiment -> {
			user.experiments
					.add(UserExperiment.builder().experiment(Experiment.builder().id(experiment.experiment.id).build())
							.variation(Variation.builder().id(experiment.variation.id).build()).build());
		});
		service.create(user);

	}

	private List<UserExperiment> randomExperiments(User user) throws AppException {
		Collection<Experiment> experiments = availableExperimentsService.getAvailableExperiments();
		return random(user, experiments);
	}

	private List<ExperimentDTO> toDto(List<UserExperiment> userExperiments) {
		List<ExperimentDTO> experiments = new ArrayList<>();
		userExperiments.stream().forEach(userExperiment -> experiments.add(toDto(userExperiment)));
		return experiments;
	}

	private ExperimentDTO toDto(UserExperiment userExperiment) {
		return ExperimentDTO.builder().name(userExperiment.experiment.name).variationName(userExperiment.variation.name)
				.build();
	}

	private List<UserExperiment> random(User user, Collection<Experiment> experiments) {
		List<UserExperiment> randomExperiments = new ArrayList<UserExperiment>();
		experiments.stream()
				.forEach(experiment -> randomExperiments.add(randomVariationRuleService.random(user, experiment)));
		return randomExperiments;
	}

	private User getUser(UserDTO userDTO) {
		return User.builder().code(userDTO.code).department(userDTO.department).host(userDTO.host).login(userDTO.login)
				.name(userDTO.name).build();
	}
}
