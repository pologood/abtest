package com.softexpert.resource;

import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;

public class IntegrationTest {

	@ArquillianResource
	private URI base;

	protected WebTarget target;

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
}
