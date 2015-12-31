package com.softexpert.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="seq_test", initialValue=1)
public class Test {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_test")
	public Long id;

}
