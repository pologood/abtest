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
public class ExperimentPersistenceService {

	@Inject
	private DefaultRepository<Experiment> repository;
	@Inject
	private Validator validator;
	
	public Experiment create(Experiment entity) throws AppException {
		validation(entity);
		try {
			return repository.save(entity);
		} catch (Exception e) {
			throw new AppException(SAVE_ERROR);
		}
	}
	
	public Experiment edit(Experiment entity) throws AppException {
		validation(entity);
		try {
			return repository.edit(entity);
		} catch (Exception e) {
			throw new AppException(COULD_NOT_EDIT_ERROR);
		}
	}
	
	public void delete(Long id) throws AppException {
		try {
			repository.delete(QExperiment.experiment, QExperiment.experiment.id.eq(id));
		} catch (Exception e) {
			throw new AppException(DELETE_ERROR);
		}
	}
	
	private void validation(Experiment entity) throws AppException {
		Set<ConstraintViolation<Experiment>> violations = validator.validate(entity);
		if (!violations.isEmpty())
			throw new AppException(violations.iterator().next().getMessage());
	}
}
