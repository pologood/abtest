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

	@Test
	public void getSamples() throws Exception {
		Client client = ClientBuilder.newClient();
		target = client.target(base);
		List<Feature> response = list("dasdasdadsa");
		MatcherAssert.assertThat(response, Matchers.hasSize(0));
	}

	@Test
	public void post() throws Exception {
		Client client = ClientBuilder.newClient();
		target = client.target(base);
		Feature entity = create("name");
		Feature response = post(entity);
		MatcherAssert.assertThat(response.id, Matchers.notNullValue());
		MatcherAssert.assertThat(response.name, Matchers.equalTo(entity.name));
	}

	@Test
	public void list() throws Exception {
		Client client = ClientBuilder.newClient();
		target = client.target(base);
		Feature entity = create("new name");
		post(entity);
		List<Feature> list = list(entity.name);
		MatcherAssert.assertThat(list, Matchers.hasSize(1));
	}

	private Feature post(Feature entity) {
		return target.path("/v1/features").request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE), Feature.class);
	}

	private List<Feature> list(String search) {
		return target.path("/v1/features").queryParam("search", search).request(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(List.class);
	}

	private Feature create(String name) {
		return Feature.builder().name(name).build();
	}

}
