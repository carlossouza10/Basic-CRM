package com.crafaelsouza.crm.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.crafaelsouza.crm.controller.CustomerController;
import com.crafaelsouza.crm.dto.AppointmentDTO;
import com.crafaelsouza.crm.dto.AppointmentRatingDTO;
import com.crafaelsouza.crm.dto.ErrorDTO;
import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.entity.Customer;
import com.crafaelsouza.crm.repository.AppointmentRepository;
import com.crafaelsouza.crm.repository.impl.AppointmentRepositoryImpl;
import com.crafaelsouza.crm.repository.impl.CustomerRepositoryImpl;
import com.crafaelsouza.crm.service.CustomerService;
import com.crafaelsouza.crm.service.impl.CustomerServiceImpl;
import com.crafaelsouza.crm.util.DateParserUtil;
import com.google.gson.Gson;

@RunWith(MockitoJUnitRunner.class)
public class CustomerControllerTest extends AbstractTest {

	@Mock
	private EntityManager entityManager;
	private CustomerRepositoryImpl customerRepository;
	private AppointmentRepository appointmentRepository;
	private CustomerService customerService;
	private Validator validator;
	private MockMvc mockMvc;
	
	@Before
    public void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		customerRepository = new CustomerRepositoryImpl(validator, entityManager);
		appointmentRepository = new AppointmentRepositoryImpl(validator, entityManager);
    	customerService = new CustomerServiceImpl(customerRepository, appointmentRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController(customerService)).build();
    }
	
	@Test
	public void testFindAll() throws Exception {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Customer customer2 = getPopulatedCustomerWithoutIdV2();
		customer2.setId(2);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(customer, customer2)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Customer"), Mockito.any());
		
		this.mockMvc.perform(get("/crm/api/v1/customers")).andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", Matchers.equalTo(customer.getId())))
				.andExpect(jsonPath("$.[0].firstName", Matchers.equalTo(customer.getFirstName())))
				.andExpect(jsonPath("$.[0].surname", Matchers.equalTo(customer.getSurname())))
				.andExpect(jsonPath("$.[0].phone", Matchers.equalTo(customer.getPhone())))
				.andExpect(jsonPath("$.[0].email", Matchers.equalTo(customer.getEmail())))
				.andExpect(jsonPath("$.[0].address", Matchers.equalTo(customer.getAddress())))
				.andExpect(jsonPath("$.[1].id", Matchers.equalTo(customer2.getId())))
				.andExpect(jsonPath("$.[1].firstName", Matchers.equalTo(customer2.getFirstName())))
				.andExpect(jsonPath("$.[1].surname", Matchers.equalTo(customer2.getSurname())))
				.andExpect(jsonPath("$.[1].phone", Matchers.equalTo(customer2.getPhone())))
				.andExpect(jsonPath("$.[1].email", Matchers.equalTo(customer2.getEmail())))
				.andExpect(jsonPath("$.[1].address", Matchers.equalTo(customer2.getAddress())));
	}
	
	@Test
	public void testFindAllAppointmentByCustomerId() throws Exception {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		
		this.mockMvc.perform(get("/crm/api/v1/customers/1/appointments")).andExpect(status().isOk())
					.andExpect(jsonPath("$.[0].id").value(appointment.getId()))
					.andExpect(jsonPath("$.[0].dateAndTime").value(DateParserUtil.toString(appointment.getDate())))
					.andExpect(jsonPath("$.[0].description").value(appointment.getDescription()));
	}
	
	
	@Test
	public void testFindByIdSuccessful() throws Exception {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, 1);
		this.mockMvc.perform(get("/crm/api/v1/customers/1")).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(customer.getId()))
		.andExpect(jsonPath("$.firstName").value(customer.getFirstName()))
		.andExpect(jsonPath("$.surname").value(customer.getSurname()))
		.andExpect(jsonPath("$.phone").value(customer.getPhone()))
		.andExpect(jsonPath("$.email").value(customer.getEmail()))
		.andExpect(jsonPath("$.address").value(customer.getAddress()));
	}
	
	@Test
	public void testFindNextAppoitment() throws Exception {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, 1);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		this.mockMvc.perform(get("/crm/api/v1/customers/1/appointments/next")).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(appointment.getId()))
		.andExpect(jsonPath("$.description").value(appointment.getDescription()))
		.andExpect(jsonPath("$.dateAndTime").value(DateParserUtil.toString(appointment.getDate())));
	}
	
	@Test
	public void testFindAppointmentByIdAndCustomer() throws Exception {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		
		this.mockMvc.perform(get("/crm/api/v1/customers/1/appointments/1")).andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(appointment.getId()))
		.andExpect(jsonPath("$.description").value(appointment.getDescription()))
		.andExpect(jsonPath("$.dateAndTime").value(DateParserUtil.toString(appointment.getDate())));
	}
	
	@Test
	public void testFindAppointmentByIdAndCustomerNoResult() throws Exception {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(new ArrayList<Appointment>()).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		
		this.mockMvc.perform(get("/crm/api/v1/customers/1/appointments/1")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testFindNextAppoitmentNoResult() throws Exception {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(new ArrayList<Appointment>()).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		this.mockMvc.perform(get("/crm/api/v1/customers/1/appointments/next")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testCreateAppointmentNonExistingCustomer() throws Exception {
		Appointment newAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		AppointmentDTO dto = new AppointmentDTO();
		dto.setDescription(newAppointment.getDescription());
		dto.setDateAndTime(DateParserUtil.toString(newAppointment.getDate()));
		Mockito.doReturn(null).when(entityManager).find(Customer.class, newAppointment.getCustomer().getId());
		
		Gson gson = new Gson();
		String json = gson.toJson(dto);
		this.mockMvc.perform(post("/crm/api/v1/customers/1/appointments/")
				 			.contentType(MediaType.APPLICATION_JSON)
				 			.content(json))
							.andExpect(status().isBadRequest())
							.andExpect(jsonPath("$.messages", Matchers.hasSize(1)));
	}
	
	@Test
	public void testCreateAppointmentSuccessful() throws Exception {
		Appointment newAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		newAppointment.setId(1);
		AppointmentDTO dto = new AppointmentDTO();
		dto.setDescription(newAppointment.getDescription());
		dto.setDateAndTime(DateParserUtil.toString(newAppointment.getDate()));
		Mockito.doReturn(newAppointment.getCustomer()).when(entityManager).find(Customer.class, newAppointment.getCustomer().getId());
		Mockito.doReturn(newAppointment).when(entityManager).merge(Mockito.any());
		
		Gson gson = new Gson();
		String json = gson.toJson(dto);
		this.mockMvc.perform(post("/crm/api/v1/customers/1/appointments/")
				 			.contentType(MediaType.APPLICATION_JSON)
				 			.content(json))
							.andExpect(status().isOk())
							.andExpect(jsonPath("$.id").value(newAppointment.getId()))
							.andExpect(jsonPath("$.description").value(newAppointment.getDescription()))
							.andExpect(jsonPath("$.dateAndTime").value(DateParserUtil.toString(newAppointment.getDate())));
	}
	
	@Test
	public void testRateAppointmentSuccessful() throws Exception {
		AppointmentRatingDTO ratingDTO = new AppointmentRatingDTO();
		ratingDTO.setRatingDescription("Rating description");
		ratingDTO.setRating(3);
		Appointment expectedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		expectedAppointment.setId(1);
		expectedAppointment.setRatingDescription(ratingDTO.getRatingDescription());
		expectedAppointment.setRating(ratingDTO.getRating());
		Appointment storedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		storedAppointment.setDate(getPreviousDay());
		storedAppointment.setId(1);
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(storedAppointment).when(entityManager).find(Appointment.class, expectedAppointment.getId());
		Mockito.doReturn(expectedAppointment).when(entityManager).merge(Mockito.any());
		
		Gson gson = new Gson();
		String json = gson.toJson(ratingDTO);
		this.mockMvc.perform(post("/crm/api/v1/customers/1/appointments/1/rating")
				 			.contentType(MediaType.APPLICATION_JSON)
				 			.content(json))
							.andExpect(status().isOk())
							.andExpect(jsonPath("$.id").value(expectedAppointment.getId()))
							.andExpect(jsonPath("$.description").value(expectedAppointment.getDescription()))
							.andExpect(jsonPath("$.dateAndTime").value(DateParserUtil.toString(expectedAppointment.getDate())))
							.andExpect(jsonPath("$.ratingDescription").value(expectedAppointment.getRatingDescription()))
							.andExpect(jsonPath("$.rating").value(expectedAppointment.getRating()));
	}
	
	@Test
	public void testRateAppointmentInvalidNonExistingCustomer() throws Exception {
		AppointmentRatingDTO ratingDTO = new AppointmentRatingDTO();
		ratingDTO.setRatingDescription("Rating description");
		ratingDTO.setRating(3);
		Appointment expectedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		expectedAppointment.setId(1);
		expectedAppointment.setRatingDescription(ratingDTO.getRatingDescription());
		expectedAppointment.setRating(ratingDTO.getRating());
		Appointment storedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		storedAppointment.setId(1);
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(null).when(entityManager).find(Customer.class, customer.getId());
		
		Gson gson = new Gson();
		String json = gson.toJson(ratingDTO);
		this.mockMvc.perform(post("/crm/api/v1/customers/1/appointments/1/rating")
				 			.contentType(MediaType.APPLICATION_JSON)
				 			.content(json))
							.andExpect(status().isBadRequest())
							.andExpect(jsonPath("$.messages", Matchers.hasSize(1)));
	}
	
	@Test
	public void testFindByIdNoResult() throws Exception {
		Mockito.doReturn(null).when(entityManager).find(Customer.class, 1);
		this.mockMvc.perform(get("/crm/api/v1/customers/1")).andExpect(status().isNotFound());
	}
	
	@Test
	public void testCreateSuccessful() throws Exception {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV1();
		expectedCustomer.setId(1);
		Mockito.doReturn(expectedCustomer).when(entityManager).merge(Mockito.any());
		Gson gson = new Gson();
		String json = gson.toJson(customer);
		this.mockMvc.perform(
	            post("/crm/api/v1/customers")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$.id").value(expectedCustomer.getId()))
	    		.andExpect(jsonPath("$.firstName").value(expectedCustomer.getFirstName()))
	    		.andExpect(jsonPath("$.surname").value(expectedCustomer.getSurname()))
	    		.andExpect(jsonPath("$.phone").value(expectedCustomer.getPhone()))
	    		.andExpect(jsonPath("$.email").value(expectedCustomer.getEmail()))
	    		.andExpect(jsonPath("$.address").value(expectedCustomer.getAddress()));
	}
	
	@Test
	public void testCreateWithEmptyField() throws Exception {
		Customer customer = new Customer();
		Gson gson = new Gson();
		String json = gson.toJson(customer);
		this.mockMvc.perform(
	            post("/crm/api/v1/customers")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	            .andExpect(status().isBadRequest())
	            .andExpect(jsonPath("$.messages", Matchers.hasSize(5)));
		
	}
	
	@Test
	public void testUpdateNonExistingCustomer() throws Exception {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV1();
		Mockito.doReturn(null).when(entityManager).find(Mockito.any(), Mockito.eq(1));
		customer.getAppointments().clear();
		Mockito.doReturn(expectedCustomer).when(entityManager).merge(Mockito.any());
		Gson gson = new Gson();
		String json = gson.toJson(customer);
		this.mockMvc.perform(
	             put("/crm/api/v1/customers/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	            .andExpect(status().isBadRequest())
	            .andExpect(jsonPath("$.messages", Matchers.hasSize(1)));
	}
	
	@Test
	public void testUpdateSuccessful() throws Exception {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV1();
		expectedCustomer.setId(1);
		Mockito.doReturn(expectedCustomer).when(entityManager).find(Mockito.any(), Mockito.eq(1));
		Mockito.doReturn(expectedCustomer).when(entityManager).merge(Mockito.any());
		Gson gson = new Gson();
		String json = gson.toJson(customer);
		this.mockMvc.perform(
	             put("/crm/api/v1/customers/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.id").value(expectedCustomer.getId()))
	    		.andExpect(jsonPath("$.firstName").value(expectedCustomer.getFirstName()))
	    		.andExpect(jsonPath("$.surname").value(expectedCustomer.getSurname()))
	    		.andExpect(jsonPath("$.phone").value(expectedCustomer.getPhone()))
	    		.andExpect(jsonPath("$.email").value(expectedCustomer.getEmail()))
	    		.andExpect(jsonPath("$.address").value(expectedCustomer.getAddress()));
	}
	
	@Test
	public void testPartialUpdateWithNonexistingCustomerId() throws Exception {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		Gson gson = new Gson();
		String json = gson.toJson(customer);
		Mockito.doReturn(null).when(entityManager).find(Mockito.any(), Mockito.any());
		this.mockMvc.perform(
	             patch("/crm/api/v1/customers/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	            .andExpect(status().isBadRequest())
	            .andExpect(jsonPath("$.messages", Matchers.hasSize(1)));
		
	}
	
	@Test
	public void testPartialUpdateSuccessful() throws Exception {
		Customer oldCustomer = getPopulatedCustomerWithoutIdV1();
		oldCustomer.setId(1);
		Customer partialCustomer = getPopulatedCustomerWithoutIdV2();
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV2();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName(partialCustomer.getFirstName());
		Mockito.doReturn(oldCustomer).when(entityManager).find(Mockito.any(), Mockito.any());
		Mockito.doReturn(expectedCustomer).when(entityManager).merge(Mockito.any());
		Gson gson = new Gson();
		String json = gson.toJson(partialCustomer);
		this.mockMvc.perform(
	             patch("/crm/api/v1/customers/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.id").value(expectedCustomer.getId()))
	    		.andExpect(jsonPath("$.firstName").value(expectedCustomer.getFirstName()))
	    		.andExpect(jsonPath("$.surname").value(expectedCustomer.getSurname()))
	    		.andExpect(jsonPath("$.phone").value(expectedCustomer.getPhone()))
	    		.andExpect(jsonPath("$.email").value(expectedCustomer.getEmail()))
	    		.andExpect(jsonPath("$.address").value(expectedCustomer.getAddress()));
		
	}
	
	@Test
	public void testDeleteWithNonexistingCustomerId() throws Exception {
		Customer customer = new Customer();
		Gson gson = new Gson();
		String json = gson.toJson(customer);
		Mockito.doReturn(null).when(entityManager).find(Mockito.any(), Mockito.any());
		MockHttpServletResponse response = this.mockMvc.perform(
	             delete("/crm/api/v1/customers/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(json))
	            .andExpect(status().isBadRequest())
	            .andReturn().getResponse();
		String responseJson = response.getContentAsString();
		ErrorDTO errorDTO = gson.fromJson(responseJson, ErrorDTO.class);
		Assert.assertEquals(1, errorDTO.getMessages().size());
		
	}
	
	@Test
	public void testDeleteSuccessful() throws Exception {
		Customer oldCustomer = getPopulatedCustomerWithoutIdV1();
		oldCustomer.setId(1);
		Mockito.doReturn(oldCustomer).when(entityManager).find(Mockito.any(), Mockito.any());
		this.mockMvc.perform(delete("/crm/api/v1/customers/1")).andExpect(status().isOk());
		
	}
}
