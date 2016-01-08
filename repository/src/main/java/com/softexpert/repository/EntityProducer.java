package com.softexpert.repository;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EntityProducer implements Serializable {

	private static final long serialVersionUID = -5165368545535190902L;

	@PersistenceContext
	private EntityManager entityManager;

	@Produces
	public EntityManager getEntityManager() {
		return entityManager;
	}
}