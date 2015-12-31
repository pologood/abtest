package com.softexpert.business;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.common.base.Strings;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.softexpert.persistence.QFeature;
import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.Feature;
import com.softexpert.repository.DefaultRepository;

@Stateless
public class FeatureService {

	private static final String DELETE_ERROR = "Ops... registro não pode ser removido ou está send utilizado.";
	private static final String COULD_NOT_EDIT_ERROR = "Ops... registro não pode ser editado, verifique se todos os dados então preenchidos corretamente.";
	private static final String SAVE_ERROR = "Ops... erro ao salvar, verifique se todos os dados então preenchidos corretamente :(";
	private static final String SEARCH_ERROR = "Ops... ocorreu erro ao buscas, registro pode não existir mais.";

	@Inject
	protected DefaultRepository<Feature> repository;
	@Inject
	private Validator validator;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Feature find(Long id) throws AppException {
		try {
			return repository.findById(id, Feature.class);
		} catch (Exception e) {
			throw new AppException(SEARCH_ERROR);
		}
	}

	public Feature create(Feature entity) throws AppException {
		validation(entity);
		try {
			return repository.save(entity);
		} catch (Exception e) {
			throw new AppException(SAVE_ERROR);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Feature> list(String schearch) {
		if (Strings.isNullOrEmpty(schearch))
			return repository.all(QFeature.feature, QFeature.feature);
		return repository.list(QFeature.feature, QFeature.feature.name.containsIgnoreCase(schearch), QFeature.feature);
	}

	public Feature edit(Feature entity) throws AppException {
		validation(entity);
		try {
			return repository.edit(entity);
		} catch (Exception e) {
			throw new AppException(COULD_NOT_EDIT_ERROR);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void delete(Long id) throws AppException {
		try {
			repository.delete(find(id));
		} catch (Exception e) {
			throw new AppException(DELETE_ERROR);
		}
	}

	private void validation(Feature entity) throws AppException {
		Set<ConstraintViolation<Feature>> violations = validator.validate(entity);
		if (!violations.isEmpty())
			throw new AppException(violations.iterator().next().getMessage());
	}

}
