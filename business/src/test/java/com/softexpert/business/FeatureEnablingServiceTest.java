package com.softexpert.business;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.softexpert.business.exception.FeatureEnablingException;
import com.softexpert.persistence.QFeature;
import com.softexpert.repository.FeatureRepository;

public class FeatureEnablingServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private FeatureEnablingService service;
	@Mock
	private FeatureRepository repository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void enableFeature()  throws FeatureEnablingException{
		Mockito.when(repository.toogleState(true, createPredicate(ID, false))).thenReturn(1L);
		service.enabling(ID, true);
		Mockito.verify(repository).toogleState(true, createPredicate(ID, false));
	}
	
	@Test
	public void disbleFeature()  throws FeatureEnablingException{
		Mockito.when(repository.toogleState(false, createPredicate(ID, true))).thenReturn(1L);
		service.enabling(ID, false);
		Mockito.verify(repository).toogleState(false, createPredicate(ID, true));
	}
	
	@Test(expected=FeatureEnablingException.class)
	public void enableFeatureWhenItEnabled()  throws FeatureEnablingException{
		Mockito.when(repository.toogleState(true, createPredicate(ID, false))).thenReturn(0L);
		service.enabling(ID, true);
	}
	
	@Test(expected=FeatureEnablingException.class)
	public void disableFeatureWhenItDisabled()  throws FeatureEnablingException{
		Mockito.when(repository.toogleState(false, createPredicate(ID, true))).thenReturn(0L);
		service.enabling(ID, false);
	}


	private BooleanExpression createPredicate(long id, boolean state) {
		return QFeature.feature.id.eq(id).and(QFeature.feature.enabled.eq(state));
	}

}
