package com.softexpert.business;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

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
import com.softexpert.persistence.ABTest;
import com.softexpert.persistence.Feature;
import com.softexpert.persistence.QABTest;
import com.softexpert.persistence.QFeature;
import com.softexpert.repository.ABTestRepository;
import com.softexpert.repository.DefaultRepository;
import com.softexpert.repository.FeatureRepository;

public class FeatureLoadServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private FeatureLoadService service;
	@Mock
	private DefaultRepository<Feature> repository;
	@Mock
	private FeatureRepository featureRepository;
	@Mock
	private ABTestRepository abTestRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void listAll() {
		Mockito.when(repository.all(QFeature.feature, createFeatureConstructiorExpression())).thenReturn(getList());
		List<Feature> list = service.list("");
		Mockito.verify(repository).all(QFeature.feature, createFeatureConstructiorExpression());
		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(create(ID, null)));
	}

	@Test
	public void find() throws AppException {
		long testId = 89L;
		Mockito.when(featureRepository.findById(ID, createFeatureConstructiorExpression())).thenReturn(create(ID, null));
		Mockito.when(abTestRepository.listFromFeature(ID, createABTestConstructor())).thenReturn(Arrays.asList(ABTest.builder().id(testId).build()));
		Feature sample = service.find(ID);
		Mockito.verify(abTestRepository).listFromFeature(ID, createABTestConstructor());
		Mockito.verify(featureRepository).findById(ID, createFeatureConstructiorExpression());
		MatcherAssert.assertThat(sample, Matchers.equalTo(create(ID, null)));
		MatcherAssert.assertThat(sample.tests, Matchers.hasSize(1));
		MatcherAssert.assertThat(sample.tests.get(0).id, Matchers.equalTo(testId));
	}

	@Test
	public void getFilter() throws AppException {
		String schearch = "Lala";
		Predicate filter = getFilter(schearch);
		MatcherAssert.assertThat(filter, Matchers.equalTo(QFeature.feature.name.containsIgnoreCase(schearch)));
	}

	@Test(expected = AppException.class)
	public void findByIdWithError() throws AppException {
		when(featureRepository.findById(ID,createFeatureConstructiorExpression())).thenThrow(new IllegalArgumentException("Error"));
		service.find(ID);
	}

	@Test
	public void listWithError() {
		String schearch = "search";
		Mockito.when(repository.list(QFeature.feature, getFilter(schearch), createFeatureConstructiorExpression())).thenReturn(getList());
		List<Feature> list = service.list(schearch);
		Mockito.verify(repository).list(QFeature.feature, getFilter(schearch), createFeatureConstructiorExpression());
		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(create(ID, null)));
	}

	private ConstructorExpression<ABTest> createABTestConstructor() {
		return Projections.constructor(ABTest.class, QABTest.aBTest.id,QABTest.aBTest.name);
	}
	
	private ConstructorExpression<Feature> createFeatureConstructiorExpression() {
		return Projections.constructor(Feature.class,QFeature.feature.id, QFeature.feature.name, QFeature.feature.enabled, QFeature.feature.percentage);
	}

	private Predicate getFilter(String schearch) {
		return QFeature.feature.name.containsIgnoreCase(schearch);
	}

	private List<Feature> getList() {
		return Collections.singletonList(create(ID, null));
	}

	private Feature create(Long id, String name) {
		return Feature.builder()
				.id(id)
				.name(name)
				.build();
	}

}
