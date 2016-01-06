package com.softexpert.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.Variation;
import com.softexpert.repository.AvailableExperimentsRepository;

@Stateless
public class AvailableExperimentsService {

	@Inject
	private AvailableExperimentsRepository repository;

	public List<Experiment> getAvailableExperiments() {
		List<Variation> variations = repository.list(QExperiment.experiment.enabled.isTrue());
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
		if (!experiments.isEmpty()){
			Optional<Experiment> experiment = experiments.stream().filter(e -> e.id.equals(variation.experiment.id)).findFirst();
			if(experiment.isPresent())
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
}
