package com.softexpert.business;

import static com.softexpert.persistence.QFeature.feature;

import javax.inject.Inject;

import com.softexpert.business.exception.FeatureEnablingException;
import com.softexpert.persistence.QFeature;
import com.softexpert.repository.FeatureRepository;

public class FeatureEnablingService {

	@Inject
	private FeatureRepository repository;

	public boolean toggle(Long id, Boolean state) throws FeatureEnablingException {
		if (updateState(id, state))
			return state;
		throw new FeatureEnablingException("Teste já está desabilito/habilitado");
	}

	private boolean updateState(Long id, Boolean state) {
		return executeUpdate(id, state) > 0;
	}

	private long executeUpdate(Long id, Boolean state) {
		return repository.toogleState(state, feature.id.eq(id).and(feature.enabled.eq(!state)));
	}

}
