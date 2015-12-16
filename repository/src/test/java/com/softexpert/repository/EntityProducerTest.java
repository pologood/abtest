package com.softexpert.repository;

import javax.persistence.EntityManager;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EntityProducerTest {

	@InjectMocks
	private EntityProducer producer;
	@Mock
	private EntityManager entityManager;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getEntityManager() {
		MatcherAssert.assertThat(producer.getEntityManager(), Matchers.notNullValue());
	}
}
