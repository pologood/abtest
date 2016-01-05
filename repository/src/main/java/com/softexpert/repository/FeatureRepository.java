package com.softexpert.repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.softexpert.persistence.ABTest;
import com.softexpert.persistence.Feature;
import com.softexpert.persistence.QABTest;
import com.softexpert.persistence.QFeature;

public class FeatureRepository {
	
	@Inject
	private EntityManager entityManager;
	
	@Transactional
	public long toogleState(Boolean state, Predicate predicate){
		return new JPAUpdateClause(entityManager, QFeature.feature)
				.set(QFeature.feature.enabled, state)
				.where(predicate)
				.execute();
	}
	
	public Feature findById(Long id, ConstructorExpression<Feature> constructor){
		return new JPAQuery<Feature>(entityManager).from(QFeature.feature)
			.leftJoin(QFeature.feature.tests, QABTest.aBTest)
			.where(QFeature.feature.id.eq(id))
			.select(constructor)
			.fetchFirst();
	}

}
