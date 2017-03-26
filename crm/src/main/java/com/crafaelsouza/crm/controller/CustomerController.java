package com.crafaelsouza.crm.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crafaelsouza.crm.dto.AppointmentDTO;
import com.crafaelsouza.crm.dto.AppointmentRatingDTO;
import com.crafaelsouza.crm.dto.CustomerDTO;
import com.crafaelsouza.crm.dto.ResponseDTO;
import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.entity.Customer;
import com.crafaelsouza.crm.exception.CRMException;
import com.crafaelsouza.crm.service.CustomerService;
import com.crafaelsouza.crm.util.DateParserUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * The Class CustomerController responsible for exposuring services for Customer
 * 
 * @author Carlos Souza
 */
@RestController
@RequestMapping(value="/crm/api/v1/customers")
@Api(value = "/Customers",  description = "Customer Management", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

	/** The customer service. */
	private CustomerService customerService;

	/**
	 * Instantiates a new customer controller.
	 *
	 * @param customerService the customer service
	 */
	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	/**
	 * Creates the.
	 *
	 * @param customerDTO the customer DTO
	 * @return the response entity
	 */
	@RequestMapping(method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Creates a new customer.", notes="Creates a new customer.")
	public ResponseEntity<ResponseDTO> create(@RequestBody CustomerDTO customerDTO) {
		try {
			Mapper mapper = new DozerBeanMapper();
			Customer customer = mapper.map(customerDTO, Customer.class);
			Customer newCustomer = customerService.save(customer);
			CustomerDTO newCustomerDTO = mapper.map(newCustomer, CustomerDTO.class);
			return new ResponseEntity<ResponseDTO>(newCustomerDTO, HttpStatus.CREATED);
		} catch (CRMException e) {
			return new ResponseEntity<ResponseDTO>(e.getError(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Update.
	 *
	 * @param id the id
	 * @param customerDTO the customer DTO
	 * @return the response entity
	 */
	@RequestMapping(path="/{id}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Updates a specific customer.", notes="Updates a specific customer.")
	public ResponseEntity<ResponseDTO> update(@PathVariable("id") Integer id, @RequestBody CustomerDTO customerDTO) {
		try {
			Mapper mapper = new DozerBeanMapper();
			customerDTO.setId(id);
			Customer customer = mapper.map(customerDTO, Customer.class);
			Customer updatedCustomer = customerService.update(customer);
			CustomerDTO updatedCustomerDTO = mapper.map(updatedCustomer, CustomerDTO.class);
			return new ResponseEntity<ResponseDTO>(updatedCustomerDTO, HttpStatus.OK);
		} catch (CRMException e) {
			return new ResponseEntity<ResponseDTO>(e.getError(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Partial update.
	 *
	 * @param id the id
	 * @param customerDTO the customer DTO
	 * @return the response entity
	 */
	@RequestMapping(path="/{id}", method= RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Update a customer partially.", notes="Update a customer partially.")
	public ResponseEntity<ResponseDTO> partialUpdate(@PathVariable("id") Integer id, @RequestBody CustomerDTO customerDTO) {
		try {
			Mapper mapper = new DozerBeanMapper();
			customerDTO.setId(id);
			Customer customer = mapper.map(customerDTO, Customer.class);
			Customer updatedCustomer = customerService.partialUpdate(customer);
			CustomerDTO updatedCustomerDTO = mapper.map(updatedCustomer, CustomerDTO.class);
			return new ResponseEntity<ResponseDTO>(updatedCustomerDTO, HttpStatus.OK);
		} catch (CRMException e) {
			return new ResponseEntity<ResponseDTO>(e.getError(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Delete.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(path="/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Delete a specific customer and its appointments.", notes="Delete a specific customer.")
	public ResponseEntity<ResponseDTO> delete(@PathVariable("id") Integer id) {
		try {
			customerService.delete(id);
		} catch (CRMException e) {
			return new ResponseEntity<ResponseDTO>(e.getError(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ResponseDTO>(HttpStatus.OK);
	}
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(path="/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Retrieves a specific customer.", notes="Retrieves a specific customer.")
	public ResponseEntity<ResponseDTO> findById(@PathVariable("id") Integer id) {
		 Customer customer = customerService.findById(id);
		 CustomerDTO dto = null;
		 if (customer != null) {
			 Mapper mapper = new DozerBeanMapper();
			 dto = mapper.map(customer, CustomerDTO.class);
		 } else {
			 return new ResponseEntity<ResponseDTO>(dto, HttpStatus.NOT_FOUND);
		 }
		 return new ResponseEntity<ResponseDTO>(dto, HttpStatus.OK);
	}
	
	/**
	 * Find all.
	 *
	 * @return the response entity
	 */
	@RequestMapping(method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Retrieves a list of customers.", notes="Retrieves a list of customers.")
	public ResponseEntity<List<CustomerDTO>> findAll() {
		 List<Customer> customers = customerService.findAll();
		 List<CustomerDTO> customersDTO = toListDTO(customers);
		 return new ResponseEntity<List<CustomerDTO>>(customersDTO, HttpStatus.OK);
	}
	
	/**
	 * Find next appoitment.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(path="/{id}/appointments/next", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Retrieves the next appoinment of a specific customer.",
				 notes="Retrieves the next appoinment of a specific customer.")
	public ResponseEntity<ResponseDTO> findNextAppoitment(@PathVariable("id") Integer id) {
		 Appointment appointment = customerService.findNextAppointment(id);
		 AppointmentDTO appointmentDTO = null;
		 if (appointment != null) {
			 Mapper mapper = new DozerBeanMapper();
			 appointmentDTO = mapper.map(appointment, AppointmentDTO.class);
			 appointmentDTO.setDateAndTime(DateParserUtil.toString(appointment.getDate()));
		 } else {
			 return new ResponseEntity<ResponseDTO>(appointmentDTO, HttpStatus.NOT_FOUND);
		 }
		 return new ResponseEntity<ResponseDTO>(appointmentDTO, HttpStatus.OK);
	}
	
	/**
	 * Creates the appointment.
	 *
	 * @param customerId the customer id
	 * @param appointmentDTO the appointment DTO
	 * @return the response entity
	 */
	@RequestMapping(path="/{id}/appointments", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Creates an appoinments for a specific customer.",
			     notes="Creates an appoinments for a specific customer.")
	public ResponseEntity<ResponseDTO> createAppointment(@PathVariable("id") Integer customerId,
			@RequestBody AppointmentDTO appointmentDTO) {
		
		try {
			Mapper mapper = new DozerBeanMapper();
			Appointment appointment = mapper.map(appointmentDTO, Appointment.class);
			appointment.setDate(DateParserUtil.toDate(appointmentDTO.getDateAndTime()));
		 
			appointment = customerService.createAppointment(customerId, appointment);
			appointmentDTO = mapper.map(appointment, AppointmentDTO.class);
			appointmentDTO.setDateAndTime(DateParserUtil.toString(appointment.getDate()));
			return new ResponseEntity<ResponseDTO>(appointmentDTO, HttpStatus.OK);
		} catch (CRMException e) {
			return new ResponseEntity<ResponseDTO>(e.getError(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Find all appointments.
	 *
	 * @param customerId the customer id
	 * @return the response entity
	 */
	@RequestMapping(path="/{custId}/appointments", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Retrieves all appointments of a customer.", notes="Retrieves all appointments of a customer.")
	public ResponseEntity<List<AppointmentDTO>> findAllAppointments(@PathVariable("custId") Integer customerId) {
		 List<Appointment> customers = customerService.findAllAppointmentsByCustomer(customerId);
		 List<AppointmentDTO> customersDTO = toListAppointDTO(customers);
		 return new ResponseEntity<List<AppointmentDTO>>(customersDTO, HttpStatus.OK);
	}
	
	/**
	 * Rate appointment.
	 *
	 * @param customerId the customer id
	 * @param appointmentId the appointment id
	 * @param appointRatingDTO the appoint rating DTO
	 * @return the response entity
	 */
	@RequestMapping(path="/{custId}/appointments/{appointId}/rating",method= RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Creates a rating for a appointment.", notes="Creates a rating for a appointment.")
	public ResponseEntity<ResponseDTO> rateAppointment(@PathVariable("custId") Integer customerId,
			@PathVariable("appointId") Integer appointmentId, @RequestBody AppointmentRatingDTO appointRatingDTO) {
		 Appointment appointment = new Appointment();
		 appointment.setRating(appointRatingDTO.getRating());
		 appointment.setRatingDescription(appointRatingDTO.getRatingDescription());
		 appointment.setId(appointmentId);
		 
		 Appointment updatedAppointment;
		try {
			 updatedAppointment = customerService.rateAppointment(appointment, customerId);
			 Mapper mapper = new DozerBeanMapper();
			 AppointmentDTO appointmentDTO = mapper.map(updatedAppointment, AppointmentDTO.class);
			 appointmentDTO.setDateAndTime(DateParserUtil.toString(updatedAppointment.getDate()));
		 return new ResponseEntity<ResponseDTO>(appointmentDTO, HttpStatus.OK);
		} catch (CRMException e) {
			return new ResponseEntity<ResponseDTO>(e.getError(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * Find appointment by id and customer.
	 *
	 * @param customerId the customer id
	 * @param appointId the appoint id
	 * @return the response entity
	 */
	@RequestMapping(path="/{custId}/appointments/{appointId}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Retrieves a specific appointment for a specific customer.",
					notes="Retrieves a specific appointment for a specific customer.")
	public ResponseEntity<ResponseDTO> findAppointmentByIdAndCustomer(@PathVariable("custId") Integer customerId,
			Integer appointId) {
		 Appointment appointment = customerService.findAppointmentByIdAndCustomerId(customerId, appointId);
		 AppointmentDTO dto = null;
		 if (appointment != null) {
			 Mapper mapper = new DozerBeanMapper();
			 dto = mapper.map(appointment, AppointmentDTO.class);
			 dto.setDateAndTime(DateParserUtil.toString(appointment.getDate()));
		 } else {
			 return new ResponseEntity<ResponseDTO>(dto, HttpStatus.NOT_FOUND);
		 }
		 return new ResponseEntity<ResponseDTO>(dto, HttpStatus.OK);
	}
	
	/**
	 * To list appoint DTO.
	 *
	 * @param appointments the appointments
	 * @return the list
	 */
	private List<AppointmentDTO> toListAppointDTO(List<Appointment> appointments) {
		List<AppointmentDTO> customersDTO = new ArrayList<AppointmentDTO>();
		Mapper mapper = new DozerBeanMapper();
		for (Appointment appointment : appointments) {
			AppointmentDTO dto = mapper.map(appointment, AppointmentDTO.class);
			dto.setDateAndTime(DateParserUtil.toString(appointment.getDate()));
			customersDTO.add(dto);
		}
		return customersDTO;
	}
	
	/**
	 * To list DTO.
	 *
	 * @param customers the customers
	 * @return the list
	 */
	private List<CustomerDTO> toListDTO(List<Customer> customers) {
		List<CustomerDTO> customersDTO = new ArrayList<CustomerDTO>();
		Mapper mapper = new DozerBeanMapper();
		for (Customer customer : customers) {
			CustomerDTO dto = mapper.map(customer, CustomerDTO.class);
			customersDTO.add(dto);
		}
		return customersDTO;
	}
	
}
