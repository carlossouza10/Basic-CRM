package com.crafaelsouza.crm.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.crafaelsouza.crm.entity.Customer;
import com.crafaelsouza.crm.exception.CRMException;
import com.crafaelsouza.crm.repository.CustomerRepository;

/**
 * The Class CustomerRepositoryImpl responsible for managing customer storage and their appointments.
 * 
 * @author Carlos Souza
 */
@Repository("customerRepository")
public class CustomerRepositoryImpl extends AbstractCustomCrudRepository<Customer, Integer>
	implements CustomerRepository {

	/** The validator. */
	@Autowired
	private Validator validator;

	/** The entity manager. */
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Instantiates a new customer repository impl.
	 *
	 * @param validator the validator
	 * @param entityManager the entity manager
	 */
	@Autowired
	public CustomerRepositoryImpl(Validator validator, EntityManager entityManager) {
		super();
		this.validator = validator;
		this.entityManager = entityManager;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.impl.AbstractCustomCrudRepository#getValidator()
	 */
	@Override
	protected Validator getValidator() {
		return validator;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.impl.AbstractCustomCrudRepository#getEntityManager()
	 */
	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.impl.AbstractCustomCrudRepository#validateAndApplyChangesInOldEntity(com.crafaelsouza.crm.entity.EntityModel, com.crafaelsouza.crm.entity.EntityModel)
	 */
	@Override
	protected void validateAndApplyChangesInOldEntity(Customer oldEntity,
			Customer partialEntity) {
		if (partialEntity.getFirstName() != null) {
			oldEntity.setFirstName(partialEntity.getFirstName());
		}
		if (partialEntity.getSurname() != null) {
			oldEntity.setSurname(partialEntity.getSurname());
		}
		if (partialEntity.getEmail() != null) {
			oldEntity.setEmail(partialEntity.getEmail());
		}
		if (partialEntity.getPhone() != null) {
			oldEntity.setPhone(partialEntity.getPhone());
		}
		if (partialEntity.getAddress() != null) {
			oldEntity.setAddress(partialEntity.getAddress());
		}
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.impl.AbstractCustomCrudRepository#validateRulesBeforeSaving(com.crafaelsouza.crm.entity.EntityModel)
	 */
	@Override
	protected void validateRulesBeforeSaving(Customer entity)
			throws CRMException {
		
	}

}
