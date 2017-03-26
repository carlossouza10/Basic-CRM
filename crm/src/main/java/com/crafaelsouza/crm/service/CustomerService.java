package com.crafaelsouza.crm.service;

import java.util.List;

import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.entity.Customer;
import com.crafaelsouza.crm.exception.CRMException;

/**
 * The Interface CustomerService.
 * 
 * @author Carlos Souza
 */
public interface CustomerService {

	/**
	 * Save.
	 *
	 * @param customer the customer
	 * @return the customer
	 * @throws CRMException the crm exception
	 */
	public Customer save(Customer customer) throws CRMException;
	
	/**
	 * Update.
	 *
	 * @param customer the customer
	 * @return the customer
	 * @throws CRMException the crm exception
	 */
	public Customer update(Customer customer) throws CRMException;
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 * @throws CRMException the crm exception
	 */
	public void delete(Integer id) throws CRMException;
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the customer
	 */
	public Customer findById(Integer id);
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<Customer> findAll();
	
	/**
	 * Partial update.
	 *
	 * @param customer the customer
	 * @return the customer
	 * @throws CRMException the crm exception
	 */
	public Customer partialUpdate(Customer customer) throws CRMException;
	
	/**
	 * Find next appointment.
	 *
	 * @param customerId the customer id
	 * @return the appointment
	 */
	Appointment findNextAppointment(Integer customerId);
	
	/**
	 * Rate appointment.
	 *
	 * @param appointment the appointment
	 * @param customerId the customer id
	 * @return the appointment
	 * @throws CRMException the crm exception
	 */
	public Appointment rateAppointment(Appointment appointment, Integer customerId) throws CRMException;
	
	/**
	 * Creates the appointment.
	 *
	 * @param customerId the customer id
	 * @param appointment the appointment
	 * @return the appointment
	 * @throws CRMException the crm exception
	 */
	public Appointment createAppointment(Integer customerId, Appointment appointment) throws CRMException;
	
	/**
	 * Find all appointments by customer.
	 *
	 * @param customerId the customer id
	 * @return the list
	 */
	public List<Appointment> findAllAppointmentsByCustomer(Integer customerId);
	
	/**
	 * Find appointment by id and customer id.
	 *
	 * @param customerId the customer id
	 * @param appointId the appoint id
	 * @return the appointment
	 */
	public Appointment findAppointmentByIdAndCustomerId(Integer customerId,
			Integer appointId);
}
