package com.softexpert.dto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Strings;
import com.softexpert.persistence.Variation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@NoArgsConstructor
public class ExperimentDTO {

	public Long id;
	public String name;
	public String description;
	public List<String> domains;
	public List<String> groups;
	public List<String> users;
	public Boolean enabled;
	public BigDecimal percentage;
	public List<Variation> variations;

	public ExperimentDTO(Long id, String name, String description, String domains, String groups, String users,
			Boolean enabled, BigDecimal percentage) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.domains = toList(domains);
		this.groups = toList(groups);
		this.users = toList(users);
		this.enabled = enabled;
		this.percentage = percentage;
	}

	private List<String> toList(String value) {
		if (Strings.isNullOrEmpty(value))
			return Collections.emptyList();
		return Arrays.asList(value.trim().split(";"));
	}

}
