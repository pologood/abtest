package com.softexpert.business;

import static com.softexpert.persistence.QFeature.feature;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.business.exception.FeatureEnablingException;
import com.softexpert.repository.FeatureRepository;

@Stateless
public class FeatureEnablingService {

	@Inject
	private FeatureRepository repository;

	public void enabling(Long id, Boolean state) throws FeatureEnablingException {
		long linesChanged = executeUpdate(id, state);
		if(!hasExecutedUpdate(linesChanged))
			throw new FeatureEnablingException("Teste já está desabilito/habilitado");
	}

	private long executeUpdate(Long id, Boolean state) {
		return repository.toogleState(state, feature.id.eq(id).and(feature.enabled.eq(!state)));
	}
	
	private boolean hasExecutedUpdate(long linesChanged) {
		return linesChanged > 0;
	}


}
