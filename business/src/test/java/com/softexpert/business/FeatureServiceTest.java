package com.softexpert.business;

import static org.mockito.Mockito.when;

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
import com.softexpert.persistence.Feature;
import com.softexpert.persistence.QFeature;
import com.softexpert.repository.DefaultRepository;
import com.softexpert.repository.FeatureRepository;

public class FeatureServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private FeatureLoadService service;
	@Mock
	private DefaultRepository<Feature> repository;
	@Mock
	private Validator validator;
	@Mock
	private FeatureRepository featureRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = AppException.class)
	public void createWithValidationError() throws AppException {
		Feature sample = create(ID, "IPI");
		Set<ConstraintViolation<Feature>> violations = new HashSet<>();
		ConstraintViolation constraintViolation = Mockito.mock(ConstraintViolation.class);
		violations.add(constraintViolation);
		Mockito.when(constraintViolation.getMessage()).thenReturn("Error");
		Mockito.when(validator.validate(sample)).thenReturn(violations);

		service.create(sample);
	}

	@Test
	public void create() throws AppException {
		Feature sample = create(ID, "IPI");
		Mockito.when(repository.save(sample)).thenReturn(create(ID, "IPI"));

		Feature newSample = service.create(sample);

		Mockito.verify(repository).save(sample);
		MatcherAssert.assertThat(newSample.id, Matchers.equalTo(ID));
	}

	@Test
	public void edit() throws AppException {
		Feature sample = create(ID, "IPI");
		Mockito.when(repository.edit(sample)).thenReturn(create(ID, null));

		Feature newSample = service.edit(sample);

		Mockito.verify(repository).edit(sample);
		Mockito.verify(validator).validate(sample);
		MatcherAssert.assertThat(newSample.id, Matchers.equalTo(ID));
	}

	@Test
	public void delete() throws AppException {
		Mockito.when(featureRepository.findById(ID)).thenReturn(create(ID, null));

		service.delete(ID);

		Mockito.verify(repository).delete(create(ID, null));
	}

	@Test(expected = AppException.class)
	public void deleteWithError() throws AppException {
		Mockito.when(featureRepository.findById(ID)).thenThrow(new IllegalArgumentException("Error"));

		service.delete(ID);

		Mockito.verify(repository).delete(create(ID, null));
	}

	@Test
	public void listAll() {
		Mockito.when(repository.all(QFeature.feature, createConstructiorExpression())).thenReturn(getList());
		List<Feature> list = service.list("");

		Mockito.verify(repository).all(QFeature.feature, createConstructiorExpression());

		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(create(ID, null)));
	}

	@Test
	public void find() throws AppException {
		Mockito.when(featureRepository.findById(ID)).thenReturn(create(ID, null));

		Feature sample = service.find(ID);

		Mockito.verify(featureRepository).findById(ID);
		MatcherAssert.assertThat(sample, Matchers.equalTo(create(ID, null)));
	}

	@Test
	public void getFilter() throws AppException {
		String schearch = "Lala";
		Predicate filter = getFilter(schearch);
		MatcherAssert.assertThat(filter, Matchers.equalTo(QFeature.feature.name.containsIgnoreCase(schearch)));
	}

	@Test(expected = AppException.class)
	public void findByIdWithError() throws AppException {
		when(featureRepository.findById(ID)).thenThrow(new IllegalArgumentException("Error"));
		service.find(ID);
	}

	@Test(expected = AppException.class)
	public void editWithError() throws AppException {
		when(repository.edit(create(ID, null))).thenThrow(new IllegalArgumentException("Error"));
		service.edit(create(ID, null));
	}

	@Test(expected = AppException.class)
	public void createWithError() throws AppException {
		when(repository.save(create(ID, null))).thenThrow(new IllegalArgumentException("Error"));
		service.create(create(ID, null));
	}

	@Test
	public void listWithError() {
		String schearch = "search";
		Mockito.when(repository.list(QFeature.feature, getFilter(schearch), createConstructiorExpression())).thenReturn(getList());
		List<Feature> list = service.list(schearch);

		Mockito.verify(repository).list(QFeature.feature, getFilter(schearch), createConstructiorExpression());

		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(create(ID, null)));
	}

	private ConstructorExpression<Feature> createConstructiorExpression() {
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
