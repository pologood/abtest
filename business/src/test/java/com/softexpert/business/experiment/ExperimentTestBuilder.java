package com.softexpert.business.experiment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

public class ExperimentTestBuilder {

	public static List<Experiment> createSimpleExperiments(String... variation) {
		return Arrays.asList(createExperiment("DEFAULT_FRAME", new BigDecimal(100D), variation));
	}

	public static List<UserExperiment> createSimpleUserExperiments(String variation) {
		return Arrays.asList(createUserExperiment("DEFAULT_FRAME", new BigDecimal(100D), variation));
	}

	public static List<UserExperiment> createSimpleUserExperiments() {
		return Arrays.asList(createUserExperiment("DEFAULT_FRAME", new BigDecimal(100D), null));
	}
	
	public static UserExperiment createUserExperiment(String name, BigDecimal percentage, String variationName) {
		Variation variation = createVariation(variationName);
		Experiment experiment = Experiment.builder().name(name).percentage(percentage).build();
		return UserExperiment.builder().experiment(experiment).variation(variation).build();
	}
	
	public static UserExperiment createUserExperiment(String name, BigDecimal percentage) {
		Variation variation = createVariation(null);
		Experiment experiment = Experiment.builder().name(name).percentage(percentage).build();
		return UserExperiment.builder().experiment(experiment).variation(variation).build();
	}

	public static Experiment createExperiment(String name, BigDecimal percentage, String... variation) {
		return Experiment.builder().name(name).percentage(percentage).variations(createVariations(variation)).build();
	}

	public static List<Variation> createVariations(String[] names) {
		List<Variation> variations = new ArrayList<>();
		for (String name : names)
			variations.add(createVariation(name));
		return variations;
	}

	private static Variation createVariation(String name) {
		return Variation.builder().name(name).build();
	}
}
