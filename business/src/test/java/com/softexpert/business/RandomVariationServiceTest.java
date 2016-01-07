package com.softexpert.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.softexpert.dto.ExperimentDTO;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.Variation;

public class RandomVariationServiceTest {

	@InjectMocks
	private RandomVariationService service;
	@Mock
	private AvailableExperimentsService availableExperimentsService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void randomWithAllUsersWithVariation() {
		mock(createSimpleExperiments());
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.nullValue());
	}
	
	@Test
	public void randomWithAllUsersSimpleExperiment() {
		mock(createSimpleExperiments("NEW"));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.equalTo("NEW"));
	}

	@Test
	public void randomWithAllUsersSimpleVariation() {
		mock(createSimpleExperiments("NEW", "OLD"));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments.get(0).name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiments.get(0).variationName,
				Matchers.anyOf(Matchers.equalTo("NEW"), Matchers.equalTo("OLD")));
	}

	@Test
	public void randomWithSomeUsersSimpleVariation() {
		mock(Arrays.asList(createExperiment("DASHBOARD", new BigDecimal(50D), "OLD")));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(1));
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.anyOf(Matchers.equalTo("OLD"), Matchers.nullValue()));
	}
	
	@Test
	public void randomWithSomeUsersVariation() {
		mock(Arrays.asList(createExperiment("DASHBOARD", new BigDecimal(50D), "NEW", "OLD")));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.anyOf(Matchers.equalTo("NEW"), Matchers.equalTo("OLD"), Matchers.nullValue()));
	}
	@Test
	public void randomWithSomeUsersWithoutVariation() {
		mock(Arrays.asList(createExperiment("DASHBOARD", new BigDecimal(50D))));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments.get(0).variationName,  Matchers.nullValue());
	}
	
	@Test
	public void randomWithExperiments() {
		mock(Arrays.asList(createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"),
				createExperiment("DASHBOARD", new BigDecimal(50D), "NEW", "OLD"),
				createExperiment("CHATS", new BigDecimal(0D)),
				createExperiment("PORTAL", new BigDecimal(0D),"NEW", "DEFAULT")));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments, Matchers.hasSize(4));
	}
	
	@Test
	public void randomWithMinLimit() {
		mock(Arrays.asList(createExperiment("DASHBOARD", new BigDecimal(0.00000000000001D), "OLD")));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.nullValue());
	}

	@Test
	public void randomWithMaxLimit() {
		mock(Arrays.asList(createExperiment("DASHBOARD", new BigDecimal(99.99999999999999D), "OLD")));
		List<ExperimentDTO> experiments = service.random(createUser());
		MatcherAssert.assertThat(experiments.get(0).variationName, Matchers.equalTo("OLD"));
	}
	
	private UserDTO createUser() {
		return UserDTO.builder()
				.code("1")
				.name("João Silva")
				.login("joao.silva")
				.group("Tec")
				.host("www.softexpert.com")
				.build();
	}

	private void mock(List<Experiment> experiments) {
		Mockito.when(availableExperimentsService.getAvailableExperiments()).thenReturn(experiments);
	}

	private List<Experiment> createSimpleExperiments(String... variation) {
		return Arrays.asList(createExperiment("DEFAULT_FRAME", new BigDecimal(100D), variation));
	}

	private Experiment createExperiment(String name, BigDecimal percentage, String... variation) {
		return Experiment.builder().name(name).percentage(percentage).variations(createVariations(variation)).build();
	}

	private List<Variation> createVariations(String[] names) {
		List<Variation> variations = new ArrayList<>();
		for (String name : names)
			variations.add(createVariation(name));
		return variations;
	}

	private Variation createVariation(String name) {
		return Variation.builder().name(name).build();
	}
}