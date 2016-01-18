package com.softexpert.business.experiment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.softexpert.business.exception.ExperimentEnablingException;
import com.softexpert.business.experiment.ExperimentEnablingService;
import com.softexpert.persistence.QExperiment;
import com.softexpert.repository.experiment.ExperimentRepository;

public class ExperimentEnablingServiceTest {

	private static final long ID = 1L;
	@InjectMocks
	private ExperimentEnablingService service;
	@Mock
	private ExperimentRepository repository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void enableFeature()  throws ExperimentEnablingException{
		Mockito.when(repository.toogleState(true, createPredicate(ID, false))).thenReturn(1L);
		service.enabling(ID, true);
		Mockito.verify(repository).toogleState(true, createPredicate(ID, false));
	}
	
	@Test
	public void disbleFeature()  throws ExperimentEnablingException{
		Mockito.when(repository.toogleState(false, createPredicate(ID, true))).thenReturn(1L);
		service.enabling(ID, false);
		Mockito.verify(repository).toogleState(false, createPredicate(ID, true));
	}
	
	@Test(expected=ExperimentEnablingException.class)
	public void enableFeatureWhenItEnabled()  throws ExperimentEnablingException{
		Mockito.when(repository.toogleState(true, createPredicate(ID, false))).thenReturn(0L);
		service.enabling(ID, true);
	}
	
	@Test(expected=ExperimentEnablingException.class)
	public void disableFeatureWhenItDisabled()  throws ExperimentEnablingException{
		Mockito.when(repository.toogleState(false, createPredicate(ID, true))).thenReturn(0L);
		service.enabling(ID, false);
	}


	private BooleanExpression createPredicate(long id, boolean state) {
		return QExperiment.experiment.id.eq(id).andAnyOf(QExperiment.experiment.enabled.eq(state).or(QExperiment.experiment.enabled.isNull()));
	}

}
