package com.softexpert.resource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.Variation;

@RunWith(Arquillian.class)
public class ExperimentResourceIT extends IntegrationTest{

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
		List<Variation> tests = Arrays.asList(Variation.builder().name("A").build(),
				Variation.builder().name("B").build());
		Experiment entity = find(post(name, true, tests).id);
		MatcherAssert.assertThat(entity.variations, Matchers.hasSize(2));
	}

	@Test
	public void list() throws Exception {
		String name = "new name " + System.currentTimeMillis();
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

	@Test
	public void randomTest() throws Exception {
		Experiment experiment = Experiment.builder().name("DEFAULT_FRAME").enabled(true)
				.percentage(new BigDecimal(100D)).variations(Arrays.asList(Variation.builder().name("NEW").build()))
				.domains("www.demobr.com;www.softexpert.com").groups("TEC;DES").build();
		post(experiment);
		String experiments = random(UserDTO.builder().name("Alisson Medeiros").login("alisson.muller")
				.host("www.softexpert.com").department("TEC").build());
		MatcherAssert.assertThat(experiments, Matchers.equalTo("[{\"name\":\"DEFAULT_FRAME\",\"variationName\":\"NEW\"}]"));
	}

	private Experiment post(String name, boolean enabled, List<Variation> tests) {
		return post(Experiment.builder().name(name).enabled(enabled).percentage(BigDecimal.TEN).variations(tests)
				.users("A,B").build());
	}

	private Experiment post(Experiment entity) {
		return target.path("/v1/experiments").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Experiment.class);
	}

	private String random(UserDTO user) {
		return target.path("/v1/public/users-experiments").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE), String.class);
	}

	private List<Experiment> list(String search) {
		return target.path("/v1/experiments").queryParam("search", search).request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(List.class);
	}

	private Experiment find(long id) {
		return target.path("/v1/experiments/" + id).request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(Experiment.class);
	}

	private int enabling(long id, boolean state) {
		return target.path("/v1/experiments/" + id + "/enabling").request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(state, MediaType.APPLICATION_JSON_TYPE)).getStatus();
	}

}
