package com.softexpert.repository.experiment;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.softexpert.persistence.Variation;
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.QVariation;

public class VariationRepository {

	@Inject
	private EntityManager entityManager;
	
	public List<Variation> list(Predicate predicate, ConstructorExpression<Variation> constructor){
		return new JPAQuery<Variation>(entityManager)
			.from(QVariation.variation)
			.select(constructor)
			.where(predicate)
			.fetch();
	}
}
