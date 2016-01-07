package com.softexpert.business;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.softexpert.dto.UserDTO;
import com.softexpert.persistence.User;
import com.softexpert.persistence.UserExperiment;
import com.softexpert.repository.UserExperimentRepository;

@Stateless
public class UserExperimentService {

	@Inject
	private UserExperimentRepository repository;

	public User getUser(UserDTO userDTO) {
		List<UserExperiment> userExperiments = repository.userExperiments(userDTO);
		if (userExperiments.isEmpty())
			return null;
		User user = userExperiments.get(0).user;
		user.experiments = userExperiments;
		return user;
	}
}
