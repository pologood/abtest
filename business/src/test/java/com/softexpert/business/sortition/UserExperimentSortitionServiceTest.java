package com.softexpert.business.sortition;

import static com.softexpert.business.experiment.ExperimentTestBuilder.createExperiment;

import java.math.BigDecimal;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.softexpert.persistence.UserExperiment;

public class UserExperimentSortitionServiceTest {

	@InjectMocks
	private UserExperimentSortitionService service;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void randomWithSingleVariation() {
		UserExperiment experiment = service.sortition(createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"));
		MatcherAssert.assertThat(experiment.experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("NEW"));
	}

	@Test
	public void randomWithLowerCase() {
		UserExperiment experiment = service.sortition(createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "nEw"));
		MatcherAssert.assertThat(experiment.experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("NEW"));
	}

	@Test
	public void randomWithVariation() {
		UserExperiment experiment = service.sortition(createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"));
		MatcherAssert.assertThat(experiment.experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiment.variation.name,
				Matchers.anyOf(Matchers.equalTo("NEW"), Matchers.equalTo("OLD")));
	}

	@Test
	public void randomWithSomeUsersSimpleVariation() {
		UserExperiment experiment = service.sortition(createExperiment("DASHBOARD", new BigDecimal(50D), "OLD"));
		MatcherAssert.assertThat(experiment.variation.name,
				Matchers.anyOf(Matchers.equalTo("OLD"), Matchers.nullValue()));
	}

	@Test
	public void randomWithSomeUsersVariation() {
		UserExperiment experiment = service.sortition(createExperiment("DASHBOARD", new BigDecimal(50D), "NEW", "OLD"));
		MatcherAssert.assertThat(experiment.variation.name,
				Matchers.anyOf(Matchers.equalTo("NEW"), Matchers.equalTo("OLD"), Matchers.nullValue()));
	}

	@Test
	public void randomWithSomeUsersWithoutVariation() {
		UserExperiment experiment = service.sortition(createExperiment("DASHBOARD", new BigDecimal(50D)));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithMinLimit() {
		UserExperiment experiment = service
				.sortition(createExperiment("DASHBOARD", new BigDecimal(0.00000000000001D), "OLD"));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithMaxLimit() {
		UserExperiment experiment = service
				.sortition(createExperiment("DASHBOARD", new BigDecimal(99.99999999999999D), "OLD"));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("OLD"));
	}

}
