package com.softexpert.persistence;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@SequenceGenerator(name="seq_usertest", initialValue=1)
public class UserTest {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_usertest")
	public Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	public User user;
	@ManyToOne(fetch = FetchType.LAZY)
	public ABTest test; 
	
}
