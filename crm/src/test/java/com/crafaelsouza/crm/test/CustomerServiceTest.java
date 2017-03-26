package com.crafaelsouza.crm.test;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.crafaelsouza.crm.entity.Customer;
import com.crafaelsouza.crm.exception.CRMException;
import com.crafaelsouza.crm.repository.AppointmentRepository;
import com.crafaelsouza.crm.repository.impl.AppointmentRepositoryImpl;
import com.crafaelsouza.crm.repository.impl.CustomerRepositoryImpl;
import com.crafaelsouza.crm.service.CustomerService;
import com.crafaelsouza.crm.service.impl.CustomerServiceImpl;

public class CustomerServiceTest extends AbstractTest {

	@Mock
	private EntityManager entityManager;

	private CustomerRepositoryImpl customerRepository;
	
	private AppointmentRepository appointmentRepository;
	
	private CustomerService customerService;
	
	private Validator validator;
	
	@Before
    public void setup() {
		MockitoAnnotations.initMocks(this);
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		customerRepository = new CustomerRepositoryImpl(validator, entityManager);
		appointmentRepository = new AppointmentRepositoryImpl(validator, entityManager);
    	customerService = new CustomerServiceImpl(customerRepository, appointmentRepository);
    }
	
	@Test
	public void testSaveWithEmptyFields() throws CRMException {
		Customer customer = new Customer();
		try {
			customerService.save(customer);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 5);
		}
	}
	
	@Test
	public void testSaveWithExistingID() {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		try {
			customerService.save(customer);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testSaveSuccessful() throws CRMException {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		Mockito.doReturn(customer).when(entityManager).merge(Mockito.any());
		Customer customerSaved = customerService.save(customer);
		Assert.assertNotNull(customerSaved);
	}
	
	@Test
	public void testUpdateEmptyFields() {
		Customer customer = new Customer();
		try {
			customerService.update(customer);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 5);
		}
	}
	
	@Test
	public void testUpdateWithoutId() {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		try {
			customerService.update(customer);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testUpdateWithNoExistingCustomer() {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(null).when(entityManager).find(Mockito.any(), Mockito.any());
		try {
			customerService.update(customer);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testUpdateSuccessful() throws CRMException {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Mockito.any(), Mockito.any());
		Mockito.doReturn(customer).when(entityManager).merge(Mockito.any());
		Customer updatedCustomer = customerService.update(customer);
		Assert.assertEquals(customer.getId(), updatedCustomer.getId());
	}
	
	@Test
	public void testPartialUpdateWithoutId() {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		try {
			customerService.update(customer);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testPartialUpdateWithNoExistingCustomer() {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(null).when(entityManager).find(Mockito.any(), Mockito.any());
		
		try {
			customerService.partialUpdate(customer);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testPartialUpdateSuccessfulFirstName() throws CRMException {
		Customer oldCustomer = getPopulatedCustomerWithoutIdV1();
		oldCustomer.setId(1);
		Customer partialCustomer = new Customer();
		partialCustomer.setId(1);
		partialCustomer.setFirstName("Christian");
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV1();
		expectedCustomer.setId(1);
		expectedCustomer.setFirstName(partialCustomer.getFirstName());
		
		assertFieldsForPartialUpdate(partialCustomer, oldCustomer, expectedCustomer);
	}
	
	@Test
	public void testPartialUpdateSuccessfulSurname() throws CRMException {
		Customer oldCustomer = getPopulatedCustomerWithoutIdV1();
		oldCustomer.setId(1);
		Customer partialCustomer = new Customer();
		partialCustomer.setId(1);
		partialCustomer.setSurname("McConway");
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV1();
		expectedCustomer.setId(1);
		expectedCustomer.setSurname(partialCustomer.getSurname());
		
		assertFieldsForPartialUpdate(partialCustomer, oldCustomer, expectedCustomer);
	}
	
	@Test
	public void testPartialUpdateSuccessfulPhone() throws CRMException {
		Customer oldCustomer = getPopulatedCustomerWithoutIdV1();
		oldCustomer.setId(1);
		Customer partialCustomer = new Customer();
		partialCustomer.setId(1);
		partialCustomer.setPhone("0000123456789");
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV1();
		expectedCustomer.setId(1);
		expectedCustomer.setPhone(partialCustomer.getPhone());
		
		assertFieldsForPartialUpdate(partialCustomer, oldCustomer, expectedCustomer);
	}
	
	@Test
	public void testPartialUpdateSuccessfulEmail() throws CRMException {
		Customer oldCustomer = getPopulatedCustomerWithoutIdV1();
		oldCustomer.setId(1);
		Customer partialCustomer = new Customer();
		partialCustomer.setId(1);
		partialCustomer.setEmail("0000123456789");
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV1();
		expectedCustomer.setId(1);
		expectedCustomer.setEmail(partialCustomer.getEmail());
		
		assertFieldsForPartialUpdate(partialCustomer, oldCustomer, expectedCustomer);
	}
	
	@Test
	public void testPartialUpdateSuccessfulAddress() throws CRMException {
		Customer oldCustomer = getPopulatedCustomerWithoutIdV1();
		oldCustomer.setId(1);
		Customer partialCustomer = new Customer();
		partialCustomer.setId(1);
		partialCustomer.setAddress("0000123456789");
		Customer expectedCustomer = getPopulatedCustomerWithoutIdV1();
		expectedCustomer.setId(1);
		expectedCustomer.setAddress(partialCustomer.getAddress());
		
		assertFieldsForPartialUpdate(partialCustomer, oldCustomer, expectedCustomer);
	}
	
	@Test
	public void testDeleteWithNoExistingCustomer() {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(null).when(entityManager).find(Mockito.any(), Mockito.any());
		
		try {
			customerService.delete(1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}

	@Test
	public void testDeleteSuccessful() throws CRMException {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Mockito.any(), Mockito.any());
		customerService.delete(1);
	}
	
	@Test
	public void testFindById() throws CRMException {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, 1);
		Customer customerFound = customerService.findById(1);
		Assert.assertEquals(customer.getId(), customerFound.getId());
	}
	
	@Test
	public void testFindNextAppointmentSuccessful() throws CRMException {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Appointment appointment1 = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment1.setId(1);
		Appointment appointment2 = getPopulatedAppointmentWithoutIdAndRatingV2();
		appointment2.setId(2);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment1, appointment2)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Appointment nextAppointment = customerService.findNextAppointment(customer.getId());
		Assert.assertEquals(nextAppointment.getId(), appointment1.getId());
	}
	
	@Test
	public void testFindNextAppointmentNoResults() throws CRMException {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(new ArrayList<Appointment>()).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Appointment nextAppointment = customerService.findNextAppointment(customer.getId());
		Assert.assertNull(nextAppointment);
	}
	
	@Test
	public void testFindAppointmentByCustomerId() throws CRMException {
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(getPopulatedAppointmentWithoutIdAndRatingV1())).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		Appointment appointment = customerService.findAppointmentByIdAndCustomerId(1, 2);
		Assert.assertNotNull(appointment);
	}
	
	@Test
	public void testRateAppointmentPastDate() throws CRMException {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRatingDescription("Rating description");
		appointment.setRating(3);
		Appointment storedAppoint = getPopulatedAppointmentWithoutIdAndRatingV1();
		storedAppoint.setDate(getPreviousDay());
		storedAppoint.setId(1);
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		appointment.setCustomer(customer);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(storedAppoint).when(entityManager).find(Appointment.class, storedAppoint.getId());
		customerService.rateAppointment(appointment, 1);
	}
	
	@Test
	public void testRateAppointmentNonExistingCustomer() {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRatingDescription("Rating description");
		appointment.setRating(3);
		Mockito.doReturn(null).when(entityManager).find(Customer.class, 1);
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	

	@Test
	public void testRateAppointmentRatingGreaterThanMaximum() {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRatingDescription("Rating description");
		appointment.setRating(6);
		Appointment storedAppoint = getPopulatedAppointmentWithoutIdAndRatingV1();
		storedAppoint.setId(1);
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		appointment.setCustomer(customer);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(storedAppoint).when(entityManager).find(Appointment.class, storedAppoint.getId());
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testRateAppointmentFutureDate() {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRatingDescription("Rating description");
		appointment.setRating(3);
		Appointment storedAppoint = getPopulatedAppointmentWithoutIdAndRatingV1();
		storedAppoint.setDate(getNextDay());
		storedAppoint.setId(1);
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		appointment.setCustomer(customer);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(storedAppoint).when(entityManager).find(Appointment.class, storedAppoint.getId());
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testRateAppointmentRatingLessThanMinimum() {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRatingDescription("Rating description");
		appointment.setRating(0);
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Appointment storedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		storedAppointment.setId(1);
		appointment.setCustomer(customer);
		storedAppointment.setCustomer(customer);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(storedAppointment).when(entityManager).find(Appointment.class, appointment.getId());
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	
	@Test
	public void testRateAppointmentRatingOnlyDescription() {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRatingDescription("Rating description");
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testRateAppointmentRatingOnlyRating() {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRating(3);
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testRateAppointmentNonExistingAppointment() {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		
		Customer customerWrong = getPopulatedCustomerWithoutIdV2();
		customerWrong.setId(1);
		
		Mockito.doReturn(null).when(entityManager).find(Appointment.class, appointment.getId());
		Mockito.doReturn(customerWrong).when(entityManager).find(Customer.class, appointment.getCustomer().getId());
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testRateAppointmentWrongCustomer() {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		appointment.getCustomer().setId(3);
		
		Customer customerWrong = getPopulatedCustomerWithoutIdV2();
		customerWrong.setId(2);
		
		Mockito.doReturn(appointment).when(entityManager).find(Mockito.eq(Appointment.class), Mockito.anyInt());
		Mockito.doReturn(customerWrong).when(entityManager).find(Mockito.eq(Customer.class), Mockito.anyInt());
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
	}
	
	@Test
	public void testRateAppointmentWithEmptyFields() {
		Appointment appointment = new Appointment();
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setAppointments(null);
		customer.setId(1);
		appointment.setCustomer(customer);
		
		Mockito.doReturn(appointment).when(entityManager).find(Appointment.class, appointment.getId());
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		try {
			customerService.rateAppointment(appointment, 1);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 2);
		}
	}
	
	@Test
	public void testRateAppointmentSuccessfulMinRating() throws CRMException {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRatingDescription("Rating description");
		appointment.setRating(1);
		Appointment storedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		storedAppointment.setId(1);
		storedAppointment.setDate(getPreviousDay());
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(storedAppointment).when(entityManager).find(Appointment.class, appointment.getId());
		Mockito.doReturn(appointment).when(entityManager).merge(Mockito.any());
		Appointment rateAppointment = customerService.rateAppointment(appointment, 1);
		Assert.assertEquals(appointment.getDescription(), rateAppointment.getDescription());
		Assert.assertEquals(appointment.getRating(), rateAppointment.getRating());
		
	}
	
	@Test
	public void testCreateAppointmentWithDateBeforeToday() throws CRMException {
		Appointment appointment = new Appointment();
		appointment.setDescription("Description");
		appointment.setDate(getPreviousDay());
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(appointment).when(entityManager).merge(Mockito.any());
		
		try {
			customerService.createAppointment(1, appointment);
			Assert.fail();
		} catch (CRMException e) {
			assertExistingErrorMessages(e, 1);
		}
		
	}
	
	@Test
	public void testCreateAppointmentSuccessful() throws CRMException {
		Appointment appointment = new Appointment();
		appointment.setDescription("Description");
		appointment.setDate(getNextDay());
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(appointment).when(entityManager).merge(Mockito.any());
		Appointment rateAppointment = customerService.createAppointment(1, appointment);
		Assert.assertEquals(appointment.getDescription(), rateAppointment.getDescription());
		Assert.assertEquals(appointment.getRating(), rateAppointment.getRating());
		
	}
	
	@Test
	public void testFindAllAppointmentsByCustomerId() throws CRMException {
		Appointment appointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		appointment.setId(1);
		Appointment appointment2 = getPopulatedAppointmentWithoutIdAndRatingV2();
		appointment.setId(2);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(appointment, appointment2)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Appointment"));
		List<Appointment> appointments = customerService.findAllAppointmentsByCustomer(1);
		Assert.assertEquals(2, appointments.size());
	}
	
	@Test
	public void testRateAppointmentSuccessfulMaxRating() throws CRMException {
		Appointment appointment = new Appointment();
		appointment.setId(1);
		appointment.setRatingDescription("Rating description");
		appointment.setRating(5);
		Appointment storedAppointment = getPopulatedAppointmentWithoutIdAndRatingV1();
		storedAppointment.setId(1);
		storedAppointment.setDate(getPreviousDay());
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Mockito.doReturn(customer).when(entityManager).find(Customer.class, customer.getId());
		Mockito.doReturn(storedAppointment).when(entityManager).find(Appointment.class, appointment.getId());
		Mockito.doReturn(appointment).when(entityManager).merge(Mockito.any());
		Appointment rateAppointment = customerService.rateAppointment(appointment, 1);
		Assert.assertEquals(appointment.getDescription(), rateAppointment.getDescription());
		Assert.assertEquals(appointment.getRating(), rateAppointment.getRating());
		
	}
	
	@Test
	public void testFindAll() throws CRMException {
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		Customer customer2 = getPopulatedCustomerWithoutIdV2();
		customer.setId(2);
		@SuppressWarnings("rawtypes")
		TypedQuery query = Mockito.mock(TypedQuery.class);
		Mockito.doReturn(Arrays.asList(customer, customer2)).when(query).getResultList();
		Mockito.doReturn(query).when(entityManager).createQuery(Mockito.contains("Customer"), Mockito.any());
		List<Customer> customers = customerService.findAll();
		Assert.assertEquals(2, customers.size());
	}

	private void assertFieldsForPartialUpdate(Customer partialCustomer, Customer oldCustomer, Customer expectedCustomer) throws CRMException {
		Mockito.doReturn(oldCustomer).when(entityManager).find(Mockito.any(), Mockito.any());
		Mockito.doReturn(expectedCustomer).when(entityManager).merge(Mockito.any());
		Customer updatedCustomer = customerService.partialUpdate(partialCustomer);
		Assert.assertEquals(expectedCustomer.getId(), updatedCustomer.getId());
		Assert.assertEquals(expectedCustomer.getFirstName(), updatedCustomer.getFirstName());
		Assert.assertEquals(expectedCustomer.getSurname(), updatedCustomer.getSurname());
		Assert.assertEquals(expectedCustomer.getEmail(), updatedCustomer.getEmail());
		Assert.assertEquals(expectedCustomer.getPhone(), updatedCustomer.getPhone());
		Assert.assertEquals(expectedCustomer.getAddress(), updatedCustomer.getAddress());
	}
	
	private void assertExistingErrorMessages(CRMException e, int amoutErrorExpected) {
		Assert.assertNotNull(e.getError());
		Assert.assertNotNull(e.getError().getMessages());
		Assert.assertEquals(amoutErrorExpected, e.getError().getMessages().size());
	}
	
}
