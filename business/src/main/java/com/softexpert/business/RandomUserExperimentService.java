package com.softexpert.business;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.ejb.Stateless;

import com.google.common.base.Strings;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

@Stateless
public class RandomUserExperimentService {

	private static final int MIN_VALUE = 1;
	private static final BigDecimal HUNDRED = new BigDecimal(100D);

	public UserExperiment random(User user, Experiment experiment) {
		if (isRandomWithoutCondition(experiment.domains, experiment.groups, experiment.users))
			return randomExperiment(experiment);
		return randomWithCondition(user, experiment);
	}

	private UserExperiment randomWithCondition(User user, Experiment experiment) {
		List<String> users = toList(experiment.users);
		List<String> domains = toList(experiment.domains);
		List<String> groups =  toList(experiment.groups);
		if (isRandomWithGroupCondition(user, domains, groups))
			return randomExperiment(experiment);
		else if (isRandoWithUserCondition(user, domains, users))
			return randomExperiment(experiment);
		else if (isRandomWithDomainCondition(user, domains, groups, users))
			return randomExperiment(experiment);
		return createEmptyExperiment(experiment);
	}

	private List<String> toList(String value) {
		if (Strings.isNullOrEmpty(value))
			return Collections.emptyList();
		return Arrays.asList(value.trim().split(";"));
	}

	private boolean isRandomWithDomainCondition(User user, List<String> domains, List<String> groups,
			List<String> users) {
		return !domains.isEmpty() && domains.contains(user.host) && groups.isEmpty()&& users.isEmpty();
	}

	private boolean isRandoWithUserCondition(User user, List<String> domains, List<String> users) {
		return !users.isEmpty() && (users.contains(user.login) && !domains.isEmpty() && domains.contains(user.host));
	}

	private boolean isRandomWithGroupCondition(User user, List<String> domains, List<String> groups) {
		return !groups.isEmpty()	&& (groups.contains(user.group) && !domains.isEmpty() && domains.contains(user.host));
	}

	private boolean isRandomWithoutCondition(String domains, String groups, String users) {
		return Strings.isNullOrEmpty(domains) && Strings.isNullOrEmpty(groups) && Strings.isNullOrEmpty(users);
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
		return UserExperiment.builder().experiment(experiment).variation(Variation.builder().build()).build();
	}

	private UserExperiment buildRandomExperiment(Experiment experiment) {
		return UserExperiment.builder().experiment(experiment).variation(buildRandomVariantion(experiment)).build();
	}

	private Variation buildRandomVariantion(Experiment experiment) {
		Variation variation = experiment.variations.get(getRandomVariationPosition(experiment.variations.size()));
		variation.name = variation.name.toUpperCase();
		return variation;
	}

	private int getRandomVariationPosition(int possibilities) {
		return new Random().nextInt((possibilities - MIN_VALUE) + MIN_VALUE);
	}

	private boolean hasNeedRandom(Experiment experiment, double value) {
		int compare = new BigDecimal(value).compareTo(experiment.percentage);
		return !experiment.variations.isEmpty() && (compare == -1 || experiment.percentage.compareTo(HUNDRED) == 0);
	}
}
