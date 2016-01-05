package com.softexpert.business;


import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.business.exception.FeatureEnablingException;
import com.softexpert.persistence.QExperiment;
import com.softexpert.repository.ExperimentRepository;

@Stateless
public class ExperimentEnablingService {

	@Inject
	private ExperimentRepository repository;

	public void enabling(Long id, Boolean state) throws FeatureEnablingException {
		long linesChanged = executeUpdate(id, state);
		if(!hasExecutedUpdate(linesChanged))
			throw new FeatureEnablingException("Teste já está desabilito/habilitado");
	}

	private long executeUpdate(Long id, Boolean state) {
		return repository.toogleState(state, QExperiment.experiment.id.eq(id).and(QExperiment.experiment.enabled.eq(!state)));
	}
	
	private boolean hasExecutedUpdate(long linesChanged) {
		return linesChanged > 0;
	}


}
