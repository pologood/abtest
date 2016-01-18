package com.softexpert.business.experiment;

import static org.mockito.Mockito.when;

import java.util.Arrays;
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

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.softexpert.business.exception.AppException;
import com.softexpert.business.experiment.ExperimentLoaderService;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.QVariation;
import com.softexpert.persistence.Variation;
import com.softexpert.repository.DefaultRepository;
import com.softexpert.repository.experiment.ExperimentRepository;
import com.softexpert.repository.experiment.VariationRepository;

public class ExperimentLoaderServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private ExperimentLoaderService service;
	@Mock
	private DefaultRepository<Experiment> repository;
	@Mock
	private ExperimentRepository experimentRepository;
	@Mock
	private VariationRepository variationRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void listAll() {
		Mockito.when(repository.all(QExperiment.experiment, createFeatureConstructiorExpression())).thenReturn(getList());
		List<Experiment> list = service.list("");
		Mockito.verify(repository).all(QExperiment.experiment, createFeatureConstructiorExpression());
		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(create(ID, null)));
	}

	@Test
	public void find() throws AppException {
		long testId = 89L;
		Mockito.when(experimentRepository.findById(QExperiment.experiment.id.eq(ID), createFinderFeatureConstructiorExpression())).thenReturn(create(ID, null));
		Mockito.when(variationRepository.list(QVariation.variation.experiment.id.eq(ID), createABTestConstructor())).thenReturn(Arrays.asList(Variation.builder().id(testId).build()));
		Experiment sample = service.find(ID);
		Mockito.verify(variationRepository).list(QVariation.variation.experiment.id.eq(ID), createABTestConstructor());
		Mockito.verify(experimentRepository).findById(QExperiment.experiment.id.eq(ID), createFinderFeatureConstructiorExpression());
		MatcherAssert.assertThat(sample, Matchers.equalTo(create(ID, null)));
		MatcherAssert.assertThat(sample.variations, Matchers.hasSize(1));
		MatcherAssert.assertThat(sample.variations.get(0).id, Matchers.equalTo(testId));
	}

	@Test
	public void getFilter() throws AppException {
		String schearch = "Lala";
		Predicate filter = getFilter(schearch);
		MatcherAssert.assertThat(filter, Matchers.equalTo(QExperiment.experiment.name.containsIgnoreCase(schearch)));
	}

	@Test(expected = AppException.class)
	public void findByIdWithError() throws AppException {
		when(experimentRepository.findById(QExperiment.experiment.id.eq(ID),createFeatureConstructiorExpression())).thenThrow(new IllegalArgumentException("Error"));
		service.find(ID);
	}

	@Test
	public void listWithError() {
		String schearch = "search";
		Mockito.when(repository.list(QExperiment.experiment, getFilter(schearch), createFeatureConstructiorExpression())).thenReturn(getList());
		List<Experiment> list = service.list(schearch);
		Mockito.verify(repository).list(QExperiment.experiment, getFilter(schearch), createFeatureConstructiorExpression());
		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(create(ID, null)));
	}

	private ConstructorExpression<Variation> createABTestConstructor() {
		return Projections.constructor(Variation.class, QVariation.variation.id, QVariation.variation.name);
	}
	
	private ConstructorExpression<Experiment> createFeatureConstructiorExpression() {
		return Projections.constructor(Experiment.class,QExperiment.experiment.id, QExperiment.experiment.name, QExperiment.experiment.enabled, QExperiment.experiment.percentage);
	}

	private ConstructorExpression<Experiment> createFinderFeatureConstructiorExpression() {
		return Projections.constructor(Experiment.class,QExperiment.experiment.id, QExperiment.experiment.name, QExperiment.experiment.enabled, QExperiment.experiment.percentage, QExperiment.experiment.domains, QExperiment.experiment.groups, QExperiment.experiment.users);
	}
	
	private Predicate getFilter(String schearch) {
		return QExperiment.experiment.name.containsIgnoreCase(schearch);
	}

	private List<Experiment> getList() {
		return Collections.singletonList(create(ID, null));
	}

	private Experiment create(Long id, String name) {
		return Experiment.builder()
				.id(id)
				.name(name)
				.build();
	}

}
