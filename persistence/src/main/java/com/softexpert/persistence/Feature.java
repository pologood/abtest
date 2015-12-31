package com.softexpert.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(callSuper=false, of="id")
@SequenceGenerator(name="seq_feature", initialValue=1)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feature {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_feature")
	public Long id;
	
	@Size(min = 1)
	public String name;
	
	public Boolean enabled;
	
}