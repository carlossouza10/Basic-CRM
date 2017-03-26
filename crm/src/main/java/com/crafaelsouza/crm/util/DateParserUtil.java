package com.crafaelsouza.crm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.crafaelsouza.crm.dto.ErrorDTO;
import com.crafaelsouza.crm.exception.CRMException;

/**
 * The Class DateParserUtil responsible for converting date/string string/date.
 * 
 * @author Carlos Souza
 */
public class DateParserUtil {
	
	/** The Constant DEFAULT_DATE_FORMAT. */
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
	
	/**
	 * To date.
	 *
	 * @param dateAsString the date as string
	 * @return the date
	 * @throws CRMException the crm exception
	 */
	public static Date toDate(String dateAsString) throws CRMException {
		return toDate(dateAsString, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * To string.
	 *
	 * @param date the date
	 * @return the string
	 */
	public static String toString(Date date) {
		return toString(date, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * To date.
	 *
	 * @param dateAsString the date as string
	 * @param format the format
	 * @return the date
	 * @throws CRMException the crm exception
	 */
	public static Date toDate(String dateAsString, String format) throws CRMException {
		Date date = null;
		if (dateAsString != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			try {
				date = formatter.parse(dateAsString);
			} catch (ParseException e) {
				throw new CRMException(
						new ErrorDTO("Date '" + dateAsString + "' is not in the correct format: " + DEFAULT_DATE_FORMAT));
			}
		}
		return date;
	}
	
	
	/**
	 * To string.
	 *
	 * @param date the date
	 * @param format the format
	 * @return the string
	 */
	public static String toString(Date date, String format) {
		String dateAsString = null;
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			dateAsString = formatter.format(date);
		}
		return dateAsString;
	}
}
