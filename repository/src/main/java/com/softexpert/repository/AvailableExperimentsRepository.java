package com.softexpert.repository;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.QVariation;
import com.softexpert.persistence.Variation;

public class AvailableExperimentsRepository {

	@Inject
	private EntityManager entityManager;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Variation> list(Predicate predicate, Expression<Variation> select){
		return new JPAQuery<Variation>(entityManager)
			.from(QVariation.variation)
			.join(QVariation.variation.experiment, QExperiment.experiment)
			.select(select)
			.where(predicate)
			.fetch();
	}
	
}
