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
public class RaffleVariationService {

	@Inject
	private AvailableExperimentsService availableExperimentsService;
	
	public List<ExperimentDTO> raffle(UserDTO userDTO) {
		User user = getUser(userDTO);
		Collection<Experiment> experiment = availableExperimentsService.getAvailableExperiments();
		List<UserExperiment> userExperiments = raffle(user, experiment);
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
				.variation(userExperiment.variation.name)
				.build();
	}

	private List<UserExperiment> raffle(User user, Collection<Experiment> experiments) {
		List<UserExperiment> raffleExperiments = new ArrayList<UserExperiment>();
		experiments.stream().forEach(experiment -> raffleExperiments.add(raffleExperiment(experiment)));
		return raffleExperiments;
	}

	private UserExperiment raffleExperiment(Experiment experiment) {
		double value = experiment.percentage.doubleValue()
				+ Math.random() * (100D - experiment.percentage.doubleValue());
		if (hasNeedRaffle(experiment, value))
			return raffleVariation(experiment);
		return UserExperiment.builder().experiment(experiment).variation(Variation.builder().name("NONE").build())
				.build();
	}

	private UserExperiment raffleVariation(Experiment experiment) {
		int possibilities = experiment.variations.size();
		return UserExperiment.builder()
				.experiment(experiment)
				.variation(experiment.variations.get(getVariationPosition(possibilities)))
				.build();
	}

	private int getVariationPosition(int possibilities) {
		return new Random().nextInt((possibilities -1 )+ 1);
	}

	private boolean hasNeedRaffle(Experiment experiment, double value) {
		int compare = experiment.percentage.compareTo(new BigDecimal(value));
		return compare == 1 || experiment.percentage.equals(new BigDecimal(100D));
	}

	private User getUser(UserDTO userDTO) {
		return User.builder().id(1L).code(userDTO.code).group(userDTO.group).host(userDTO.host).login(userDTO.login)
				.name(userDTO.name).build();
	}
}
