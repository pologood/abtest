package com.softexpert.business.sortition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;

import com.google.common.base.Strings;
import com.softexpert.persistence.Experiment;
import com.softexpert.persistence.User;

@Stateless
public class ElegibleSortitionService {

	private List<String> users;
	private List<String> domains;
	private List<String> groups;

	public boolean isElegible(User user, Experiment experiment) {
		build(experiment.users, experiment.domains, experiment.groups);
		if (isElegibleWithoutCondition())
			return true;
		return isElegibleWithCondition(user);
	}

	private void build(String users, String domains, String groups) {
		this.users = toList(users);
		this.domains = toList(domains);
		this.groups = toList(groups);
	}

	private boolean isElegibleWithCondition(User user) {
		return isRandomWithGroupCondition(user) || isRandoWithUserCondition(user) || isRandomWithDomainCondition(user);
	}

	private boolean isElegibleWithoutCondition() {
		return domains.isEmpty() && groups.isEmpty() && users.isEmpty();
	}

	private List<String> toList(String value) {
		if (Strings.isNullOrEmpty(value))
			return Collections.emptyList();
		return Arrays.asList(value.trim().split(";"));
	}

	private boolean isRandomWithDomainCondition(User user) {
		return !domains.isEmpty() && domains.contains(user.host) && groups.isEmpty() && users.isEmpty();
	}

	private boolean isRandoWithUserCondition(User user) {
		return !users.isEmpty() && (users.contains(user.login) && !domains.isEmpty() && domains.contains(user.host));
	}

	private boolean isRandomWithGroupCondition(User user) {
		return !groups.isEmpty() && isSortionPerGroup(user);
	}

	private boolean isSortionPerGroup(User user) {
		return groups.contains(user.department) && !domains.isEmpty() && domains.contains(user.host);
	}

}
