package com.crafaelsouza.crm.repository;

import java.util.List;

import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.exception.CRMException;

/**
 * The Interface AppointmentRepository.
 * 
 * @author Carlos Souza
 */
public interface AppointmentRepository extends CustomCrudRepository<Appointment, Integer> {

	/**
	 * Find next weeks.
	 *
	 * @param numberWeeksAhead the number weeks ahead
	 * @return the list
	 */
	List<Appointment> findNextWeeks(Integer numberWeeksAhead);
	
	/**
	 * Find next by customer.
	 *
	 * @param customerId the customer id
	 * @return the appointment
	 */
	Appointment findNextByCustomer(Integer customerId);
	
	/**
	 * Rate appointment.
	 *
	 * @param appointmentRated the appointment rated
	 * @return the appointment
	 * @throws CRMException the crm exception
	 */
	Appointment rateAppointment(Appointment appointmentRated) throws CRMException;
	
	/**
	 * Find all by customer.
	 *
	 * @param customerId the customer id
	 * @return the list
	 */
	List<Appointment> findAllByCustomer(Integer customerId);
	
	/**
	 * Find by id and customer.
	 *
	 * @param customerId the customer id
	 * @param appointId the appoint id
	 * @return the appointment
	 */
	Appointment findByIdAndCustomer(Integer customerId, Integer appointId);
}
