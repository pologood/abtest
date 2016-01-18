package com.softexpert.business.experiment;


import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.business.exception.ExperimentEnablingException;
import com.softexpert.persistence.QExperiment;
import com.softexpert.repository.experiment.ExperimentRepository;

@Stateless
public class ExperimentEnablingService {

	@Inject
	private ExperimentRepository repository;

	public void enabling(Long id, Boolean state) throws ExperimentEnablingException {
		long linesChanged = executeUpdate(id, state);
		if(!hasExecutedUpdate(linesChanged))
			throw new ExperimentEnablingException("Teste já está desabilito/habilitado");
	}

	private long executeUpdate(Long id, Boolean state) {
		return repository.toogleState(state, QExperiment.experiment.id.eq(id).andAnyOf(QExperiment.experiment.enabled.eq(!state).or(QExperiment.experiment.enabled.isNull())));
	}
	
	private boolean hasExecutedUpdate(long linesChanged) {
		return linesChanged > 0;
	}


}
