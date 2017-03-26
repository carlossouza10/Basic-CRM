package com.crafaelsouza.crm.dto;

import java.util.ArrayList;
import java.util.List;

import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * The Class ErrorDTO.
 * 
 * @author Carlos Souzas
 */
public class ErrorDTO implements ResponseDTO {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The messages. */
	@ApiModelProperty(required= true, notes="Error list.")
	private List<String> messages;

	/**
	 * Instantiates a new error DTO.
	 *
	 * @param message the message
	 */
	public ErrorDTO(String message) {
		messages = new ArrayList<String>();
		messages.add(message);
	}

	/**
	 * Instantiates a new error DTO.
	 *
	 * @param messages the messages
	 */
	public ErrorDTO(List<String> messages) {
		this.messages = messages;
	}
	
	/**
	 * Gets the messages.
	 *
	 * @return the messages
	 */
	public List<String> getMessages() {
		return messages;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ErrorDTO [messages=" + messages + "]";
	}

}
