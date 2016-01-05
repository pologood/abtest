package com.softexpert.business;

import static com.softexpert.business.ErrorMessages.SEARCH_ERROR;

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
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.QVariation;
import com.softexpert.persistence.Variation;
import com.softexpert.repository.DefaultRepository;
import com.softexpert.repository.ExperimentRepository;
import com.softexpert.repository.VariationRepository;

@Stateless
public class ExperimentLoadService {

	@Inject
	private DefaultRepository<Experiment> repository;
	@Inject
	private ExperimentRepository experimentRepository;
	@Inject
	private VariationRepository variationsRepository;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Experiment find(Long id) throws AppException {
		try {
			Experiment feature = experimentRepository.findById(QExperiment.experiment.id.eq(id),
					createConstructiorExpression());
			feature.variations = variationsRepository.list(QVariation.variation.experiment.id.eq(id),
					createABTestExpressionConstructor());
			return feature;
		} catch (Exception e) {
			throw new AppException(SEARCH_ERROR);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Experiment> list(String schearch) {
		if (Strings.isNullOrEmpty(schearch))
			return repository.all(QExperiment.experiment, createConstructiorExpression());
		return repository.list(QExperiment.experiment, QExperiment.experiment.name.containsIgnoreCase(schearch),
				createConstructiorExpression());
	}

	private ConstructorExpression<Variation> createABTestExpressionConstructor() {
		return Projections.constructor(Variation.class, QVariation.variation.id, QVariation.variation.name);
	}

	private ConstructorExpression<Experiment> createConstructiorExpression() {
		return Projections.constructor(Experiment.class, QExperiment.experiment.id, QExperiment.experiment.name,
				QExperiment.experiment.enabled, QExperiment.experiment.percentage);
	}

}
