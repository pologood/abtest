package com.softexpert.business;

import static com.softexpert.business.ExperimentTestBuilder.createExperiment;

import java.math.BigDecimal;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;

public class RandomUserExperimentServiceTest {

	@InjectMocks
	private RandomUserExperimentService service;
	@Spy
	private ExperimentRandomService experimentRandomService;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void randomWithSingleVariation() {
		UserExperiment experiment = service.random(createUser(),
				createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"));
		MatcherAssert.assertThat(experiment.experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("NEW"));
	}

	@Test
	public void randomWithLowerCase() {
		UserExperiment experiment = service.random(createUser(),
				createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "nEw"));
		MatcherAssert.assertThat(experiment.experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("NEW"));
	}

	@Test
	public void randomWithVariation() {
		UserExperiment experiment = service.random(createUser(),
				createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"));
		MatcherAssert.assertThat(experiment.experiment.name, Matchers.equalTo("DEFAULT_FRAME"));
		MatcherAssert.assertThat(experiment.variation.name,
				Matchers.anyOf(Matchers.equalTo("NEW"), Matchers.equalTo("OLD")));
	}

	@Test
	public void randomWithSomeUsersSimpleVariation() {
		UserExperiment experiment = service.random(createUser(),
				createExperiment("DASHBOARD", new BigDecimal(50D), "OLD"));
		MatcherAssert.assertThat(experiment.variation.name,
				Matchers.anyOf(Matchers.equalTo("OLD"), Matchers.nullValue()));
	}

	@Test
	public void randomWithSomeUsersVariation() {
		UserExperiment experiment = service.random(createUser(),
				createExperiment("DASHBOARD", new BigDecimal(50D), "NEW", "OLD"));
		MatcherAssert.assertThat(experiment.variation.name,
				Matchers.anyOf(Matchers.equalTo("NEW"), Matchers.equalTo("OLD"), Matchers.nullValue()));
	}

	@Test
	public void randomWithSomeUsersWithoutVariation() {
		UserExperiment experiment = service.random(createUser(), createExperiment("DASHBOARD", new BigDecimal(50D)));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithMinLimit() {
		UserExperiment experiment = service.random(createUser(),
				createExperiment("DASHBOARD", new BigDecimal(0.00000000000001D), "OLD"));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithMaxLimit() {
		UserExperiment experiment = service.random(createUser(),
				createExperiment("DASHBOARD", new BigDecimal(99.99999999999999D), "OLD"));
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("OLD"));
	}

	@Test
	public void randomWithDomain() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.domains = "www.demobr.com;www.softexpert.com";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("OLD"));
	}

	@Test
	public void randomWithDomainNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.domains = "www.demobr.com";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithGroupAndDomain() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.groups = "DES;Tec";
		dashboard.domains = "www.demobr.com;www.softexpert.com";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("OLD"));
	}

	@Test
	public void randomWithGroup() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.groups = "Tec";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithGropsNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.groups = "DES";
		dashboard.domains = "www.softexpert.com";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithGroupAlowAndDomainNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.groups = "Tec";
		dashboard.domains = "www.demobr.com";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithAlowUserAndDomain() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "pedrojoaqeuim@se.com;joao.silva";
		dashboard.domains = "www.softexpert.com";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("OLD"));
	}

	@Test
	public void randomWithUserAlowUser() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "joao.silva";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithUserWithoutDomain() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "maria.souza";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithUserNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "maria.souza";
		dashboard.domains = "www.softexpert.com";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithUserAlowAndDomainNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "joao.silva";
		dashboard.domains = "www.demobr.com;www.tec.com";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.nullValue());
	}

	@Test
	public void randomWithEmptyLists() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "";
		dashboard.domains = "";
		UserExperiment experiment = service.random(createUser(), dashboard);
		MatcherAssert.assertThat(experiment.variation.name, Matchers.equalTo("OLD"));
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

}
