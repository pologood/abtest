package com.softexpert.repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.softexpert.persistence.QFeature;

public class FeatureRepository {
	
	@Inject
	private EntityManager entityManager;
	
	public long toogleState(Boolean state, Predicate predicate){
		return new JPAUpdateClause(entityManager, QFeature.feature)
				.set(QFeature.feature.enabled, state)
				.where(predicate)
				.execute();
	}

}
