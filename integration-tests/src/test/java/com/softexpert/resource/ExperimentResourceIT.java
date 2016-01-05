package com.softexpert.resource;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.softexpert.persistence.Variation;
import com.softexpert.persistence.Experiment;

@RunWith(Arquillian.class)
public class ExperimentResourceIT {

	@ArquillianResource
	private URI base;

	private WebTarget target;

	@Deployment(testable = false)
	public static WebArchive deploy() {
		URL webXML = ExperimentResourceIT.class.getResource("web.xml");
		File[] archives = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeDependencies().resolve()
				.withTransitivity().asFile();
		return ShrinkWrap.create(WebArchive.class).setWebXML(webXML).addAsLibraries(archives);
	}

	@Before
	public void init() throws Exception {
		Client client = ClientBuilder.newClient();
		target = client.target(base);
	}

	@Test
	public void getSamples() throws Exception {
		List<Experiment> response = list("dasdasdadsa");
		MatcherAssert.assertThat(response, Matchers.hasSize(0));
	}

	@Test
	public void post() throws Exception {
		String name = "name";
		Experiment entity = post(name, true, null);
		MatcherAssert.assertThat(entity.id, Matchers.notNullValue());
		MatcherAssert.assertThat(entity.name, Matchers.equalTo(name));
	}

	@Test
	public void postTests() throws Exception {
		String name = "name";
		List<Variation> tests = Arrays.asList(Variation.builder().name("A").build(), Variation.builder().name("B").build());
		Experiment entity = find(post(name, true, tests).id);
		MatcherAssert.assertThat(entity.variations, Matchers.hasSize(2));
	}

	@Test
	public void list() throws Exception {
		String name = "new name "+System.currentTimeMillis();
		post(name, true, null);
		List<Experiment> list = list(name);
		MatcherAssert.assertThat(list, Matchers.hasSize(1));
	}

	@Test
	public void disable() throws Exception {
		Experiment entity = post("disable", true, null);
		int httpStatus = enabling(entity.id, false);
		MatcherAssert.assertThat(httpStatus, Matchers.equalTo(204));
	}

	@Test
	public void disableWithError() throws Exception {
		Experiment entity = post("disable2", true, null);
		enabling(entity.id, false);
		int httpStatus = enabling(entity.id, false);
		MatcherAssert.assertThat(httpStatus, Matchers.equalTo(404));
	}

	@Test
	public void enabled() throws Exception {
		Experiment entity = post("enabled", false, null);
		int httpStatus = enabling(entity.id, true);
		MatcherAssert.assertThat(httpStatus, Matchers.equalTo(204));
	}

	@Test
	public void enabledWithError() throws Exception {
		Experiment entity = post("enabled2", false, null);
		enabling(entity.id, true);
		int httpStatus = enabling(entity.id, true);
		MatcherAssert.assertThat(httpStatus, Matchers.equalTo(404));
	}

	private Experiment post(String name, boolean enabled, List<Variation> tests) {
		Experiment entity = Experiment.builder().name(name).enabled(enabled).percentage(BigDecimal.TEN).variations(tests).build();
		return target.path("/v1/experiments")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Experiment.class);
	}

	private List<Experiment> list(String search) {
		return target.path("/v1/experiments")
				.queryParam("search", search)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(List.class);
	}

	private Experiment find(long id) {
		return target.path("/v1/experiments/" + id)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(Experiment.class);
	}

	private int enabling(long id, boolean state) {
		return target.path("/v1/experiments/" + id + "/enabling")
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(state, MediaType.APPLICATION_JSON_TYPE))
				.getStatus();
	}

}
