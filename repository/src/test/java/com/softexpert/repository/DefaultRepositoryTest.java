package com.softexpert.repository;

import static com.softexpert.persistence.QFeature.feature;
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
import com.softexpert.persistence.Feature;
import com.softexpert.repository.DefaultRepository;

public class DefaultRepositoryTest {

	private static final long DUMMY_ID = 1L;
	@Spy
	@InjectMocks
	private DefaultRepository<Feature> repository;
	@Mock
	private EntityManager entityManager;
	@Mock
	private JPAQuery<Feature> jpaQuery;

	@Before
	public void init() {
		initMocks(this);
		Mockito.when(repository.createQuery()).thenReturn(jpaQuery);
	}

	@Test
	public void findById() {
		Mockito.when(entityManager.find(Feature.class, DUMMY_ID)).thenReturn(createSample());
		Feature sample= repository.findById(1L, Feature.class);
		MatcherAssert.assertThat(sample.id, Matchers.equalTo(DUMMY_ID));
	}

	@Test
	public void save() {
		Feature entity = createSample();
		entity = repository.save(entity);
		Mockito.verify(entityManager).persist(entity);
		Mockito.verify(entityManager, Mockito.never()).merge(entity);
	}

	@Test
	public void edit() {
		Feature entity = createSample();
		repository.edit(entity);
		Mockito.verify(entityManager, Mockito.never()).persist(entity);
		Mockito.verify(entityManager).merge(entity);
	}

	@Test
	public void delete() {
		Feature entity = createSample();
		repository.delete(entity);
		Mockito.verify(entityManager).remove(entity);
	}

	@Test
	public void list() {
		BooleanExpression expression = feature.id.eq(DUMMY_ID);
		Mockito.when(jpaQuery.select(feature)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.from(feature)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.where(expression)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.fetch()).thenReturn(Arrays.asList(createSample(), createSample()));
		List<Feature> list = repository.list(feature, expression, feature);
		MatcherAssert.assertThat(list, Matchers.hasSize(2));
		Mockito.verify(jpaQuery).select(feature);
		Mockito.verify(jpaQuery).from(feature);
		Mockito.verify(jpaQuery).where(expression);
		Mockito.verify(jpaQuery).fetch();
	}

	@Test
	public void all() {
		Mockito.when(jpaQuery.select(feature)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.from(feature)).thenReturn(jpaQuery);
		Mockito.when(jpaQuery.fetch()).thenReturn(Arrays.asList(createSample(), createSample()));
		List<Feature> list = repository.all(feature, feature);
		MatcherAssert.assertThat(list, Matchers.hasSize(2));
		Mockito.verify(jpaQuery).select(feature);
		Mockito.verify(jpaQuery).from(feature);
		Mockito.verify(jpaQuery).fetch();
	}

	private Feature createSample() {
		return Feature.builder().id(DUMMY_ID).build();
	}
}
