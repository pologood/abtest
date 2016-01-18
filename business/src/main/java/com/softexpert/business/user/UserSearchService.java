package com.softexpert.business.user;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.QExperiment;
import com.softexpert.persistence.QUser;
import com.softexpert.persistence.QUserExperiment;
import com.softexpert.persistence.QVariation;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.repository.user.UserExperimentRepository;

@Stateless
public class UserSearchService {

	@Inject
	private UserExperimentRepository repository;

	public User search(UserDTO userDTO) {
		List<UserExperiment> userExperiments = repository.getUserExperiments(constructor(), condition(userDTO));
		if (userExperiments.isEmpty())
			return null;
		User user = userExperiments.get(0).user;
		user.experiments = userExperiments;
		return user;
	}

	private BooleanExpression condition(UserDTO userDTO) {
		return QUser.user.login.eq(userDTO.login).and(QUser.user.host.eq(userDTO.host));
	}

	private ConstructorExpression<UserExperiment> constructor() {
		return Projections.constructor(UserExperiment.class, QUserExperiment.userExperiment.id, QUser.user,
				QVariation.variation, QExperiment.experiment);
	}
}
