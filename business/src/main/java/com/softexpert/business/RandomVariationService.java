package com.softexpert.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.dto.ExperimentDTO;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

@Stateless
public class RandomVariationService {

	private static final int MIN_VALUE = 1;
	private static final double HUNDRED = 100D;
	@Inject
	private AvailableExperimentsService availableExperimentsService;
	
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
		List<UserExperiment> raffleExperiments = new ArrayList<UserExperiment>();
		experiments.stream().forEach(experiment -> raffleExperiments.add(randomExperiment(experiment)));
		return raffleExperiments;
	}

	private UserExperiment randomExperiment(Experiment experiment) {
		double value = randomValue(experiment);
		if (hasNeedRaffle(experiment, value))
			return buildRandomExperiment(experiment);
		return createEmptyExperiment(experiment);
	}

	private double randomValue(Experiment experiment) {
		return Math.random() * HUNDRED;
	}

	private UserExperiment createEmptyExperiment(Experiment experiment) {
		return UserExperiment.builder()
				.experiment(experiment)
				.variation(Variation.builder().build())
				.build();
	}

	private UserExperiment buildRandomExperiment(Experiment experiment) {
		return UserExperiment.builder()
				.experiment(experiment)
				.variation(buildRandomVariantion(experiment))
				.build();
	}

	private Variation buildRandomVariantion(Experiment experiment) {
		return experiment.variations.get(getRandomVariationPosition(experiment.variations.size()));
	}

	private int getRandomVariationPosition(int possibilities) {
		return new Random().nextInt((possibilities -MIN_VALUE )+ MIN_VALUE);
	}

	private boolean hasNeedRaffle(Experiment experiment, double value) {
		int compare = experiment.percentage.compareTo(new BigDecimal(value));
		return !experiment.variations.isEmpty() && (compare == 1 || experiment.percentage.equals(new BigDecimal(HUNDRED)));
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
