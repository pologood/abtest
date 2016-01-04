package com.softexpert.persistence;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@SequenceGenerator(name = "seq_feature", initialValue = 1)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class Feature {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_feature")
	public Long id;

	@Size(min = 1)
	public String name;

	public String description;

	public String domainList;

	public String groupList;

	public String userList;
	
	public Boolean enabled;

	public BigDecimal percentage;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "FEATURE_ID")
	public List<ABTest> tests;

	public Feature(Long id, String name, Boolean enabled, BigDecimal percentage) {
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.percentage = percentage;
	}

}
