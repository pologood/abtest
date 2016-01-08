package com.softexpert.business;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.Experiment;

public class ValidationServiceTest {

	@InjectMocks
	private ValidationService service;
	@Mock
	private Validator validator;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = AppException.class)
	public void validate() throws AppException {
		Experiment sample = Experiment.builder().id(1L).name("A").build();
		Set<ConstraintViolation<Experiment>> violations = new HashSet<>();
		ConstraintViolation constraintViolation = Mockito.mock(ConstraintViolation.class);
		violations.add(constraintViolation);
		Mockito.when(constraintViolation.getMessage()).thenReturn("Error");
		Mockito.when(validator.validate(sample)).thenReturn(violations);
		service.validate(sample);
	}

}
