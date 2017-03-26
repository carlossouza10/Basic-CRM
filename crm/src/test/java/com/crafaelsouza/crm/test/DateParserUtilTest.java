package com.crafaelsouza.crm.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.crafaelsouza.crm.exception.CRMException;
import com.crafaelsouza.crm.util.DateParserUtil;

public class DateParserUtilTest {

	
	@Test
	public void testToString() {
		LocalDate of = LocalDate.of(2016, 12, 10);
		LocalDateTime atTime = of.atTime(13, 15);
		Date date = Date.from(atTime.atZone(ZoneId.systemDefault()).toInstant());
		String expected = "10/12/2016 13:15";
		@SuppressWarnings("static-access")
		String dateAsString = new DateParserUtil().toString(date);
		Assert.assertEquals(expected, dateAsString);
	}
	
	@Test
	public void testToToDate() throws CRMException {
		String dateAsString = "10/12/2016 13:15";
		Date date = DateParserUtil.toDate(dateAsString);
		Assert.assertNotNull(date);
	}
	
	@Test(expected = CRMException.class)
	public void testToToDateInvalid() throws CRMException {
		String dateAsString = "10122016 13:15";
		Date date = DateParserUtil.toDate(dateAsString);
		Assert.assertNotNull(date);
	}
	
	@Test
	public void testToToDateNull() throws CRMException {
		Date date = DateParserUtil.toDate(null);
		Assert.assertNull(date);
	}
	
	@Test
	public void testToToStringNull() {
		String dateAsString = DateParserUtil.toString(null);
		Assert.assertNull(dateAsString);
	}
}
