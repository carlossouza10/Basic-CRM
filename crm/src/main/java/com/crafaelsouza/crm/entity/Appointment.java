package com.crafaelsouza.crm.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * The Class Appointment.
 * 
 * @author Carlos Souza
 */
@Entity
@Table(name = "APPOINTMENT")
public class Appointment implements EntityModel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue
	@Column(name = "APPOINT_ID")
	private Integer id;
	
	/** The description. */
	@Column(name = "DESCRIPTION")
	@NotNull(message = "'description' cannot be null.")
	private String description;
	
	/** The customer. */
	@ManyToOne
	@JoinColumn(name = "CUSTOMER_ID")
	@NotNull(message = "'customer' cannot be null.")
	private Customer customer;
	
	/** The date. */
	@Column(name = "DATE")
	@NotNull(message = "'date' cannot be null.")
	private Date date;

	/** The rating. */
	@Column(name = "RATING")
	private Integer rating;
	
	/** The rating description. */
	@Column(name = "RATING_DESCRIPTION")
	private String ratingDescription;
	
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
	 * Gets the customer.
	 *
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * Sets the customer.
	 *
	 * @param customer the new customer
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Date date) {
		this.date = date;
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

}
