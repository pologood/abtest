package com.softexpert.business;

import static com.softexpert.business.ErrorMessages.COULD_NOT_EDIT_ERROR;
import static com.softexpert.business.ErrorMessages.SAVE_ERROR;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.business.exception.AppException;
import com.softexpert.repository.DefaultRepository;

@Stateless
public class PersistenceService<T> {

	@Inject
	private DefaultRepository<T> repository;
	@Inject
	private ValidationService validationService;

	public T create(T entity) throws AppException {
		validationService.validate(entity);
		try {
			return repository.save(entity);
		} catch (Exception e) {
			throw new AppException(SAVE_ERROR, e);
		}
	}

	public T edit(T entity) throws AppException {
		validationService.validate(entity);
		try {
			return repository.edit(entity);
		} catch (Exception e) {
			throw new AppException(COULD_NOT_EDIT_ERROR, e);
		}
	}

}
