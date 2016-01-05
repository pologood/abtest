package com.softexpert.repository;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.softexpert.persistence.ABTest;
import com.softexpert.persistence.QABTest;

public class ABTestRepository {

	@Inject
	private EntityManager entityManager;
	
	public List<ABTest> listFromFeature(Long featureId, ConstructorExpression<ABTest> constructor){
		return new JPAQuery<ABTest>(entityManager)
			.from(QABTest.aBTest)
			.select(constructor)
			.where(QABTest.aBTest.feature.id.eq(featureId))
			.fetch();
	}
}
