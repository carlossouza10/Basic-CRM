package com.crafaelsouza.crm.repository.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.transaction.annotation.Transactional;

import com.crafaelsouza.crm.dto.ErrorDTO;
import com.crafaelsouza.crm.entity.EntityModel;
import com.crafaelsouza.crm.exception.CRMException;
import com.crafaelsouza.crm.repository.CustomCrudRepository;

/**
 * The Class AbstractCustomCrudRepository responsible for process a customized crud operation.
 *
 * @param <T> the generic type
 * @param <ID> the generic type
 * 
 * @author Carlos Souza
 */
@Transactional
public abstract class AbstractCustomCrudRepository<T extends EntityModel, ID extends Serializable>
	implements CustomCrudRepository<T, ID> {

	/** The domain class. */
	private Class<T> domainClass;
	
	/**
	 * Gets the validator.
	 *
	 * @return the validator
	 */
	protected abstract Validator getValidator();
	
	/**
	 * Gets the entity manager.
	 *
	 * @return the entity manager
	 */
	protected abstract EntityManager getEntityManager();
	
	/**
	 * Validate and apply changes in old entity. 
	 * Method invoked in case of partial update.
	 *
	 * @param oldEntity the old entity
	 * @param partialEntity the partial entity
	 * @throws CRMException the crm exception
	 */
	protected abstract void validateAndApplyChangesInOldEntity(T oldEntity, T partialEntity)
				throws CRMException;
	
	/**
	 * Validate rules before saving.
	 *
	 * @param entity the entity
	 * @throws CRMException the CRMexception
	 */
	protected abstract void validateRulesBeforeSaving(T entity) throws CRMException;
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.CustomCrudRepository#findAll()
	 */
	@Override
	public List<T> findAll() {
		List<T> resultList = getEntityManager().createQuery(
				"FROM " + domainClass.getSimpleName(), domainClass)
				.getResultList();
		return resultList;
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.CustomCrudRepository#findById(java.lang.Integer)
	 */
	@Override
	public T findById(Integer id) {
		T entityFound = getEntityManager().find(domainClass, id);
		return entityFound;
	}
	
	/**
	 * Instantiates a new abstract custom crud repository.
	 */
	@SuppressWarnings("unchecked")
	public AbstractCustomCrudRepository() {
		this.domainClass = (Class<T>) ((java.lang.reflect.ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.CustomCrudRepository#save(com.crafaelsouza.crm.entity.EntityModel)
	 */
	@Override
	public T save(T entity) throws CRMException {
		validateConstraint(entity);
		if (entity.getId() != null) {
			ErrorDTO errorDTO = new ErrorDTO("The field 'id' cannot be informed for customer creation.");
			throw new CRMException(errorDTO);
		}
		validateRulesBeforeSaving(entity);
		T newEntity = getEntityManager().merge(entity);
		return newEntity;
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.CustomCrudRepository#update(com.crafaelsouza.crm.entity.EntityModel)
	 */
	@Override
	public T update(T entity) throws CRMException {
		validateConstraint(entity);
		validateAndReturnExistingEntity(entity.getId(), domainClass);
		T newEntity = getEntityManager().merge(entity);
		return newEntity;
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.CustomCrudRepository#delete(java.lang.Integer)
	 */
	@Override
	public void delete(Integer id) throws CRMException {
		T existingEntity = validateAndReturnExistingEntity(id, domainClass);
		getEntityManager().remove(existingEntity);
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.repository.CustomCrudRepository#partialUpdate(com.crafaelsouza.crm.entity.EntityModel)
	 */
	@Override
	public T partialUpdate(T entity) throws CRMException {
		T entityFound = validateAndReturnExistingEntity(entity.getId(), domainClass);
		validateAndApplyChangesInOldEntity(entityFound, entity);
		T updatedEntity = getEntityManager().merge(entityFound);
		return updatedEntity;
	}
	
	/**
	 * Validate constraint.
	 *
	 * @param entity the entity
	 * @throws CRMException the CRMexception
	 */
	protected void validateConstraint( T entity)
		throws CRMException {
		Set<ConstraintViolation<T>> constraints = getValidator().validate(entity);
		if (!constraints.isEmpty()) {
			List<String> messages = new ArrayList<String>();
			for (ConstraintViolation<T> constraintViolation : constraints) {
				messages.add(constraintViolation.getMessage());
			}
			ErrorDTO error = new ErrorDTO(messages);
			throw new CRMException(error);
		}
	}

	/**
	 * Validate and return existing entity.
	 *
	 * @param id the id
	 * @param clazz the clazz
	 * @return the t
	 * @throws CRMException the crm exception
	 */
	protected T validateAndReturnExistingEntity(Integer id, Class<T> clazz)
		throws CRMException {
		if (id == null) {
			ErrorDTO error = new ErrorDTO("The field 'id' cannot be null.");
			throw new CRMException(error);
		}
		T entityFound = getEntityManager().find(clazz, id);
		if (entityFound == null) {
			ErrorDTO error = new ErrorDTO(clazz.getSimpleName() + " with 'id' " + id + " does not exist.");
			throw new CRMException(error);
		}
		return entityFound;
	}
}
