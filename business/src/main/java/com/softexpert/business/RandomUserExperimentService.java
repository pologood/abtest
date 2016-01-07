package com.softexpert.business;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;

import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

@Stateless
public class RandomUserExperimentService {

	private static final int MIN_VALUE = 1;
	private static final BigDecimal HUNDRED = new BigDecimal(100D);
	
	public UserExperiment random(User user, Experiment experiment) {
		if(isRandomWithoutCondition(experiment.domains, experiment.groups, experiment.users))
			return randomExperiment(experiment);
		return randomWithCondition(user, experiment);
	}

	private UserExperiment randomWithCondition(User user, Experiment experiment) {
		if(isRandomWithGroupCondition(user, experiment.domains, experiment.groups))
			return randomExperiment(experiment);
		else if(isRandoWithUserCondition(user, experiment.domains, experiment.users))
			return randomExperiment(experiment);
		else if(isRandomWithDomainCondition(user, experiment.domains, experiment.groups, experiment.users))
			return randomExperiment(experiment);				
		return createEmptyExperiment(experiment);
	}

	private boolean isRandomWithDomainCondition(User user, List<String> domains, List<String> groups, List<String> users) {
		return hasValueInList(domains) && domains.contains(user.host)  && !hasValueInList(groups) && !hasValueInList(users);
	}

	private boolean isRandoWithUserCondition(User user, List<String> domains, List<String> users) {
		return hasValueInList(users) && (users.contains(user.login) && hasValueInList(domains) && domains.contains(user.host));
	}

	private boolean isRandomWithGroupCondition(User user, List<String> domains, List<String> groups) {
		return hasValueInList(groups) && (groups.contains(user.group) && hasValueInList(domains) && domains.contains(user.host));
	}

	private boolean isRandomWithoutCondition(List<String> domains, List<String> groups, List<String> users) {
		return !hasValueInList(domains) && !hasValueInList(groups) && !hasValueInList(users);
	}
	
	private boolean hasValueInList(List<String> list) {
		return list != null && !list.isEmpty();
	}
	
	private UserExperiment randomExperiment(Experiment experiment) {
		double value = randomValue(experiment);
		if (hasNeedRandom(experiment, value))
			return buildRandomExperiment(experiment);
		return createEmptyExperiment(experiment);
	}

	private double randomValue(Experiment experiment) {
		return Math.random() * HUNDRED.doubleValue();
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
		Variation variation = experiment.variations.get(getRandomVariationPosition(experiment.variations.size()));
		variation.name = variation.name.toUpperCase();
		return variation;
	}

	private int getRandomVariationPosition(int possibilities) {
		return new Random().nextInt((possibilities -MIN_VALUE )+ MIN_VALUE);
	}

	private boolean hasNeedRandom(Experiment experiment, double value) {
		int compare = experiment.percentage.compareTo(new BigDecimal(value));
		return !experiment.variations.isEmpty() && (compare == 1 || experiment.percentage.equals(HUNDRED));
	}
}
