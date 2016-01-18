package com.softexpert.repository.experiment;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.QExperiment;

public class ExperimentRepository {
	
	@Inject
	private EntityManager entityManager;
	
	@Transactional
	public long toogleState(Boolean state, Predicate predicate){
		return new JPAUpdateClause(entityManager, QExperiment.experiment)
			.set( QExperiment.experiment.enabled, state)
			.where(predicate)
			.execute();
	}
	
	public Experiment findById(Predicate predicate, ConstructorExpression<Experiment> constructor){
		return new JPAQuery<Experiment>(entityManager).from(QExperiment.experiment)
			.where(predicate)
			.select(constructor)
			.fetchFirst();
}
	
}
