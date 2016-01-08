package com.softexpert.business;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.repository.UserExperimentRepository;

public class UserExperimentServiceTest {

	private static final long EXPERIMENT_ID = 1L;
	private static final long USER_ID = 11L;
	@InjectMocks
	private UserExperimentService service;
	@Mock
	private UserExperimentRepository repository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getUserWithExistent(){
		UserDTO userDTO = createUser();
		Mockito.when(repository.getUserExperiments(userDTO)).thenReturn(createUserExperiments());
		User user = service.getUser(userDTO);
		MatcherAssert.assertThat(user, Matchers.notNullValue());
		MatcherAssert.assertThat(user.id, Matchers.equalTo(USER_ID));
		MatcherAssert.assertThat(user.experiments.get(0).id, Matchers.equalTo(EXPERIMENT_ID));
	}

	@Test
	public void getUserWithNotExistent() {
		User user = service.getUser(createUser());
		MatcherAssert.assertThat(user, Matchers.nullValue());
	}
	private UserDTO createUser() {
		return UserDTO.builder().login("alisson@se.com").host("www.softexpert.com").build();
	}
	
	private List<UserExperiment> createUserExperiments() {
		return Arrays.asList(UserExperiment.builder()
				.id(EXPERIMENT_ID)
				.user(User.builder().id(USER_ID).build())
				.build());
	}

}
