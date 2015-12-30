package com.softexpert.persistence;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@SequenceGenerator(name="seq_user", initialValue=1)

public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_user")
	public Long id;
	
}
