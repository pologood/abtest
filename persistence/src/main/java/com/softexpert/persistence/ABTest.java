package com.softexpert.persistence;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@SequenceGenerator(name="seq_test", initialValue=1)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class ABTest {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_test")
	public Long id;
	
	@Size(min = 1)
	public String name;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public Feature feature;
	
	public ABTest(Long id, String name){
		this.id = id;
		this.name = name;
	}

}
