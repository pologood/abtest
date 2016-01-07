package com.softexpert.business;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.softexpert.business.exception.AppException;

@Stateless
public class ValidationService {

	@Inject
	private Validator validator;
	
	public <T> void  validation(T t) throws AppException {
		Set<ConstraintViolation<T>> violations = validator.validate(t);
		if (!violations.isEmpty())
			throw new AppException(violations.iterator().next().getMessage());
	}
}
