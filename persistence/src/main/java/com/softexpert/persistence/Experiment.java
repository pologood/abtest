package com.softexpert.persistence;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(callSuper = false, of = "id")
@SequenceGenerator(name = "seq_experiment", initialValue = 1)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Experiment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_experiment")
	public Long id;

	@Size(min = 1)
	public String name;

	public String description;

	@ElementCollection
	@CollectionTable(name = "domains", joinColumns = @JoinColumn(name = "domain_id") )
	@Column(name = "domains")
	public List<String> domains;

	@ElementCollection
	@CollectionTable(name = "groups", joinColumns = @JoinColumn(name = "group_id") )
	@Column(name = "groups")
	public List<String> groups;

	@ElementCollection
	@CollectionTable(name = "username", joinColumns = @JoinColumn(name = "user_id") )
	@Column(name = "user")
	public List<String> users;

	public Boolean enabled;

	public BigDecimal percentage;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "EXPERIMENT_ID")
	public List<Variation> variations;

	public Experiment(Long id, String name, Boolean enabled, BigDecimal percentage) {
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.percentage = percentage;
	}

}
