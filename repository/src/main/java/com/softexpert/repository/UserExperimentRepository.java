package com.softexpert.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.QUser;
import com.softexpert.persistence.QUserExperiment;
import com.softexpert.persistence.QVariation;
import com.softexpert.persistence.UserExperiment;

@Stateless
public class UserExperimentRepository {

	@Inject
	private EntityManager entityManager;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UserExperiment> getUserExperiments(Expression<UserExperiment> select,Predicate predicate) {
		QUser user = QUser.user;
		QUserExperiment userexperiment = QUserExperiment.userExperiment;
		QVariation variation = QVariation.variation;
		QExperiment experiment = QExperiment.experiment;
		return new JPAQuery<UserExperiment>(entityManager, HQLTemplates.DEFAULT).from(userexperiment)
				.join(userexperiment.user, user).join(userexperiment.variation, variation)
				.join(userexperiment.experiment, experiment)
				.select(select)
				.where(predicate)
				.fetch();
	}

}
