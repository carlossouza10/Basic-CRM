package com.crafaelsouza.crm.test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.exception.CRMException;
import com.crafaelsouza.crm.repository.CustomerRepository;
import com.crafaelsouza.crm.repository.impl.AppointmentRepositoryImpl;
import com.crafaelsouza.crm.service.AppointmentService;
import com.crafaelsouza.crm.service.impl.AppointmentServiceImpl;

public class AppointmentServiceTest extends AbstractTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private EntityManager entityManagerCustomer;
	
	@Mock
	private CustomerRepository customerRepository;
	
	private AppointmentRepositoryImpl appointmentRepository;
	
	private AppointmentService appointmentService;
	
	private Validator validator;
	
	@Before
    public void setup() {
		MockitoAnnotations.initMocks(this);
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		appointmentRepository = new AppointmentRepositoryImpl(validator, entityManager);
    	appointmentService = new AppointmentServiceImpl(appointmentRepository, customerRepository);
    }
	
	@Test
	public void testPartialUpdateWithNoExistingAppointment() {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Mockito.doReturn(null).when(entityManager).find(Mockito.any(), Mockito.any());
		
		try {
			appointmentService.partialUpdate(appointment);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testPartialUpdateWithNoCustomerID() {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		appointment.getCustomer().setId(null);
		try {
			appointmentService.partialUpdate(appointment);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testPartialUpdateWithNonExistingCustomerID() {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		Mockito.doReturn(null).when(customerRepository).findById(Mockito.anyInt());
		try {
			appointmentService.partialUpdate(appointment);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testPartialUpdateWithCustomer() {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setCustomer(null);
		try {
			appointmentService.partialUpdate(appointment);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testPartialUpdateSuccessfulDescription() throws CRMException {
		Appointment oldAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		oldAppointment.setId(1);
		Appointment partialAppointment = new Appointment();
		partialAppointment.setCustomer(getPopulatedCustomerWithoutIdV1());
		partialAppointment.getCustomer().setId(1);
		partialAppointment.setId(1);
		partialAppointment.setDescription("New appointment description");
		Appointment expectedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		expectedAppointment.setId(1);
		expectedAppointment.setDescription(partialAppointment.getDescription());
		
		assertFieldsForPartialUpdate(partialAppointment, oldAppointment, expectedAppointment);
	}
	
	@Test
	public void testPartialUpdateSuccessfulDate() throws CRMException {
		Appointment oldAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		oldAppointment.setId(1);
		Appointment partialAppointment = new Appointment();
		partialAppointment.setCustomer(getPopulatedCustomerWithoutIdV1());
		partialAppointment.getCustomer().setId(1);
		partialAppointment.setId(1);
		LocalDate date = LocalDate.now();
		date.plusWeeks(2);
		partialAppointment.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		Appointment expectedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		expectedAppointment.setId(1);
		expectedAppointment.setDate(partialAppointment.getDate());
		
		assertFieldsForPartialUpdate(partialAppointment, oldAppointment, expectedAppointment);
	}
	
	@Test
	public void testPartialUpdateSuccessfulDescriptionRating() throws CRMException {
		Appointment oldAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		oldAppointment.setId(1);
		Appointment partialAppointment = new Appointment();
		partialAppointment.setCustomer(getPopulatedCustomerWithoutIdV1());
		partialAppointment.getCustomer().setId(1);
		partialAppointment.setId(1);
		partialAppointment.setRatingDescription("New description rating");
		Appointment expectedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		expectedAppointment.setId(1);
		expectedAppointment.setRatingDescription(partialAppointment.getRatingDescription());
		
		assertFieldsForPartialUpdate(partialAppointment, oldAppointment, expectedAppointment);
	}
	
	
	@Test
	public void testPartialUpdateSuccessfulRating() throws CRMException {
		Appointment oldAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		oldAppointment.setDate(getPreviousDay());
		oldAppointment.setId(1);
		Appointment partialAppointment = new Appointment();
		partialAppointment.setCustomer(getPopulatedCustomerWithoutIdV2());
		partialAppointment.getCustomer().setId(1);
		partialAppointment.setId(1);
		partialAppointment.setRating(3);
		Appointment expectedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		expectedAppointment.setDate(getPreviousDay());
		expectedAppointment.setId(1);
		expectedAppointment.setCustomer(partialAppointment.getCustomer());
		expectedAppointment.setRating(partialAppointment.getRating());
		
		assertFieldsForPartialUpdate(partialAppointment, oldAppointment, expectedAppointment);
	}
	
	@Test
	public void testFindById() throws CRMException {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Mockito.doReturn(appointment).when(entityManager).find(Appointment.class, 1);
		Appointment appointmentFound = appointmentService.findById(1);
		Assert.assertEquals(appointment.getId(), appointmentFound.getId());
	}
	
	@Test
	public void testFindNextWeeks() throws CRMException {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Appointment appointment2 = getPopulatedAppointmentWithoutIdAndRatingV2();
		appointment2.setId(2);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment, appointment2)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		
		List<Appointment> appointments = appointmentService.findNextWeeks(1);
		Assert.assertEquals(2, appointments.size());
	}
	
	
	@Test
	public void testFindAll() throws CRMException {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Appointment appointment2 = getPopulatedAppointmentWithoutIdAndRatingV2();
		appointment.setId(2);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment, appointment2)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"), Mockito.any());
		List<Appointment> appointments = appointmentService.findAll();
		Assert.assertEquals(2, appointments.size());
	}

	@Test
	public void testFindByIdAndCustomer() {
	}
	
	private void assertFieldsForPartialUpdate(Appointment partialAppointment, Appointment oldAppointment, Appointment expectedAppointment) throws CRMException {
		oldAppointment.setDate(getPreviousDay());
		expectedAppointment.setDate(getPreviousDay());
		Mockito.doReturn(oldAppointment).when(entityManager).find(Mockito.eq(Appointment.class), Mockito.any());
		Mockito.doReturn(expectedAppointment).when(entityManager).merge(Mockito.any());
		Mockito.doReturn(expectedAppointment.getCustomer()).when(customerRepository).findById(Mockito.anyInt());
		Appointment updatedAppointment = appointmentService.partialUpdate(partialAppointment);
		
		Assert.assertEquals(expectedAppointment.getId(), updatedAppointment.getId());
		Assert.assertEquals(expectedAppointment.getDescription(), updatedAppointment.getDescription());
		Assert.assertEquals(expectedAppointment.getDate(), updatedAppointment.getDate());
		Assert.assertEquals(expectedAppointment.getCustomer(), updatedAppointment.getCustomer());
		Assert.assertEquals(expectedAppointment.getRating(), updatedAppointment.getRating());
		Assert.assertEquals(expectedAppointment.getRatingDescription(), updatedAppointment.getRatingDescription());
	}
	
	private void assertExistingErrorMessages(CRMException e, int amoutErrorExpected) {
		Assert.assertNotNull(e.getError());
		Assert.assertNotNull(e.getError().getMessages());
		Assert.assertEquals(amoutErrorExpected, e.getError().getMessages().size());
	}
}
