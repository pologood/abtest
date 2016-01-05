package com.softexpert.business;

import static com.softexpert.business.ErrorMessages.SEARCH_ERROR;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.common.base.Strings;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.softexpert.persistence.QFeature;
import com.softexpert.business.exception.AppException;
import com.softexpert.persistence.ABTest;
import com.softexpert.persistence.Feature;
import com.softexpert.persistence.QABTest;
import com.softexpert.repository.ABTestRepository;
import com.softexpert.repository.DefaultRepository;
import com.softexpert.repository.FeatureRepository;

@Stateless
public class FeatureLoadService {

	@Inject
	private DefaultRepository<Feature> repository;
	@Inject
	private FeatureRepository featureRepository;
	@Inject
	private ABTestRepository abTestRepository;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Feature find(Long id) throws AppException {
		try {
			Feature feature = featureRepository.findById(id, createConstructiorExpression());
			feature.tests = abTestRepository.listFromFeature(feature.id, createABTestExpressionConstructor());
			return feature;
		} catch (Exception e) {
			throw new AppException(SEARCH_ERROR);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Feature> list(String schearch) {
		if (Strings.isNullOrEmpty(schearch))
			return repository.all(QFeature.feature, createConstructiorExpression());
		return repository.list(QFeature.feature, QFeature.feature.name.containsIgnoreCase(schearch), createConstructiorExpression());
	}

	private ConstructorExpression<ABTest> createABTestExpressionConstructor() {
		return Projections.constructor(ABTest.class, QABTest.aBTest.id,QABTest.aBTest.name);
	}
	
	private  ConstructorExpression<Feature>  createConstructiorExpression(){
		 return Projections.constructor(Feature.class,QFeature.feature.id, QFeature.feature.name, QFeature.feature.enabled, QFeature.feature.percentage);
	}
	
	
}
