package com.softexpert.business;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.google.common.base.Strings;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Stateless
@NoArgsConstructor
public class RandomUserExperimentService {

	@Inject
	private ExperimentRandomService experimentRandomService;
	private List<String> users;
	private List<String> domains;
	private List<String> groups;

	public RandomUserExperimentService(ExperimentRandomService experimentRandomService){
		this.experimentRandomService = experimentRandomService;
	}
	
	public UserExperiment random(User user, Experiment experiment) {
		build(experiment.users, experiment.domains, experiment.groups);
		if (isRandomWithoutCondition())
			return experimentRandomService.randomExperiment(experiment);
		return randomWithCondition(user, experiment);
	}

	private void build(String users, String domains, String groups) {
		this.users = toList(users);
		this.domains = toList(domains);
		this.groups = toList(groups);
	}
	
	private UserExperiment randomWithCondition(User user, Experiment experiment) {
		if (isRandomWithGroupCondition(user))
			return experimentRandomService.randomExperiment(experiment);
		if (isRandoWithUserCondition(user))
			return experimentRandomService.randomExperiment(experiment);
		if (isRandomWithDomainCondition(user))
			return experimentRandomService.randomExperiment(experiment);
		return experimentRandomService.createEmptyExperiment(experiment);
	}

	private boolean isRandomWithoutCondition() {
		return domains.isEmpty() && groups.isEmpty() && users.isEmpty();
	}

	private List<String> toList(String value) {
		if (Strings.isNullOrEmpty(value))
			return Collections.emptyList();
		return Arrays.asList(value.trim().split(";"));
	}

	private boolean isRandomWithDomainCondition(User user) {
		return !domains.isEmpty() && domains.contains(user.host) && groups.isEmpty() && users.isEmpty();
	}

	private boolean isRandoWithUserCondition(User user) {
		return !users.isEmpty() && (users.contains(user.login) && !domains.isEmpty() && domains.contains(user.host));
	}

	private boolean isRandomWithGroupCondition(User user) {
		return !groups.isEmpty() && (groups.contains(user.department) && !domains.isEmpty() && domains.contains(user.host));
	}

}
