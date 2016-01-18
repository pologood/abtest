package com.softexpert.business.experiment;

import static com.softexpert.business.ErrorMessages.SEARCH_ERROR;
import static com.softexpert.persistence.QExperiment.experiment;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.google.common.base.Strings;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.QVariation;
import com.softexpert.persistence.Variation;
import com.softexpert.repository.DefaultRepository;
import com.softexpert.repository.experiment.ExperimentRepository;
import com.softexpert.repository.experiment.VariationRepository;

@Stateless
public class ExperimentLoaderService {

	@Inject
	private DefaultRepository<Experiment> repository;
	@Inject
	private ExperimentRepository experimentRepository;
	@Inject
	private VariationRepository variationsRepository;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Experiment find(Long id) throws AppException {
		try {
			Experiment feature = experimentRepository.findById(experiment.id.eq(id), createAllConstructiorExpression());
			feature.variations = variationsRepository.list(QVariation.variation.experiment.id.eq(id),
					createABTestExpressionConstructor());
			return feature;
		} catch (Exception e) {
			throw new AppException(SEARCH_ERROR, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Experiment> list(String schearch) {
		if (Strings.isNullOrEmpty(schearch))
			return repository.all(experiment, createConstructiorExpression());
		return repository.list(experiment, experiment.name.containsIgnoreCase(schearch),
				createConstructiorExpression());
	}

	private ConstructorExpression<Variation> createABTestExpressionConstructor() {
		return Projections.constructor(Variation.class, QVariation.variation.id, QVariation.variation.name);
	}

	private ConstructorExpression<Experiment> createConstructiorExpression() {
		return Projections.constructor(Experiment.class, experiment.id, experiment.name, experiment.enabled,
				experiment.percentage);
	}

	private ConstructorExpression<Experiment> createAllConstructiorExpression() {
		return Projections.constructor(Experiment.class, experiment.id, experiment.name, experiment.enabled,
				experiment.percentage, experiment.domains, experiment.groups, experiment.users);
	}

}
