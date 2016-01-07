package com.softexpert.business;

import static com.softexpert.business.ErrorMessages.COULD_NOT_EDIT_ERROR;
import static com.softexpert.business.ErrorMessages.DELETE_ERROR;
import static com.softexpert.business.ErrorMessages.SAVE_ERROR;

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.QExperiment;
import com.softexpert.repository.DefaultRepository;

@Stateless
public class PersistenceService<T> {

	@Inject
	private DefaultRepository<T> repository;
	@Inject
	private ValidationService validationService;
	
	public T create(T entity) throws AppException {
		validationService.validation(entity);
		try {
			return repository.save(entity);
		} catch (Exception e) {
			throw new AppException(SAVE_ERROR, e);
		}
	}
	
	public T edit(T entity) throws AppException {
		validationService.validation(entity);
		try {
			return repository.edit(entity);
		} catch (Exception e) {
			throw new AppException(COULD_NOT_EDIT_ERROR, e);
		}
	}
	
}
