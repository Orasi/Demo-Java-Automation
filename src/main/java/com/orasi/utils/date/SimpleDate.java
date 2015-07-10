package com.orasi.utils.date;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author 
 */
public class SimpleDate {

    public static final String MONTHS[] = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
    
    private Calendar calendar = Calendar.getInstance();
    private int month = calendar.get(Calendar.MONTH);
    private int day = calendar.get(Calendar.DATE);
    private int year = calendar.get(Calendar.YEAR);

    public SimpleDate() {
    }

    public Calendar getCalendar() {
        return calendar;
    }
    
    public static Timestamp getTimestamp(){
   	return new Timestamp(new java.util.Date().getTime());
       }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
        month = this.calendar.get(Calendar.MONTH);
        day = this.calendar.get(Calendar.DATE);
        year = this.calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
        calendar.set(Calendar.MONTH, month);
    }
    
    public int getDay() {
        return day;
    }
    
    public void setDay(int day) {
        this.day = day;
        calendar.set(Calendar.DATE, day);
    }
    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        calendar.set(Calendar.YEAR, year);
    }
    
    public SimpleDate advanceDay(int days) {
        calendar.add(Calendar.DATE, days);
        day = calendar.get(Calendar.DATE);
        return this;
    }

    public SimpleDate advanceMonth(int months) {
        calendar.add(Calendar.MONTH, months);
        month = calendar.get(Calendar.MONTH);
        return this;
    }

    public SimpleDate advanceYear(int years) {
        calendar.add(Calendar.YEAR, years);
        year = calendar.get(Calendar.YEAR);
        return this;
    }
    
    public String getMonthString() {
        return MONTHS[month];
    }
    
    public String toString(String format) {
        SimpleDateFormat simpleDate = new SimpleDateFormat(format);
        Date date = calendar.getTime();
        String dateString = simpleDate.format(date);
        return dateString;
    }
    
    public boolean isBefore(SimpleDate date) {
        return calendar.before(date);
    }
    
    public boolean isAfter(SimpleDate date) {
        return calendar.after(date);
    }
    
    public int compareTo(SimpleDate date) {
        return calendar.compareTo(date.getCalendar());
    }
    
    public int compareCalendarTo(Calendar calendar) {
        return this.calendar.compareTo(calendar);
    }
    
    public static Map<TimeUnit,Long> computeDiff(Date date1, Date date2) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
	    Collections.reverse(units);

	    Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
	    long milliesRest = diffInMillies;
	    for ( TimeUnit unit : units ) {
	        long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
	        long diffInMilliesForUnit = unit.toMillis(diff);
	        milliesRest = milliesRest - diffInMilliesForUnit;
	        result.put(unit,diff);
	    }
	    return result;
	}
    
    public static Map<TimeUnit,Long> computeDiff(String date1, Date date2) {
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    	Map<TimeUnit,Long> result = null;
    	try {
    		result = computeDiff(format.parse(date1), date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
	}
}
