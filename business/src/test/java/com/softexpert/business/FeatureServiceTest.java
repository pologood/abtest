package com.softexpert.business;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.querydsl.core.types.Predicate;
import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.Feature;
import com.softexpert.persistence.QFeature;
import com.softexpert.repository.DefaultRepository;

public class FeatureServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private FeatureService service;
	@Mock
	private DefaultRepository<Feature> repository;
	@Mock
	private Validator validator;

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
		Mockito.when(repository.findById(ID, Feature.class)).thenReturn(create(ID, null));

		service.delete(ID);

		Mockito.verify(repository).delete(create(ID, null));
	}

	@Test(expected = AppException.class)
	public void deleteWithError() throws AppException {
		Mockito.when(repository.findById(ID, Feature.class)).thenThrow(new IllegalArgumentException("Error"));

		service.delete(ID);

		Mockito.verify(repository).delete(create(ID, null));
	}

	@Test
	public void listAll() {
		Mockito.when(repository.all(QFeature.feature, QFeature.feature)).thenReturn(getList());
		List<Feature> list = service.list("");

		Mockito.verify(repository).all(QFeature.feature, QFeature.feature);

		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(create(ID, null)));
	}

	@Test
	public void find() throws AppException {
		Mockito.when(repository.findById(ID, Feature.class)).thenReturn(create(ID, null));

		Feature sample = service.find(ID);

		Mockito.verify(repository).findById(ID, Feature.class);
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
		when(repository.findById(ID, Feature.class)).thenThrow(new IllegalArgumentException("Error"));
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
		Mockito.when(repository.list(QFeature.feature, getFilter(schearch), QFeature.feature)).thenReturn(getList());
		List<Feature> list = service.list(schearch);

		Mockito.verify(repository).list(QFeature.feature, getFilter(schearch), QFeature.feature);

		MatcherAssert.assertThat(list, Matchers.hasSize(1));
		MatcherAssert.assertThat(list, Matchers.contains(create(ID, null)));
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
