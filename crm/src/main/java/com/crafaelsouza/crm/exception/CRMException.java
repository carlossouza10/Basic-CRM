package com.crafaelsouza.crm.exception;

import com.crafaelsouza.crm.dto.ErrorDTO;

/**
 * The Class CRMException.
 * 
 * @author Carlos Souza
 */
public class CRMException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The error. */
	private ErrorDTO error;

	/**
	 * Instantiates a new CRM exception.
	 *
	 * @param errorDTO the error DTO
	 */
	public CRMException(ErrorDTO errorDTO) {
		super();
		error = errorDTO;
	}

	/**
	 * Gets the error.
	 *
	 * @return the error
	 */
	public ErrorDTO getError() {
		return error;
	}
	
}
