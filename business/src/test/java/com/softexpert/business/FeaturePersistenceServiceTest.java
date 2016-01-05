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

public class FeaturePersistenceServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private FeaturePersistenceService service;
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

	@Test(expected = AppException.class)
	public void createWithError() throws AppException {
		Feature sample = create(ID, "IPI");
		Mockito.when(repository.save(sample)).thenThrow(new IllegalArgumentException());
		service.create(sample);
		Mockito.verify(repository).save(sample);
		Mockito.verify(validator).validate(sample);
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

	@Test(expected = AppException.class)
	public void editWithError() throws AppException {
		Feature sample = create(ID, "IPI");
		Mockito.when(repository.edit(sample)).thenThrow(new IllegalArgumentException());
		service.edit(sample);
		Mockito.verify(repository).edit(sample);
		Mockito.verify(validator).validate(sample);
	}
	
	@Test
	public void delete() throws AppException {
		service.delete(ID);

		Mockito.verify(repository).delete(QFeature.feature, QFeature.feature.id.eq(ID));
	}

	@Test(expected = AppException.class)
	public void deleteWithError() throws AppException {
		Mockito.doThrow(new IllegalArgumentException("Error")).when(repository).delete(QFeature.feature, QFeature.feature.id.eq(ID));
		service.delete(ID);
	}


	private Feature create(Long id, String name) {
		return Feature.builder()
				.id(id)
				.name(name)
				.build();
	}

}
