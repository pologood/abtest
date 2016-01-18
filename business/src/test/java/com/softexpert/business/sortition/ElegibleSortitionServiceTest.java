package com.softexpert.business.sortition;

import static com.softexpert.business.experiment.ExperimentTestBuilder.createExperiment;

import java.math.BigDecimal;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;

public class ElegibleSortitionServiceTest {

	@InjectMocks
	private ElegibleSortitionService service;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void randomWithSingleVariation() {
		boolean isElegible = service.isElegible(createUser(),
				createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"));
		MatcherAssert.assertThat(isElegible, Matchers.is(true));
	}

	@Test
	public void randomWithLowerCase() {
		boolean isElegible = service.isElegible(createUser(),
				createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "nEw"));
		MatcherAssert.assertThat(isElegible, Matchers.is(true));
	}

	@Test
	public void randomWithVariation() {
		boolean isElegible = service.isElegible(createUser(),
				createExperiment("DEFAULT_FRAME", new BigDecimal(100D), "NEW"));
		MatcherAssert.assertThat(isElegible, Matchers.is(true));

	}

	@Test
	public void randomWithoutVariation() {
		boolean isElegible = service.isElegible(createUser(), createExperiment("DASHBOARD", new BigDecimal(50D)));
		MatcherAssert.assertThat(isElegible, Matchers.is(true));
	}

	@Test
	public void randomWithDomain() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.domains = "www.demobr.com;www.softexpert.com";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(true));

	}

	@Test
	public void randomWithDomainNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.domains = "www.demobr.com";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(false));
	}

	@Test
	public void randomWithGroupAndDomain() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.groups = "DES;Tec";
		dashboard.domains = "www.demobr.com;www.softexpert.com";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(true));
	}

	@Test
	public void randomWithGroup() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.groups = "Tec";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(false));
	}

	@Test
	public void randomWithGropsNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.groups = "DES";
		dashboard.domains = "www.softexpert.com";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(false));
	}

	@Test
	public void randomWithGroupAlowAndDomainNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.groups = "Tec";
		dashboard.domains = "www.demobr.com";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(false));
	}

	@Test
	public void randomWithAlowUserAndDomain() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "pedrojoaqeuim@se.com;joao.silva";
		dashboard.domains = "www.softexpert.com";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(true));
	}

	@Test
	public void randomWithUserAlowUser() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "joao.silva";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(false));
	}

	@Test
	public void randomWithUserWithoutDomain() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "maria.souza";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(false));
	}

	@Test
	public void randomWithUserNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "maria.souza";
		dashboard.domains = "www.softexpert.com";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(false));
	}

	@Test
	public void randomWithUserAlowAndDomainNotAlow() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "joao.silva";
		dashboard.domains = "www.demobr.com;www.tec.com";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(false));
	}

	@Test
	public void randomWithEmptyLists() {
		Experiment dashboard = createExperiment("DASHBOARD", new BigDecimal(100D), "OLD");
		dashboard.users = "";
		dashboard.domains = "";
		boolean isElegible = service.isElegible(createUser(), dashboard);
		MatcherAssert.assertThat(isElegible, Matchers.is(true));
	}

	private User createUser() {
		return User.builder().code("1").name("João Silva").login("joao.silva").department("Tec")
				.host("www.softexpert.com").build();
	}
}
