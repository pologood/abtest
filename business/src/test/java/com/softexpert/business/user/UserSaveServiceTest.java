package com.softexpert.business.user;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.softexpert.business.PersistenceService;
import com.softexpert.business.exception.AppException;
import com.softexpert.business.user.UserSaveService;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

public class UserSaveServiceTest {

	@InjectMocks
	private UserSaveService service;
	@Mock
	private PersistenceService<User> persistenceService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void save() throws AppException {
		User user = createUser();
		List<UserExperiment> experiments = createUserExperiments(2L,3L);
		service.save(user, experiments);
		User saved = User.builder().name("João").experiments(experiments).build();
		Mockito.verify(persistenceService).create(saved);
	}

	@Test
	public void saveWithoutExperiment() throws AppException {
		User user = createUser();
		List<UserExperiment> experiments =createUserExperiments(null ,Variation.builder().id(3L).build());
		service.save(user, experiments);
		User saved = createUser();
		Mockito.verify(persistenceService).create(saved);
	}

	@Test
	public void saveWithoutVariation() throws AppException {
		User user = createUser();
		List<UserExperiment> experiments =createUserExperiments(Experiment.builder().id(2L).build(), null);
		service.save(user, experiments);
		User saved = createUser();
		Mockito.verify(persistenceService).create(saved);
	}

	@Test
	public void saveWithtExperimentIdNull() throws AppException {
		User user = createUser();
		List<UserExperiment> experiments = createUserExperiments(null, 3L);
		service.save(user, experiments);
		User saved = createUser();
		Mockito.verify(persistenceService).create(saved);
	}

	@Test
	public void saveWithtVariationIdNull() throws AppException {
		User user = createUser();
		List<UserExperiment> experiments = createUserExperiments(3L, null);
		service.save(user, experiments);
		User saved = createUser();
		Mockito.verify(persistenceService).create(saved);
	}

	private List<UserExperiment> createUserExperiments(Long experimentId, Long variationId) {
		Experiment experiment = Experiment.builder().id(experimentId).build();
		Variation variation = Variation.builder().id(variationId).build();
		return createUserExperiments(experiment, variation);
	}

	private List<UserExperiment> createUserExperiments(Experiment experiment, Variation variation) {
		return Arrays.asList(UserExperiment.builder().experiment(experiment).variation(variation).build());
	}
	
	private User createUser() {
		return User.builder().name("João").build();
	}
}
