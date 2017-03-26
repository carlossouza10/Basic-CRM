package com.crafaelsouza.crm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * The Class AppointmentDTO.
 * 
 * @author Carlos Souza
 */
@JsonInclude(Include.NON_NULL)
public class AppointmentDTO implements ResponseDTO {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private Integer id;
	
	/** The description. */
	@ApiModelProperty(required= true, notes="Brief description of appointment.")
	private String description;
	
	/** The date and time. */
	@ApiModelProperty(required= true, notes="Date and time of the appointment. 'Format: dd/MM/yyyy HH:mm'.")
	private String dateAndTime;
	
	/** The rating description. */
	@ApiModelProperty(notes = "Rating description. Required when creating rating")
	private String ratingDescription;
	
	/** The rating. */
	@ApiModelProperty(notes = "Appointment rating which has to be between 0 and 5. Required when creating rating")
	private Integer rating;
	
	/** The customer id. */
	@ApiModelProperty(notes = "Customer ID which is going to be shown when searching on /appointment.")
	private Integer customerId;
	
	/** The customer first name. */
	@ApiModelProperty(notes = "Customer first name.")
	private String customerFirstName;
	
	/** The customer surname. */
	@ApiModelProperty(notes = "Customer Surname.")
	private String customerSurname;
	
	/**
	 * Instantiates a new appointment DTO.
	 */
	public AppointmentDTO() {
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the date and time.
	 *
	 * @return the date and time
	 */
	public String getDateAndTime() {
		return dateAndTime;
	}

	/**
	 * Sets the date and time.
	 *
	 * @param dateAndTime the new date and time
	 */
	public void setDateAndTime(String dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	/**
	 * Gets the rating description.
	 *
	 * @return the rating description
	 */
	public String getRatingDescription() {
		return ratingDescription;
	}

	/**
	 * Sets the rating description.
	 *
	 * @param ratingDescription the new rating description
	 */
	public void setRatingDescription(String ratingDescription) {
		this.ratingDescription = ratingDescription;
	}

	/**
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	public Integer getRating() {
		return rating;
	}

	/**
	 * Sets the rating.
	 *
	 * @param rating the new rating
	 */
	public void setRating(Integer rating) {
		this.rating = rating;
	}

	/**
	 * Gets the customer id.
	 *
	 * @return the customer id
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the customer id.
	 *
	 * @param customerId the new customer id
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * Gets the customer first name.
	 *
	 * @return the customer first name
	 */
	public String getCustomerFirstName() {
		return customerFirstName;
	}

	/**
	 * Sets the customer first name.
	 *
	 * @param customerFirstName the new customer first name
	 */
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	/**
	 * Gets the customer surname.
	 *
	 * @return the customer surname
	 */
	public String getCustomerSurname() {
		return customerSurname;
	}

	/**
	 * Sets the customer surname.
	 *
	 * @param customerSurname the new customer surname
	 */
	public void setCustomerSurname(String customerSurname) {
		this.customerSurname = customerSurname;
	}

}
