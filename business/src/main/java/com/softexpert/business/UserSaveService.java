package com.softexpert.business;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

@Stateless
public class UserSaveService {

	@Inject
	private PersistenceService<User> service;

	public void save(User user, List<UserExperiment> experiments) throws AppException {
		user.experiments = new ArrayList<>();
		experiments.stream().forEach(experiment -> {
			extracted(user, experiment);
		});
		service.create(user);

	}

	private void extracted(User user, UserExperiment userExperiment) {
		if (!canAddExperiment(userExperiment))
			return;
		user.experiments.add(UserExperiment.builder()
						.experiment(Experiment.builder().id(userExperiment.experiment.id).build())
						.variation(Variation.builder().id(userExperiment.variation.id).build())
						.user(user)
						.build());
	}

	private boolean canAddExperiment(UserExperiment userExperiment) {
		return !(userExperiment.experiment == null 
				|| userExperiment.experiment.id == null 
				|| userExperiment.variation == null
				|| userExperiment.variation.id == null);
	}
}
