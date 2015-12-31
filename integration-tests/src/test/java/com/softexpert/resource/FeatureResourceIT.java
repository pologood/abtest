package com.softexpert.resource;

import java.io.File;
import java.net.URI;
import java.net.URL;
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

import com.softexpert.persistence.Feature;

@RunWith(Arquillian.class)
public class FeatureResourceIT {

	@ArquillianResource
	private URI base;

	private WebTarget target;

	@Deployment(testable = false)
	public static WebArchive deploy() {
		URL webXML = FeatureResourceIT.class.getResource("web.xml");
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
		List<Feature> response = list("dasdasdadsa");
		MatcherAssert.assertThat(response, Matchers.hasSize(0));
	}

	@Test
	public void post() throws Exception {
		String name = "name";
		Feature entity = post(name, true);
		MatcherAssert.assertThat(entity.id, Matchers.notNullValue());
		MatcherAssert.assertThat(entity.name, Matchers.equalTo(name));
	}

	@Test
	public void list() throws Exception {
		String name = "new name";
		post(name, true);
		List<Feature> list = list(name);
		MatcherAssert.assertThat(list, Matchers.hasSize(1));
	}

	@Test
	public void disable() throws Exception {
		Feature entity = post("disable", true);
		int httpStatus = enabling(entity.id, false);
		MatcherAssert.assertThat(httpStatus, Matchers.equalTo(204));
	}
	
	@Test
	public void disableWithError() throws Exception {
		Feature entity = post("disable2", true);
		enabling(entity.id, false);
		int httpStatus = enabling(entity.id, false);
		MatcherAssert.assertThat(httpStatus, Matchers.equalTo(404));
	}
	
	@Test
	public void enabled() throws Exception {
		Feature entity = post("enabled", false);
		int httpStatus = enabling(entity.id, true);
		MatcherAssert.assertThat(httpStatus, Matchers.equalTo(204));
	}
	
	@Test
	public void enabledWithError() throws Exception {
		Feature entity = post("enabled2", false);
		enabling(entity.id, true);
		int httpStatus = enabling(entity.id, true);
		MatcherAssert.assertThat(httpStatus, Matchers.equalTo(404));
	}

	private Feature post(String name, boolean enabled) {
		Feature entity =  Feature.builder().name(name).enabled(enabled).build();
		return target.path("/v1/features")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Feature.class);
	}

	private List<Feature> list(String search) {
		return target.path("/v1/features").queryParam("search", search)
				.request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(List.class);
	}

	private int enabling(long id, boolean state){
		return target.path("/v1/features/"+id+"/enabling")
			.request(MediaType.APPLICATION_JSON)
			.put(Entity.entity(state, MediaType.APPLICATION_JSON_TYPE))
			.getStatus();
	}

}