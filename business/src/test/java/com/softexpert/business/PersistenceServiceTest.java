package com.softexpert.business;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.Experiment;
import com.softexpert.repository.DefaultRepository;

public class PersistenceServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private PersistenceService<Experiment> service;
	@Mock
	private DefaultRepository<Experiment> repository;
	@Mock
	private ValidationService validator;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = AppException.class)
	public void createWithValidationError() throws AppException {
		Experiment sample = create(ID, "IPI");
		Mockito.doThrow(new AppException("error")).when(validator).validate(sample);
		service.create(sample);
	}

	@Test
	public void create() throws AppException {
		Experiment sample = create(ID, "IPI");
		Mockito.when(repository.save(sample)).thenReturn(create(ID, "IPI"));
		Experiment newSample = service.create(sample);
		Mockito.verify(repository).save(sample);
		MatcherAssert.assertThat(newSample.id, Matchers.equalTo(ID));
	}

	@Test(expected = AppException.class)
	public void createWithError() throws AppException {
		Experiment sample = create(ID, "IPI");
		Mockito.when(repository.save(sample)).thenThrow(new IllegalArgumentException());
		service.create(sample);
		Mockito.verify(repository).save(sample);
		Mockito.verify(validator).validate(sample);
	}

	@Test
	public void edit() throws AppException {
		Experiment sample = create(ID, "IPI");
		Mockito.when(repository.edit(sample)).thenReturn(create(ID, null));

		Experiment newSample = service.edit(sample);

		Mockito.verify(repository).edit(sample);
		Mockito.verify(validator).validate(sample);
		MatcherAssert.assertThat(newSample.id, Matchers.equalTo(ID));
	}

	@Test(expected = AppException.class)
	public void editWithError() throws AppException {
		Experiment sample = create(ID, "IPI");
		Mockito.when(repository.edit(sample)).thenThrow(new IllegalArgumentException());
		service.edit(sample);
		Mockito.verify(repository).edit(sample);
		Mockito.verify(validator).validate(sample);
	}

	private Experiment create(Long id, String name) {
		return Experiment.builder().id(id).name(name).build();
	}

}
