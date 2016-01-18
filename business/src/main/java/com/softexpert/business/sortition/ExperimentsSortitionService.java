package com.softexpert.business.sortition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.business.experiment.AvailableExperimentsService;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;

@Stateless
public class ExperimentsSortitionService {

	@Inject
	private AvailableExperimentsService availableExperimentsService;
	@Inject
	private UserExperimentSortitionService experimentRandomService;
	@Inject
	private ElegibleSortitionService elegibleSortitionService;

	public List<UserExperiment> random(User user) {
		Collection<Experiment> experiments = availableExperimentsService.getAvailableExperiments();
		List<UserExperiment> randomExperiments = new ArrayList<UserExperiment>();
		experiments.stream().forEach(experiment -> randomExperiments.add(random(user, experiment)));
		return randomExperiments;
	}

	private UserExperiment random(User user, Experiment experiment) {
		if (elegibleSortitionService.isElegible(user, experiment))
			return experimentRandomService.sortition(experiment);
		return experimentRandomService.createEmptyExperiment(experiment);
	}

}
