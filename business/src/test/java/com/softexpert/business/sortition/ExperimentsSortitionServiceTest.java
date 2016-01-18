package com.softexpert.business.sortition;

import static com.softexpert.business.experiment.ExperimentTestBuilder.createExperiment;
import static com.softexpert.business.experiment.ExperimentTestBuilder.createSimpleExperiments;

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
import com.softexpert.business.experiment.AvailableExperimentsService;
import com.softexpert.business.sortition.ElegibleSortitionService;
import com.softexpert.business.sortition.ExperimentsSortitionService;
import com.softexpert.business.sortition.UserExperimentSortitionService;
import com.softexpert.business.user.UserSaveService;
import com.softexpert.business.user.UserSearchService;
import com.softexpert.dto.ExperimentDTO;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.persistence.Variation;

public class ExperimentsSortitionServiceTest {

	@InjectMocks
	private ExperimentsSortitionService service;
	@Mock
	private AvailableExperimentsService availableExperimentsService;
	@Mock
	private UserSaveService userSaveService;
	@Mock
	private UserSearchService userExperimentService;
	@Spy
	private UserExperimentSortitionService experimentRandomService;
	@Mock
	private ElegibleSortitionService elegibleSortionService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(elegibleSortionService.isElegible(Mockito.any(User.class), Mockito.any(Experiment.class)))
				.thenReturn(true);
	}

	@Test
	public void randomWithAllUsersWithVariation() throws AppException {
		mock(createSimpleExperiments());
		List<ExperimentDTO> experiments = service.sortionOrSearch(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.nullValue());
		Mockito.verify(userSaveService).save(Mockito.any(User.class), Mockito.anyList());
	}

	@Test
	public void randomWithAllUsersSimpleExperiment() throws AppException {
		mock(createSimpleExperiments("NEW"));
		List<ExperimentDTO> experiments = service.sortionOrSearch(createUser());
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
		List<ExperimentDTO> experiments = service.sortionOrSearch(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(4));
		Mockito.verify(userSaveService).save(Mockito.any(User.class), Mockito.anyList());
	}

	@Test
	public void randomWithExistentUser() throws AppException {
		UserDTO userDTO = createUser();
		Mockito.when(userExperimentService.search(userDTO)).thenReturn(createExistentUser());
		List<ExperimentDTO> experiments = service.sortionOrSearch(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.equalTo("NEW"));
		Mockito.verify(userSaveService, Mockito.never()).save(Mockito.any(User.class), Mockito.anyList());
	}

	@Test
	public void randomWithIlegibileUser() throws AppException {
		mock(createSimpleExperiments("NEW"));
		Mockito.when(elegibleSortionService.isElegible(Mockito.any(User.class), Mockito.any(Experiment.class)))
				.thenReturn(false);
		List<ExperimentDTO> experiments = service.sortionOrSearch(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.nullValue());
		Mockito.verify(userSaveService).save(Mockito.any(User.class), Mockito.anyList());
		Mockito.verify(experimentRandomService).createEmptyExperiment(Mockito.any(Experiment.class));

	}

	private User createExistentUser() {
		Experiment experiment = Experiment.builder().name("DEFAULT_FRAME").build();
		Variation variation = Variation.builder().name("NEW").build();
		List<UserExperiment> experiments = Arrays
				.asList(UserExperiment.builder().experiment(experiment).variation(variation).build());
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
