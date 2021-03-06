package com.softexpert.business.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.QVariation;
import com.softexpert.persistence.Variation;
import com.softexpert.repository.AvailableExperimentsRepository;

@Stateless
public class AvailableExperimentsService {

	@Inject
	private AvailableExperimentsRepository repository;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Experiment> getAvailableExperiments() {
		List<Variation> variations = repository.list(QExperiment.experiment.enabled.isTrue(), buildConstructor());
		List<Experiment> experiments = new ArrayList<>();
		variations.stream().forEach(variation -> {
			addVariant(experiments, variation);
		});
		return experiments;
	}

	private void addVariant(List<Experiment> experiments, Variation variation) {
		Experiment experiment = getExperiment(experiments, variation);
		if (experiment == null)
			experiments.add(getExperiment(variation));
		else
			experiment.variations.add(variation);
	}

	private Experiment getExperiment(List<Experiment> experiments, Variation variation) {
		if (!experiments.isEmpty()) {
			Optional<Experiment> experiment = experiments.stream().filter(e -> e.id.equals(variation.experiment.id))
					.findFirst();
			if (experiment.isPresent())
				return experiment.get();
		}
		return null;
	}

	private Experiment getExperiment(Variation variant) {
		Experiment experiment = variant.experiment;
		experiment.variations = new ArrayList<>();
		experiment.variations.add(variant);
		return experiment;
	}

	private ConstructorExpression<Variation> buildConstructor() {
		return Projections.constructor(Variation.class, QVariation.variation.id, QVariation.variation.name,
				QVariation.variation.experiment);
	}
}
