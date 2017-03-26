package com.crafaelsouza.crm.repository;

import java.io.Serializable;
import java.util.List;

import com.crafaelsouza.crm.entity.EntityModel;
import com.crafaelsouza.crm.exception.CRMException;

/**
 * The Interface CustomCrudRepository that defines crud methods.
 *
 * @param <T> the generic type
 * @param <ID> the generic type
 * 
 * @author Carlos Souza
 */
public interface CustomCrudRepository<T extends EntityModel, ID extends Serializable> {

	/**
	 * Save.
	 *
	 * @param entity the entity
	 * @return the t
	 * @throws CRMException the crm exception
	 */
	T save(T entity) throws CRMException;
	
	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @return the t
	 * @throws CRMException the crm exception
	 */
	T update(T entity) throws CRMException;
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the t
	 */
	T findById(Integer id);
	
	/**
	 * Partial update.
	 *
	 * @param entity the entity
	 * @return the t
	 * @throws CRMException the crm exception
	 */
	T partialUpdate(T entity) throws CRMException;
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	List<T> findAll();
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 * @throws CRMException the crm exception
	 */
	void delete(Integer id) throws CRMException;
}
