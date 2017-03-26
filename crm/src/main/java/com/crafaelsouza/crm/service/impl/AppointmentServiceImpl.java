package com.crafaelsouza.crm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.crafaelsouza.crm.dto.ErrorDTO;
import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.entity.Customer;
import com.crafaelsouza.crm.exception.CRMException;
import com.crafaelsouza.crm.repository.AppointmentRepository;
import com.crafaelsouza.crm.repository.CustomerRepository;
import com.crafaelsouza.crm.service.AppointmentService;

/**
 * The Class AppointmentServiceImplresponsible for managing all operations for appoinments.
 * 
 * @author Carlos Souza
 */
@Component("appointmentService")
public class AppointmentServiceImpl implements AppointmentService {

	/** The appointment repository. */
	private AppointmentRepository appointmentRepository;
	
	/** The customer repository. */
	private CustomerRepository customerRepository;
	
	/**
	 * Instantiates a new appointment service impl.
	 *
	 * @param appointmentRepository the appointment repository
	 * @param customerRepository the customer repository
	 */
	@Autowired
	public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
			CustomerRepository customerRepository) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.customerRepository = customerRepository;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.AppointmentService#partialUpdate(com.crafaelsouza.crm.entity.Appointment)
	 */
	@Override
	public Appointment partialUpdate(Appointment appointment) throws CRMException {
		validateExistingCustomer(appointment);
		return appointmentRepository.partialUpdate(appointment);
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.AppointmentService#findById(java.lang.Integer)
	 */
	@Override
	public Appointment findById(Integer id) {
		Appointment appointment = appointmentRepository.findById(id);
		return appointment;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.AppointmentService#findAll()
	 */
	@Override
	public List<Appointment> findAll() {
		List<Appointment> appointments = appointmentRepository.findAll();
		return appointments;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.AppointmentService#findNextWeeks(java.lang.Integer)
	 */
	@Override
	public List<Appointment> findNextWeeks(Integer numberWeeksAhead) {
		List<Appointment> findNextWeeks = appointmentRepository.findNextWeeks(numberWeeksAhead);
		return findNextWeeks;
	}

	/**
	 * Validate existing customer.
	 *
	 * @param entity the entity
	 * @throws CRMException the crm exception
	 */
	private void validateExistingCustomer(Appointment entity) throws CRMException {
		if (entity.getCustomer() == null || entity.getCustomer().getId() == null) {
			throw new CRMException(new ErrorDTO("Customer is null or does not exist."));
		}
		Customer customerFound = customerRepository.findById(entity.getId());
		if (customerFound == null) {
			throw new CRMException(new ErrorDTO("Customer does not exist."));
		}
	}
}
