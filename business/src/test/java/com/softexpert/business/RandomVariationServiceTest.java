package com.softexpert.business;

import static com.softexpert.business.ExperimentTestBuilder.createExperiment;
import static com.softexpert.business.ExperimentTestBuilder.createSimpleExperiments;

import java.math.BigDecimal;
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
import org.mockito.Spy;

import com.softexpert.business.exception.AppException;
import com.softexpert.dto.ExperimentDTO;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

public class RandomVariationServiceTest {

	@InjectMocks
	private RandomVariationService service;
	@Mock
	private AvailableExperimentsService availableExperimentsService;
	@Spy
	private RandomUserExperimentService randomVariationRuleService = new RandomUserExperimentService();
	@Mock
	private UserSaveService userSaveService;
	@Mock
	private UserExperimentService userExperimentService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void randomWithAllUsersWithVariation() throws AppException {
		mock(createSimpleExperiments());
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.nullValue());
		Mockito.verify(userSaveService).save(Mockito.any(User.class), Mockito.anyList());
	}

	@Test
	public void randomWithAllUsersSimpleExperiment() throws AppException {
		mock(createSimpleExperiments("NEW"));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.equalTo("NEW"));
		Mockito.verify(userSaveService).save(Mockito.any(User.class), Mockito.anyList());
	}

	@Test
	public void randomWithExperiments() throws AppException {
		mock(Arrays.asList(createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"),
				createExperiment("DASHBOARD", new BigDecimal(50D), "NEW", "OLD"),
				createExperiment("CHATS", new BigDecimal(0D)),
				createExperiment("PORTAL", new BigDecimal(0D), "NEW", "DEFAULT")));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(4));
		Mockito.verify(userSaveService).save(Mockito.any(User.class), Mockito.anyList());
	}
	
	@Test
	public void randomWithExistentUser() throws AppException {
		UserDTO userDTO = createUser();
		Mockito.when(userExperimentService.getUser(userDTO)).thenReturn(createExistentUser());
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.equalTo("NEW"));
		Mockito.verify(userSaveService, Mockito.never()).save(Mockito.any(User.class), Mockito.anyList());
	}

	private User createExistentUser(){
		Experiment experiment = Experiment.builder().name("DEFAULT_FRAME").build();
		Variation variation = Variation.builder().name("NEW").build();
		List<UserExperiment> experiments = Arrays.asList(UserExperiment
				.builder()
				.experiment(experiment)
				.variation(variation)
				.build());
		return User.builder().experiments(experiments).build();
	}
	private UserDTO createUser() {
		return UserDTO.builder().code("1").name("João Silva").login("joao.silva").department("Tec")
				.host("www.softexpert.com").build();
	}

	private void mock(List<Experiment> experiments) {
		Mockito.when(availableExperimentsService.getAvailableExperiments()).thenReturn(experiments);
	}

}
