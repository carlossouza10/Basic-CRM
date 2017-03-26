package com.crafaelsouza.crm.service;

import java.util.List;

import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.exception.CRMException;

/**
 * The Interface AppointmentService.
 * 
 * @author Carlos Souza
 */
public interface AppointmentService {

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the appointment
	 */
	public Appointment findById(Integer id);
	
	/**
	 * Find all.
	 *
	 * @return the list
	 */
	public List<Appointment> findAll();
	
	/**
	 * Partial update.
	 *
	 * @param appointment the appointment
	 * @return the appointment
	 * @throws CRMException the crm exception
	 */
	public Appointment partialUpdate(Appointment appointment) throws CRMException;
	
	/**
	 * Find appointment for next weeks ahead.
	 *
	 * @param numberWeeksAhead the number weeks ahead
	 * @return the list
	 */
	List<Appointment> findNextWeeks(Integer numberWeeksAhead);
}
