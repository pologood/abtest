package com.softexpert.business;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.Variation;
import com.softexpert.repository.AvailableExperimentsRepository;

public class AvailableExperimentsServiceTest {

	private static final long VARIATION_ID = 1L;
	private static final String VARIATION_NAME = "NEW";
	private static final String EXPERIMENT_NAME = "DEFAULT_FRAME";
	private static final long EXPERIMENT_ID = 5L;
	@InjectMocks
	private AvailableExperimentsService service;
	@Mock
	private AvailableExperimentsRepository repository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getAvailableExperimentsWithSingle() {
		Experiment experiment = Experiment.builder().id(EXPERIMENT_ID).name(EXPERIMENT_NAME).build();
		List<Variation> variations = Arrays
				.asList(Variation.builder().id(VARIATION_ID).name(VARIATION_NAME).experiment(experiment).build());
		Mockito.when(repository.list(QExperiment.experiment.enabled.isTrue())).thenReturn(variations);

		Collection<Experiment> availableExperiments = service.getAvailableExperiments();
		MatcherAssert.assertThat(availableExperiments, Matchers.notNullValue());
		MatcherAssert.assertThat(availableExperiments, Matchers.hasSize(1));
		Experiment next = availableExperiments.iterator().next();
		MatcherAssert.assertThat(next.id, Matchers.equalTo(EXPERIMENT_ID));
		MatcherAssert.assertThat(next.name, Matchers.equalTo(EXPERIMENT_NAME));

		MatcherAssert.assertThat(next.variations, Matchers.hasSize(1));
		MatcherAssert.assertThat(next.variations.get(0).id, Matchers.equalTo(VARIATION_ID));
		MatcherAssert.assertThat(next.variations.get(0).name, Matchers.equalTo(VARIATION_NAME));
	}

	@Test
	public void getAvailableExperiments() {
		Experiment defaultFrame = Experiment.builder().id(EXPERIMENT_ID).name(EXPERIMENT_NAME).build();
		Variation newVersion = Variation.builder().id(VARIATION_ID).name(VARIATION_NAME).experiment(defaultFrame)
				.build();
		Variation oldVersion = Variation.builder().id(8L).name(VARIATION_NAME).experiment(defaultFrame).build();

		Experiment dashbord = Experiment.builder().id(19L).name("DASHBORD").build();
		Variation withPortal = Variation.builder().id(54L).name("WITH_PORTAL").experiment(dashbord).build();
		Variation withoutPortal = Variation.builder().id(96L).name("WITHOUT_PORTAL").experiment(dashbord).build();

		List<Variation> variations = Arrays.asList(newVersion, oldVersion, withPortal, withoutPortal);
		Mockito.when(repository.list(QExperiment.experiment.enabled.isTrue())).thenReturn(variations);

		Collection<Experiment> availableExperiments = service.getAvailableExperiments();
		MatcherAssert.assertThat(availableExperiments, Matchers.notNullValue());
		MatcherAssert.assertThat(availableExperiments, Matchers.hasSize(2));
		Experiment next = availableExperiments.iterator().next();
		MatcherAssert.assertThat(next.id, Matchers.equalTo(EXPERIMENT_ID));
		MatcherAssert.assertThat(next.name, Matchers.equalTo(EXPERIMENT_NAME));
		MatcherAssert.assertThat(next.variations, Matchers.hasSize(2));
		
		Experiment next2 = availableExperiments.iterator().next();
		MatcherAssert.assertThat(next2.variations, Matchers.hasSize(2));
	}
}
