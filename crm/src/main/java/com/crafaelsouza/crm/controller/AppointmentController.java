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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crafaelsouza.crm.dto.AppointmentDTO;
import com.crafaelsouza.crm.dto.ResponseDTO;
import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.service.AppointmentService;
import com.crafaelsouza.crm.util.DateParserUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * The Class CustomerController responsible for exposuring services for Appointment
 * 
 * @author Carlos Souza
 */
@RestController
@RequestMapping(value="/crm/api/v1/appointments")
@Api(value = "/Appointments",  description = "Appointment Management", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppointmentController {

	/** The appointment service. */
	private AppointmentService appointmentService;

	/**
	 * Instantiates a new appointment controller.
	 *
	 * @param appointmentService the appointment service
	 */
	@Autowired
	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}

	/**
	 * Find appointments.
	 *
	 * @param numberWeeksAhead the number weeks ahead
	 * @return the response entity
	 */
	@RequestMapping(method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Retrieves all appointments or appointments for next weeks (if the parameter weeks_ahead is passed).",
	notes="Retrieves all appointments or appointments for next weeks (if the parameter weeks_ahead is passed).")
	public ResponseEntity<List<AppointmentDTO>> findAppointments(
			@ApiParam(value = "Number of weeks ahead", required=false)
			@RequestParam(name = "weeks_ahead", required = false) Integer numberWeeksAhead) {
		List<Appointment> appointments = null;
		 if (numberWeeksAhead == null) {
			appointments = appointmentService.findAll();
		 } else {
			appointments = appointmentService.findNextWeeks(numberWeeksAhead);
		 }
		 List<AppointmentDTO> appointmentsDTO = toListDTO(appointments);
		 return new ResponseEntity<List<AppointmentDTO>>(appointmentsDTO, HttpStatus.OK);
	}
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(path="/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value="Retrieves a specific appointment.")
	public ResponseEntity<ResponseDTO> findById(@PathVariable("id") Integer id) {
		 Appointment appointment = appointmentService.findById(id);
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
	 * To list DTO.
	 *
	 * @param appointments the appointments
	 * @return the list
	 */
	private List<AppointmentDTO> toListDTO(List<Appointment> appointments) {
		List<AppointmentDTO> appointmentsDTO = new ArrayList<AppointmentDTO>();
		Mapper mapper = new DozerBeanMapper();
		for (Appointment appointment : appointments) {
			AppointmentDTO dto = mapper.map(appointment, AppointmentDTO.class);
			dto.setDateAndTime(DateParserUtil.toString(appointment.getDate()));
			dto.setCustomerId(appointment.getCustomer().getId());
			dto.setCustomerFirstName(appointment.getCustomer().getFirstName());
			dto.setCustomerSurname(appointment.getCustomer().getSurname());
			appointmentsDTO.add(dto);
		}
		return appointmentsDTO;
	}
	
}
