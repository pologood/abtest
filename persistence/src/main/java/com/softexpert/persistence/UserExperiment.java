package com.softexpert.persistence;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="seq_usertest", initialValue=1)
public class UserExperiment {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_usertest")
	public Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	public User user;
	@ManyToOne(fetch = FetchType.LAZY)
	public Variation variation; 
	@ManyToOne(fetch = FetchType.LAZY)
	public Experiment experiment; 
	
}
