package com.crafaelsouza.crm.test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import com.crafaelsouza.crm.entity.Appointment;
import com.crafaelsouza.crm.entity.Customer;

public abstract class AbstractTest {

	protected Customer getPopulatedCustomerWithoutIdV1() {
		Customer customer = new Customer();
		customer.setAppointments(new ArrayList<Appointment>());
		customer.setFirstName("James");
		customer.setSurname("Silva");
		customer.setEmail("james@silva.com");
		customer.setPhone("1234566");
		customer.setAddress("164, East wall Road, Dublin, Ireland");
		return customer;
	}
	
	protected Customer getPopulatedCustomerWithoutIdV2() {
		Customer customer = new Customer();
		customer.setAppointments(new ArrayList<Appointment>());
		customer.setFirstName("Joseph");
		customer.setSurname("Galway");
		customer.setEmail("joseph@galway.com");
		customer.setPhone("99876654532");
		customer.setAddress("64, Charles Stret Great, Dublin, Ireland");
		return customer;
	}
	
	protected Appointment getPopulatedAppointmentWithoutIdAndRatingV1() {
		Appointment appointment = new Appointment();
		Customer customer = getPopulatedCustomerWithoutIdV1();
		customer.setId(1);
		appointment.setDate(getNextDay());
		appointment.setCustomer(customer);
		appointment.setDescription("Appointment Descriptin V1");
		return appointment;
	}
	
	protected Appointment getPopulatedAppointmentWithoutIdAndRatingV2() {
		Appointment appointment = new Appointment();
		Customer customer = getPopulatedCustomerWithoutIdV2();
		customer.setId(2);
		appointment.setDate(getNextDay());
		appointment.setCustomer(customer);
		appointment.setDescription("Appointment Descriptin V2");
		return appointment;
	}
	
	protected Date getPreviousDay() {
		LocalDate localDate = LocalDate.now();
		localDate = localDate.minusDays(1);
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}
	
	protected Date getNextDay() {
		LocalDate localDate = LocalDate.now();
		localDate = localDate.plusDays(1);
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return date;
	}
}
