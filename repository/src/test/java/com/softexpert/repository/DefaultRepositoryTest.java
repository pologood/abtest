package com.softexpert.repository;

import static com.softexpert.persistence.QExperiment.experiment;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.softexpert.persistence.Experiment;
import com.softexpert.repository.DefaultRepository;

public class DefaultRepositoryTest {

	private static final long DUMMY_ID = 1L;
	@Spy
	@InjectMocks
	private DefaultRepository<Experiment> repository;
	@Mock
	private EntityManager entityManager;
	@Mock
	private JPAQuery<Experiment> jpaQuery;

	@Before
	public void init() {
		initMocks(this);
		Mockito.when(repository.createQuery()).thenReturn(jpaQuery);
	}

	@Test
	public void save() {
		Experiment entity = createSample();
		entity = repository.save(entity);
		Mockito.verify(entityManager).persist(entity);
		Mockito.verify(entityManager, Mockito.never()).merge(entity);
	}

	@Test
	public void edit() {
		Experiment entity = createSample();
		repository.edit(entity);
		Mockito.verify(entityManager, Mockito.never()).persist(entity);
		Mockito.verify(entityManager).merge(entity);
	}

	@Test
	public void list() {
		BooleanExpression expression = experiment.id.eq(DUMMY_ID);
		Mockito.when(jpaQuery.select(experiment)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.from(experiment)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.where(expression)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.fetch()).thenReturn(Arrays.asList(createSample(), createSample()));
		List<Experiment> list = repository.list(experiment, expression, experiment);
		MatcherAssert.assertThat(list, Matchers.hasSize(2));
		Mockito.verify(jpaQuery).select(experiment);
		Mockito.verify(jpaQuery).from(experiment);
		Mockito.verify(jpaQuery).where(expression);
		Mockito.verify(jpaQuery).fetch();
	}

	@Test
	public void all() {
		Mockito.when(jpaQuery.select(experiment)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.from(experiment)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.fetch()).thenReturn(Arrays.asList(createSample(), createSample()));
		List<Experiment> list = repository.all(experiment, experiment);
		MatcherAssert.assertThat(list, Matchers.hasSize(2));
		Mockito.verify(jpaQuery).select(experiment);
		Mockito.verify(jpaQuery).from(experiment);
		Mockito.verify(jpaQuery).fetch();
	}

	private Experiment createSample() {
		return Experiment.builder().id(DUMMY_ID).build();
	}
}
