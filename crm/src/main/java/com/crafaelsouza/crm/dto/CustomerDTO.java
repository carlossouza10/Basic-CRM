package com.crafaelsouza.crm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * The Class CustomerDTO.
 * 
 * @author Carlos Souza
 */
@JsonInclude(Include.NON_NULL)
public class CustomerDTO implements ResponseDTO {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@ApiModelProperty(notes="Customer Id.")
	private Integer id;
    
    /** The first name. */
	@ApiModelProperty(required= true, notes="Customer first's name.")
    private String firstName;
    
    /** The surname. */
	@ApiModelProperty(required= true, notes="Customer's surname.")
    private String surname;
	
	/** The phone. */
	@ApiModelProperty(required= true, notes="Customer's phone.")
	private String phone;
	
	/** The email. */
	@ApiModelProperty(required= true, notes="Customer's email.")
	private String email;
	
	/** The address. */
	@ApiModelProperty(required= true, notes="Customer's address.")
	private String address;
	
	/**
	 * Instantiates a new customer DTO.
	 */
	public CustomerDTO() {
		super();
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
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
	 * @param fistName the new first name
	 */
	public void setFirstName(String fistName) {
		this.firstName = fistName;
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
	
}
