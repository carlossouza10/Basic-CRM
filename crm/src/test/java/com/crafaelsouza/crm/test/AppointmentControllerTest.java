package com.crafaelsouza.crm.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crafaelsouza.crm.controller.AppointmentController;
import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.repository.AppointmentRepository;
import com.crafaelsouza.crm.repository.CustomerRepository;
import com.crafaelsouza.crm.repository.impl.AppointmentRepositoryImpl;
import com.crafaelsouza.crm.repository.impl.CustomerRepositoryImpl;
import com.crafaelsouza.crm.service.AppointmentService;
import com.crafaelsouza.crm.service.impl.AppointmentServiceImpl;
import com.crafaelsouza.crm.util.DateParserUtil;

/**
 * The Class AppointmentControllerTest.
 * 
 * @author Carlos Souza
 */
@RunWith(MockitoJUnitRunner.class)
public class AppointmentControllerTest extends AbstractTest {

	/** The entity manager. */
	@Mock
	private EntityManager entityManager;
	
	/** The appointment repository. */
	private AppointmentRepository appointmentRepository;
	
	/** The customer repository. */
	private CustomerRepository customerRepository;
	
	/** The appointment service. */
	private AppointmentService appointmentService;
	
	/** The validator. */
	private Validator validator;
	
	/** The mock mvc. */
	private MockMvc mockMvc;
	
	/**
	 * Sets the up.
	 */
	@Before
    public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		appointmentRepository = new AppointmentRepositoryImpl(validator, entityManager);
		customerRepository = new CustomerRepositoryImpl(validator, entityManager);
    	appointmentService = new AppointmentServiceImpl(appointmentRepository, customerRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(new AppointmentController(appointmentService)).build();
    }
	
	/**
	 * Test find all.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testFindAll() throws Exception {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Appointment appointment2 = getPopulatedAppointmentWithoutIdAndRatingV2();
		appointment2.setId(2);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment, appointment2)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"), Mockito.any());
		
		this.mockMvc.perform(get("/crm/api/v1/appointments")).andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").value(appointment.getId()))
				.andExpect(jsonPath("$.[0].description").value(appointment.getDescription()))
				.andExpect(jsonPath("$.[0].dateAndTime").value(DateParserUtil.toString(appointment.getDate())))
				.andExpect(jsonPath("$.[1].id").value(appointment2.getId()))
				.andExpect(jsonPath("$.[1].description").value(appointment2.getDescription()))
				.andExpect(jsonPath("$.[1].dateAndTime").value(DateParserUtil.toString(appointment2.getDate())));
	}
	
	/**
	 * Test find next weeks.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testFindNextWeeks() throws Exception {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Appointment appointment2 = getPopulatedAppointmentWithoutIdAndRatingV2();
		appointment2.setId(2);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment, appointment2)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		
		this.mockMvc.perform(get("/crm/api/v1/appointments?weeks_ahead=1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id").value(appointment.getId()))
				.andExpect(jsonPath("$.[0].description").value(appointment.getDescription()))
				.andExpect(jsonPath("$.[0].dateAndTime").value(DateParserUtil.toString(appointment.getDate())))
				.andExpect(jsonPath("$.[1].id").value(appointment2.getId()))
				.andExpect(jsonPath("$.[1].description").value(appointment2.getDescription()))
				.andExpect(jsonPath("$.[1].dateAndTime").value(DateParserUtil.toString(appointment2.getDate())));
	}
	
	/**
	 * Test find by id successful.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testFindByIdSuccessful() throws Exception {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Mockito.doReturn(appointment).when(entityManager).find(Appointment.class, 1);
		this.mockMvc.perform(get("/crm/api/v1/appointments/1")).andExpect(status().isOk())
		.andExpect(jsonPath("$.id", Matchers.equalTo(appointment.getId())))
		.andExpect(jsonPath("$.description", Matchers.equalTo(appointment.getDescription())))
		.andExpect(jsonPath("$.dateAndTime", Matchers.equalTo(DateParserUtil.toString(appointment.getDate()))));
	}
	
	/**
	 * Test find by id no result.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testFindByIdNoResult() throws Exception {
		Mockito.doReturn(null).when(entityManager).find(Appointment.class, 1);
		this.mockMvc.perform(get("/crm/api/v1/appointments/1")).andExpect(status().isNotFound());
	}
}
