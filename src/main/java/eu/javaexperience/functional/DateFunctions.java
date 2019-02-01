package eu.javaexperience.functional;

import java.util.Date;

import eu.javaexperience.text.Format.strtotime;

public class DateFunctions
{
	public static Date now()
	{
		return new Date();
	}
	
	public static Date parseDate(String date)
	{
		return strtotime.strtotime(date);
	}
}
