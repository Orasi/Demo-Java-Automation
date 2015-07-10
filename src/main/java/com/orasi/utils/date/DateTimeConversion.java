package com.orasi.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeConversion {
	
	public static String convert(String date, String fromFormat, String toFormat){
		SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat,Locale.ENGLISH);
		Date parsedDate = null;
		try {
		    parsedDate = dateFormat.parse(date);
		} catch (ParseException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		
		return convert(parsedDate,toFormat);
	}
	
	public static String convert(Date date, String toFormat){
	    SimpleDateFormat dateFormat = new SimpleDateFormat(toFormat, Locale.ENGLISH);
	    return dateFormat.format(date);
	}
	
	/**
	 * 
	 * @param daysOut - Number of days from current date to get date for
	 * @param format - Desired format of date. Samples are "MM/dd/yyyy", "MMMM dd, yyyy", and "dd-MM-yy" 
	 * @return String future date in desired format
	 */
	public static String getDaysOut(String daysOut, String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate=dateFormat.format(cal.getTime());    
		
		return convertedDate;
	}
	
	/**
	 * Marked for Deprecation 6/1/2015 <br>
	 * New method {@link #getDaysOut(String, String) getDaysOut} <br>
	 * Sample: {@code getDaysOut(5,"MM/dd/yyyy")} 
	 */
	@Deprecated
	public static String ConvertToDate(String daysOut){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate=dateFormat.format(cal.getTime());    
		
		return convertedDate;
	}
	
	/**
	 * Marked for Deprecation 6/1/2015 <br>
	 * New method {@link #getDaysOut(String, String) getDaysOut} <br>
	 * Sample: {@code getDaysOut(5,"MM/dd/yyyy")} 
	 */
	@Deprecated
	public String ConvertToDateMMDDYY(String daysOut){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate=dateFormat.format(cal.getTime());    
		
		return convertedDate;
	}
	
	/**
	 * Marked for Deprecation 6/1/2015 <br>
	 * New methods: <br>
	 * {@link #convert(String date, String fromFormat, String toFormat)   } <br>
	 * {@link #convert(Date date, String toFormat) }<br>
	 * Sample: {@code convert("06/01/2015", "MM/dd/yyyy", "MMMM dd, yyyy)} 
	 */
	@Deprecated
	public static String format(String date, String format){
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * Marked for Deprecation 6/1/2015 <br>
	 * New method {@link #getDaysOut(String, String) getDaysOut} <br>
	 * Sample: {@code getDaysOut(5,"yyyy-MM-dd")} 
	 */
	@Deprecated
	public String ConvertToDateYYYYMMDD(String daysOut){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate=dateFormat.format(cal.getTime());    
		
		return convertedDate;
	}
}
