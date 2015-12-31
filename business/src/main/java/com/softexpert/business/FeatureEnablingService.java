package com.softexpert.business;

import javax.inject.Inject;

import com.softexpert.business.exception.FeatureEnablingException;
import com.softexpert.persistence.QFeature;
import com.softexpert.repository.FeatureRepository;

public class FeatureEnablingService {

	@Inject
	private FeatureRepository repository;
	
	public boolean toggle(Long id, Boolean state) throws FeatureEnablingException{
		QFeature feature = QFeature.feature;
		if( repository.toogleState(state, feature.id.eq(id).and(feature.enabled.eq(!state))) > 0)
			return state;
		throw new FeatureEnablingException("Teste já está desabilito/habilitado");
	}

}
