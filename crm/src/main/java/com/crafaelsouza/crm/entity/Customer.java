package com.crafaelsouza.crm.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The Class Customer.
 * 
 * @author Carlos Souza
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer implements EntityModel{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "CUSTOMER_ID")
	private Integer id;
	
	/** The first name. */
	@Column(name = "FIRST_NAME", nullable=false)
	@NotNull(message = "'firstName' cannot be null.")
    private String firstName;

	/** The surname. */
	@Column(name = "SURNAME", nullable=false)
	@NotNull(message = "'surname' cannot be null.")
    private String surname;
	
	/** The phone. */
	@Column(name = "PHONE", nullable=false)
	@NotNull(message = "'phone' cannot be null.")
	private String phone;
	
	/** The email. */
	@Column(name = "EMAIL", nullable=false)
	@NotNull(message = "'email' cannot be null.")
	private String email;
	
	/** The address. */
	@Column(name = "ADDRESS")
	@NotNull(message = "'address' cannot be null.")
	private String address;
	
	/**
	 * Instantiates a new customer.
	 */
	public Customer() {
		super();
	}

	/** The appointments. */
	@OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Appointment> appointments = new ArrayList<Appointment>();

	/* (non-Javadoc)
	 * @see com.crafaelsouza.crm.entity.EntityModel#getId()
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the surname.
	 *
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * Sets the surname.
	 *
	 * @param surname the new surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * Gets the phone.
	 *
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the phone.
	 *
	 * @param phone the new phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the appointments.
	 *
	 * @return the appointments
	 */
	public List<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * Sets the appointments.
	 *
	 * @param appointments the new appointments
	 */
	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

}
