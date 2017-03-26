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
import com.crafaelsouza.crm.service.CustomerService;

/**
 * The Class CustomerServiceImpl responsible for managing all operations over customer.
 * 
 * @author Carlos Souza
 */
@Component("customerService")
public class CustomerServiceImpl implements CustomerService {

	/** The customer repository. */
	private CustomerRepository customerRepository;
	
	/** The appointment repository. */
	private AppointmentRepository appointmentRepository;
	
	/**
	 * Instantiates a new customer service impl.
	 *
	 * @param customerRepository the customer repository
	 * @param appointmentRepository the appointment repository
	 */
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository, AppointmentRepository appointmentRepository) {
		super();
		this.customerRepository = customerRepository;
		this.appointmentRepository = appointmentRepository;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#save(com.crafaelsouza.crm.entity.Customer)
	 */
	@Override
	public Customer save(Customer customer) throws CRMException {
		return customerRepository.save(customer);
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#update(com.crafaelsouza.crm.entity.Customer)
	 */
	@Override
	public Customer update(Customer customer) throws CRMException {
		return customerRepository.update(customer);

	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#partialUpdate(com.crafaelsouza.crm.entity.Customer)
	 */
	@Override
	public Customer partialUpdate(Customer customer) throws CRMException {
		return customerRepository.partialUpdate(customer);
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#delete(java.lang.Integer)
	 */
	@Override
	public void delete(Integer id) throws CRMException {
		customerRepository.delete(id);

	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#findById(java.lang.Integer)
	 */
	@Override
	public Customer findById(Integer id) {
		Customer customer = customerRepository.findById(id);
		return customer;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#findAll()
	 */
	@Override
	public List<Customer> findAll() {
		List<Customer> customers = customerRepository.findAll();
		return customers;
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#findNextAppointment(java.lang.Integer)
	 */
	@Override
	public Appointment findNextAppointment(Integer customerId) {
		Appointment appointment = appointmentRepository.findNextByCustomer(customerId);
		return appointment;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#createAppointment(java.lang.Integer, com.crafaelsouza.crm.entity.Appointment)
	 */
	@Override
	public Appointment createAppointment(Integer customerId, Appointment appointment)
		throws CRMException {
		Customer existingCustomer = validateAndReturnCustomer(customerId);
		appointment.setCustomer(existingCustomer);
		return appointmentRepository.save(appointment);
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#rateAppointment(com.crafaelsouza.crm.entity.Appointment, java.lang.Integer)
	 */
	@Override
	public Appointment rateAppointment(Appointment appointment, Integer customerId) throws CRMException {
		Customer existingCustomer = validateAndReturnCustomer(customerId);
		Appointment existingAppointment = validateAndReturnAppointment(appointment.getId());
		validateAppointmentBelongsToCustomer(existingAppointment, existingCustomer);
		appointment.setCustomer(existingCustomer);
		Appointment updatedAppointment = appointmentRepository.rateAppointment(appointment);
		return updatedAppointment;
	}

	/**
	 * Validate appointment belongs to customer.
	 *
	 * @param existingAppointment the existing appointment
	 * @param existingCustomer the existing customer
	 * @throws CRMException the crm exception
	 */
	private void validateAppointmentBelongsToCustomer(
			Appointment existingAppointment, Customer existingCustomer) throws CRMException {
		if (!existingAppointment.getCustomer().getId().equals(existingCustomer.getId())) {
			throw new CRMException(new ErrorDTO(
					"This appointment with ID " + existingAppointment.getId()
							+ " belongs to another Customer"));
		}
		
	}

	/**
	 * Validate and return appointment.
	 *
	 * @param appointId the appoint id
	 * @return the appointment
	 * @throws CRMException the crm exception
	 */
	private Appointment validateAndReturnAppointment(Integer appointId) throws CRMException {
		Appointment existingAppointment = appointmentRepository.findById(appointId);
		if (existingAppointment == null) {
			throw new CRMException(new ErrorDTO("Appointment with ID "+ appointId + " does not exist"));
		}
		return existingAppointment;
	}

	/**
	 * Validate and return customer.
	 *
	 * @param customerId the customer id
	 * @return the customer
	 * @throws CRMException the crm exception
	 */
	private Customer validateAndReturnCustomer(Integer customerId) throws CRMException {
		Customer existingCustomer = customerRepository.findById(customerId);
		if (existingCustomer == null) {
			throw new CRMException(new ErrorDTO("Customer with ID "+ customerId + " does not exist"));
		}
		return existingCustomer;
	}
	
	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#findAllAppointmentsByCustomer(java.lang.Integer)
	 */
	@Override
	public List<Appointment> findAllAppointmentsByCustomer(Integer customerId) {
		List<Appointment> appointments = appointmentRepository.findAllByCustomer(customerId);
		return appointments;
	}

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.service.CustomerService#findAppointmentByIdAndCustomerId(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Appointment findAppointmentByIdAndCustomerId(Integer customerId, Integer appointId) {
		Appointment appointment = appointmentRepository.findByIdAndCustomer(customerId, appointId);
		return appointment;
	}

}
