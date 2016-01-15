package com.softexpert.business;

import java.math.BigDecimal;
import java.util.Random;

import javax.ejb.Stateless;

import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

@Stateless
public class ExperimentRandomService {

	private static final int MIN_VALUE = 1;
	private static final BigDecimal HUNDRED = new BigDecimal(100D);
	
	public UserExperiment randomExperiment(Experiment experiment) {
		double value = randomValue(experiment);
		if (hasNeedRandom(experiment, value))
			return buildRandomExperiment(experiment);
		return createEmptyExperiment(experiment);
	}
	
	public UserExperiment createEmptyExperiment(Experiment experiment) {
		return UserExperiment.builder().experiment(experiment).variation(Variation.builder().build()).build();
	}
	
	 private UserExperiment buildRandomExperiment(Experiment experiment) {
		return UserExperiment.builder().experiment(experiment).variation(buildRandomVariantion(experiment)).build();
	}
	
	private double randomValue(Experiment experiment) {
		return Math.random() * HUNDRED.doubleValue();
	}

	private int getRandomVariationPosition(int possibilities) {
		return new Random().nextInt((possibilities - MIN_VALUE) + MIN_VALUE);
	}

	private boolean hasNeedRandom(Experiment experiment, double value) {
		int compare = new BigDecimal(value).compareTo(experiment.percentage);
		return !experiment.variations.isEmpty() && (compare == -1 || experiment.percentage.compareTo(HUNDRED) == 0);
	}
	
	private Variation buildRandomVariantion(Experiment experiment) {
		Variation variation = experiment.variations.get(getRandomVariationPosition(experiment.variations.size()));
		variation.name = variation.name.toUpperCase();
		return variation;
	}
}
