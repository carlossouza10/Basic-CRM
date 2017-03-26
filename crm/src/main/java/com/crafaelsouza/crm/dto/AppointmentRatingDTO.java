package com.crafaelsouza.crm.dto;

import com.wordnik.swagger.annotations.ApiModelProperty;


/**
 * The Class AppointmentRatingDTO.
 * 
 * @author Carlos Souza
 */
public class AppointmentRatingDTO implements ResponseDTO {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The rating description. */
	@ApiModelProperty(required= true, notes="Brief description of appointment.")
	private String ratingDescription;
	
	/** The rating. */
	@ApiModelProperty(required= true, notes = "Appointment rating which has to be between 0 and 5.")
	private Integer rating;
	
	/**
	 * Instantiates a new appointment rating DTO.
	 */
	public AppointmentRatingDTO() {
		super();
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

}
