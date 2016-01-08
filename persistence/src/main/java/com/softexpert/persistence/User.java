package com.softexpert.persistence;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(name = "seq_user", initialValue = 1)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
	public Long id;

	public String code;

	public String name;

	public String login;

	public String department;

	public String host;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public List<UserExperiment> experiments;

}
