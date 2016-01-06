package com.softexpert.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
	public void getSingleResult() {
		mockSimpleExperiment();
		Collection<Experiment> availableExperiments = service.getAvailableExperiments();
		MatcherAssert.assertThat(availableExperiments, Matchers.notNullValue());
		MatcherAssert.assertThat(availableExperiments, Matchers.hasSize(1));
	}
	
	@Test
	public void getSingleResultWithDetails() {
		mockSimpleExperiment();
		Collection<Experiment> availableExperiments = service.getAvailableExperiments();
		Experiment experiment = availableExperiments.iterator().next();
		MatcherAssert.assertThat(experiment.id, Matchers.equalTo(EXPERIMENT_ID));
		MatcherAssert.assertThat(experiment.name, Matchers.equalTo(EXPERIMENT_NAME));
		MatcherAssert.assertThat(experiment.variations, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiment.variations.get(0).id, Matchers.equalTo(VARIATION_ID));
		MatcherAssert.assertThat(experiment.variations.get(0).name, Matchers.equalTo(VARIATION_NAME));
	}

	@Test
	public void getManyResults() {
		List<Variation> variations = createVariations();
		Mockito.when(repository.list(QExperiment.experiment.enabled.isTrue())).thenReturn(variations);
		List<Experiment> availableExperiments = service.getAvailableExperiments();
		MatcherAssert.assertThat(availableExperiments, Matchers.notNullValue());
		MatcherAssert.assertThat(availableExperiments, Matchers.hasSize(2));
		MatcherAssert.assertThat(availableExperiments.get(0).variations, Matchers.hasSize(1));
		MatcherAssert.assertThat(availableExperiments.get(1).variations, Matchers.hasSize(2));
	}

	@Test
	public void getWithEmptyList() {
		Mockito.when(repository.list(QExperiment.experiment.enabled.isTrue())).thenReturn(Collections.emptyList());
		List<Experiment> availableExperiments = service.getAvailableExperiments();
		MatcherAssert.assertThat(availableExperiments, Matchers.notNullValue());
		MatcherAssert.assertThat(availableExperiments, Matchers.hasSize(0));
	}
	
	private List<Variation> createVariations() {
		List<Variation> variations = new ArrayList<>(createDefaultFrameVariation());
		Experiment dashbord = Experiment.builder().id(19L).name("DASHBORD").build();
		variations.add( Variation.builder().id(54L).name("WITH_PORTAL").experiment(dashbord).build());
		variations.add( Variation.builder().id(96L).name("WITHOUT_PORTAL").experiment(dashbord).build());
		return variations;
	}
	
	private void mockSimpleExperiment() {
		List<Variation> variations = createDefaultFrameVariation();
		Mockito.when(repository.list(QExperiment.experiment.enabled.isTrue())).thenReturn(variations);
	}

	private List<Variation> createDefaultFrameVariation() {
		Experiment experiment = Experiment.builder().id(EXPERIMENT_ID).name(EXPERIMENT_NAME).build();
		return Arrays.asList(Variation.builder().id(VARIATION_ID).name(VARIATION_NAME).experiment(experiment).build());
	}
}
