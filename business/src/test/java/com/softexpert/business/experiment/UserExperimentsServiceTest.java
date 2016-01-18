package com.softexpert.business.experiment;

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
import com.softexpert.business.sortition.ElegibleSortitionService;
import com.softexpert.business.sortition.ExperimentsSortitionService;
import com.softexpert.business.sortition.UserExperimentSortitionService;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;

public class UserExperimentsServiceTest {

	@InjectMocks
	private ExperimentsSortitionService service;
	@Mock
	private AvailableExperimentsService availableExperimentsService;
	@Spy
	private UserExperimentSortitionService experimentRandomService;
	@Mock
	private ElegibleSortitionService elegibleSortitionService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		Mockito.when(elegibleSortitionService.isElegible(Mockito.any(User.class), Mockito.any(Experiment.class))).thenReturn(true);
	}

	@Test
	public void randomWithAllUsersWithVariation() throws AppException {
		mock(createSimpleExperiments());
		List<UserExperiment> experiments = service.sortition(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithAllUsersSimpleExperiment() throws AppException {
		mock(createSimpleExperiments("NEW"));
		List<UserExperiment> experiments = service.sortition(createUser());
		MatcherAssert.assertThat(experiments.get(0).experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variation.name, Matchers.equalTo("NEW"));
	}

	@Test
	public void randomWithExperiments() throws AppException {
		mock(Arrays.asList(createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"),
				createExperiment("DASHBOARD", new BigDecimal(50D), "NEW", "OLD"),
				createExperiment("CHATS", new BigDecimal(0D)),
				createExperiment("PORTAL", new BigDecimal(0D), "NEW", "DEFAULT")));
		List<UserExperiment> experiments = service.sortition(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(4));
	}

	@Test
	public void randomWithIlegibileUser() throws AppException {
		mock(createSimpleExperiments("NEW"));
		Mockito.when(elegibleSortitionService.isElegible(Mockito.any(User.class), Mockito.any(Experiment.class)))
				.thenReturn(false);
		List<UserExperiment> experiments = service.sortition(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variation.name, Matchers.nullValue());
		Mockito.verify(experimentRandomService).createEmptyExperiment(Mockito.any(Experiment.class));

	}

	private User createUser() {
		return User.builder()
				.code("1")
				.name("João Silva")
				.login("joao.silva")
				.department("Tec")
				.host("www.softexpert.com")
				.build();
	}

	private void mock(List<Experiment> experiments) {
		Mockito.when(availableExperimentsService.getAvailableExperiments()).thenReturn(experiments);
	}
}
