package com.softexpert.business;

import static com.softexpert.persistence.QFeature.feature;

import javax.inject.Inject;

import com.softexpert.business.exception.FeatureEnablingException;
import com.softexpert.repository.FeatureRepository;

public class FeatureEnablingService {

	@Inject
	private FeatureRepository repository;

	public void toggle(Long id, Boolean state) throws FeatureEnablingException {
		long linesChanged = executeUpdate(id, state);
		if(!hasChangedState(linesChanged))
			throw new FeatureEnablingException("Teste já está desabilito/habilitado");
	}

	private boolean hasChangedState(long linesChanged) {
		return linesChanged > 0;
	}

	private long executeUpdate(Long id, Boolean state) {
		return repository.toogleState(state, feature.id.eq(id).and(feature.enabled.eq(!state)));
	}

}
