package eu.javaexperience.time;


import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import eu.javaexperience.generic.SimpleCalculationMemorizer;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;
import eu.javaexperience.math.MathTools;
import eu.javaexperience.text.StringTools;

public class TimeCalc
{
	private static final SimpleCalculationMemorizer<LongInterval, Integer> yearSummerRange = new SimpleCalculationMemorizer<>(new GetBy1<LongInterval, Integer>()
	{
		@Override
		public LongInterval getBy(Integer a)
		{
			Calendar start = cal();
			start.set(Calendar.YEAR, a);
			start.set(Calendar.MONTH, 2);
			start.set(Calendar.DAY_OF_MONTH, 31);
			start.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			start.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
			start.set(Calendar.HOUR_OF_DAY, 2);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);

			long s = start.getTimeInMillis();
			
			Calendar end = start;
			end.set(Calendar.YEAR, a);
			end.set(Calendar.MONTH, 9);
			end.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			end.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
			end.set(Calendar.HOUR_OF_DAY, 3);
			end.set(Calendar.MINUTE, 0);
			end.set(Calendar.SECOND, 0);
			end.set(Calendar.MILLISECOND, 0);
			
			return new LongInterval(s, end.getTimeInMillis());
		}
	});
	
	
	//http://wiki.ham.hu/index.php/UTC
	public static boolean isDateInSummer(Date d)
	{
		LongInterval in = yearSummerRange.calcuate(getYearOfDate(d));
		return in.isBetween(d.getTime());
	}
	
	//Magyarországon télen +1 óra eltérés van az UTC-től. Nyáron, a nyári időszámítás miatt, a különbség +2 óra. 
	public static Date utcToHun(Date utc)
	{
		return new Date(isDateInSummer(utc)?utc.getTime()+7200000:utc.getTime()+3600000);
	}
	
	public static Date hunToUtc(Date hun)
	{
		return new Date(isDateInSummer(hun)?hun.getTime()-7200000:hun.getTime()-3600000);
	}
	
	public static int getOfDate(Date d,int Calendar_dot_prop)
	{
		Calendar cal = cal();
		cal.setTime(d);
		return cal.get(Calendar_dot_prop);
	}
	
	public static boolean isStartOfDay(Date d)
	{
		Calendar c = cal();
		c.setTime(d);
		return c.get(Calendar.HOUR_OF_DAY) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.MILLISECOND) % 1000 < 500;
	}
	
	public static boolean isStartOfWeek(Date d)
	{
		Calendar c = cal();
		c.setTime(d);
		return c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && c.get(Calendar.HOUR_OF_DAY) == 0 && c.get(Calendar.MINUTE) == 0 && c.get(Calendar.SECOND) == 0 && c.get(Calendar.MILLISECOND) % 1000 < 500;
	}
	
	/**
	 * a Nap vége:
	 * *\*\* 23:59:999
	 * */
	public static boolean isEndOfDay(Date d)
	{
		Calendar c = cal();
		c.setTime(d);
		return c.get(Calendar.HOUR_OF_DAY) == 23 && c.get(Calendar.MINUTE) == 59 && c.get(Calendar.MILLISECOND) % 1000 > 500;
	}
	
	public static Date roundByMilisec(Date d)
	{
		Calendar c = cal();
		c.setTime(d);
		int milli = c.get(Calendar.MILLISECOND);
		if(milli > 499)
			c.add(Calendar.MILLISECOND, 1000-milli);
		else
			c.add(Calendar.MILLISECOND, -milli);
		
		return c.getTime();
	}

	
	public static String toStringCalendar(Date d)
	{
		Calendar c = cal();
		c.setTime(d);
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\n\tCalendar.YEAR: ");
		sb.append(c.get(Calendar.YEAR));
		
		sb.append("\n\tCalendar.MONTH: ");
		sb.append(c.get(Calendar.MONTH));
		
		sb.append("\n\tCalendar.WEEK_OF_YEAR: ");
		sb.append(c.get(Calendar.WEEK_OF_YEAR));
		
		sb.append("\n\tCalendar.WEEK_OF_MONTH: ");
		sb.append(c.get(Calendar.WEEK_OF_MONTH));
		
		
		sb.append("\n\tCalendar.DAY_OF_MONTH: ");
		sb.append(c.get(Calendar.DAY_OF_MONTH));
		
		sb.append("\n\tCalendar.DAY_OF_WEEK: ");
		sb.append(c.get(Calendar.DAY_OF_WEEK));
		
		sb.append("\n\tCalendar.DAY_OF_YEAR: ");
		sb.append(c.get(Calendar.DAY_OF_YEAR));
		
		sb.append("\n\tCalendar.DAY_OF_WEEK_IN_MONTH: ");
		sb.append(c.get(Calendar.DAY_OF_WEEK_IN_MONTH));
		
		sb.append("\n\tCalendar.HOUR: ");
		sb.append(c.get(Calendar.HOUR));
		
		sb.append("\n\tCalendar.HOUR_OF_DAY: ");
		sb.append(c.get(Calendar.HOUR_OF_DAY));
		
		sb.append("\n\tCalendar.MINUTE: ");
		sb.append(c.get(Calendar.MINUTE));
		
		sb.append("\n\tCalendar.SECOND: ");
		sb.append(c.get(Calendar.SECOND));
		
		sb.append("\n\tCalendar.MILLISECOND: ");
		sb.append(c.get(Calendar.MILLISECOND));
		
		sb.append("\n}");

		return sb.toString();
	}
	
	
	public static final long sec_ms = 1000;
	public static final long minutes_ms = sec_ms*60;
	public static final long hours_ms = minutes_ms*60;
	public static final long day_ms = hours_ms*24;
	public static final long week_ms = day_ms*7;
	public static final long year_ms = day_ms*365;
	
/*	public static void main(String[] args)
	{
		//LongInterval in = yearSummerRange.calcuate(2008);
		//System.out.println(Format.magyarDatum(new Date(in.min)));
		//System.out.println(Format.magyarDatum(new Date(in.max)));
		//System.out.println(isDateInSummer(strtotime.strtotime("2008/03/30 00:00:00")));
		//System.out.println(utcToHun());
		Date d = date(2015, 2, 9, 0, 0, 0, 0);
		
		
		//Date d = date(2015, 0, 1, 0, 0, 0, -1);
		System.out.println(isStartOfWeek(d));
		//System.out.println(toStringCalendar(d));
	}
*/	
	public static int getYearOfDate(Date d)
	{
		Calendar c = cal();
		c.setTime(d);
		return c.get(Calendar.YEAR);
	}
	
	public static String magyarHetNapRovid(Date d)
	{
		Calendar c = cal();
		c.setTime(d);
		
		switch (c.get(Calendar.DAY_OF_WEEK))
		{
		case 1: return "V";
		case 2: return "H";
		case 3: return "K";
		case 4: return "Sze";
		case 5: return "Cs";
		case 6: return "P";
		case 7: return "Szo";
		}
		return "Hiba";
	}
	
	public static long absWeek(Date d)
	{
		Calendar c = cal();	
		//System.out.println(toStringCalendar(d));
		
		c.setTime(d);
		//egy évben valójában 53 hét van ha sorszámozzuk őket...
		long y = c.get(Calendar.YEAR);
		long w = c.get(Calendar.WEEK_OF_YEAR);
		if(c.get(Calendar.MONTH) == 11 && w == 1) //decemberben és első hét?
			return y*52+53;
		else
			return y*52+w;
	
	}

	public static int absHourMinOfDay(Date time)
	{
		Calendar c = cal();
		c.setTime(time);
		return c.get(Calendar.HOUR_OF_DAY)*60+c.get(Calendar.MINUTE);
	}
	
	public static final Date startOfTime = new Date(0);
	public static final Date endOfTime = new Date(java.lang.Long.MAX_VALUE);
	
	/**
	 * -1 ha nem tartozik bele
	 * */
	public static long durationInThisWeek(Date base,Date from,Date to)
	{
		long b = absWeek(base);
		long f = absWeek(from);
		long t = absWeek(to);
		
		if(f == b && b == t)
		//A héten van, nem lóg sehova át
			return to.getTime() - from.getTime();
		else if(b == f)
		//hamarabb kezdődik mint a hét
			return actionInThisWeek(from, to);
		else if(b == t)
		//belecsúszik a következő hétbe
			return duration(from.getTime(), to.getTime()) - actionInThisWeek(from, to);
		else
		//nem tartozik ebbe a hétbe
			return -1;
	}
	
	private static final ThreadLocal<Calendar> threadLocalCalendar = new ThreadLocal<Calendar>()
	{
		@Override
		protected Calendar initialValue()
		{
			return Calendar.getInstance();
		}
	};
	
	public static Date setTodayTime(Date day,int hour, int min,int sec, int milis)
	{
		Calendar c = cal();
		c.setTime(day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);
		c.set(Calendar.MILLISECOND, milis);
		return c.getTime();
	}
	
	public static boolean hasCommonSectionWith(long from1,long to1,long from2,long to2)
	{
		return	(from1 <= to2 && to2 <= to1)
			||
				(from1 <= from2 && from2 <= to1)
			;
	}
	
	public static long union(long from1, long to1, long from2, long to2)
	{
		long min = from1>from2? from1:from2;
		long max = to1>to2?to2:to1;

		long ret = max-min; 
		
		return ret<0?0:ret;
	}
	
	public static boolean isBTW(Date min, Date toCheck,Date max)
	{
		return toCheck.after(min) && toCheck.before(max);
	}
	
	public static Date addToDate(Date d, int year,int month,int day,int hour,int min, int sec, int milisec)
	{
		Calendar c = cal();
		c.setTime(d);
		
		if(year != 0)
			c.set(Calendar.YEAR, c.get(Calendar.YEAR)+year);
		
		if(month != 0)
			c.set(Calendar.MONTH, c.get(Calendar.MONTH)+month);
		
		if(day != 0)
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+day);
		
		if(hour != 0)
			c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)+hour);
		
		if(min != 0)
			c.set(Calendar.MINUTE, c.get(Calendar.MINUTE)+min);
		
		if(sec != 0)
			c.set(Calendar.SECOND, c.get(Calendar.SECOND)+sec);
		
		if(milisec != 0)
			c.set(Calendar.MILLISECOND, c.get(Calendar.MILLISECOND)+milisec);
		
		return c.getTime();
	}
	
	/**
	 * -1 esetén nem állít be semmit, pozitiv és 0 esetén azt állítja be.
	 * */
	public static Date setToDate(Date d, int year,int month,int day,int hour,int min, int sec, int milisec)
	{
		Calendar c = cal();
		c.setTime(d);
		
		if(year > -1)
			c.set(Calendar.YEAR,year);
		
		if(month > -1)
			c.set(Calendar.MONTH,month);
		
		if(day > -1)
			c.set(Calendar.DAY_OF_MONTH,day);
		
		if(hour > -1)
			c.set(Calendar.HOUR_OF_DAY,hour);
		
		if(min > -1)
			c.set(Calendar.MINUTE,min);
		
		if(sec > -1)
			c.set(Calendar.SECOND,sec);
		
		if(milisec > -1)
			c.set(Calendar.MILLISECOND, milisec);
		
		return c.getTime();
	}
	
	public static Date date(int year,int month,int day,int hour,int min, int sec, int milisec)
	{
		Calendar c = cal();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);
		c.set(Calendar.MILLISECOND, milisec);
		return c.getTime();
	}
	
	public static Date dateUtc(int year,int month,int day,int hour,int min, int sec, int milisec)
	{
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		c.set(Calendar.SECOND, sec);
		c.set(Calendar.MILLISECOND, milisec);
		return c.getTime();
	}
	
	public static String durationToHourMin(long dt)
	{
		return (dt/hours_ms)+":"+(leading0IgNeeded(StringTools.untilTerminator(String.valueOf((dt % hours_ms)/(60000.0/1.0)),'.')));
	}
	
	public static String durationToHourMinSec(long dt)
	{
		return (dt/hours_ms)+":"+(leading0IgNeeded(StringTools.untilTerminator(String.valueOf((dt % hours_ms)/60000.0),'.')))+":"+(leading0IgNeeded(StringTools.untilTerminator(String.valueOf((dt % minutes_ms)/1000.0),'.')));
	}
	
	public static CharSequence leading0IgNeeded(CharSequence str)
	{
		if(str.length() == 1)
			return "0"+str;
		else
			return str;
	}
	
	/**
	 * Vigyázat nem működik helyesen 1901 előtt és 2099/07/25 után.
	 * Mentésgemre legyen hogy az int-es ábrázolást 1970/01/01 00:00:00.000 -tól
	 * 2038/01/19 03:14:08-ig lett kitalálva
	 */
	public static long absDay(Date d)
	{
		Calendar c = cal();	
		c.setTime(d);
		int y = c.get(Calendar.YEAR);
		return ((long)(y*365))+((long)c.get(Calendar.DAY_OF_YEAR))+((long)((y-1)/4));
	}
	
	public static int absMonth(Date d)
	{
		Calendar c = cal();	
		c.setTime(d);
		return (c.get(Calendar.YEAR)*12)+(c.get(Calendar.MONTH)+1);
	}
	
	
	public static Date absDayToDate(long absDay)
	{
		int y = (int) (absDay/365);
		int d = (int) ((absDay%365) - ((y-1)/4));
		return date(y, 1, d, 0,0,0,0);
	}
	
	public static boolean dateEqualsByUnits(Date d1,Date d2,boolean year,boolean month,boolean day, boolean hour,boolean minute, boolean secound, boolean ms)
	{
		if(year)
			if(getOfDate(d1, Calendar.YEAR) != getOfDate(d2, Calendar.YEAR))
				return false;
			
		if(month)
			if(getOfDate(d1, Calendar.MONTH) != getOfDate(d2, Calendar.MONTH))
				return false;

		if(day)
			if(getOfDate(d1, Calendar.DAY_OF_MONTH) != getOfDate(d2, Calendar.DAY_OF_MONTH))
				return false;

		if(hour)
			if(getOfDate(d1, Calendar.HOUR_OF_DAY) != getOfDate(d2, Calendar.HOUR_OF_DAY))
				return false;

		if(minute)
			if(getOfDate(d1, Calendar.MINUTE) != getOfDate(d2, Calendar.MINUTE))
				return false;

		if(secound)
			if(getOfDate(d1, Calendar.SECOND) != getOfDate(d2, Calendar.SECOND))
				return false;

		if(ms)
			if(getOfDate(d1, Calendar.MILLISECOND) != getOfDate(d2, Calendar.MILLISECOND))
				return false;
			
		return true;
	}
	
	public static void main(String[] args)
	{
		//long t0 = absDay(date(2014, 8, 3, 3, 30, 0, 0));
		//long t1 = absDay(date(2014, 8, 3, 7, 30, 0, 0));
		
		
		//System.out.println(Format.magyarDatum(absDayToDate(t0)));
		//System.out.println(Format.magyarDatum(absDayToDate(t1)));

		
	//	UnitTeszt.testAbsDayConvertToAndFrom();
		
		/*int n = 0;
		for(int i= 0;i < 2000;i++)
		{
			Date d = date(i, 12, 31, 12, 0, 0, 0);
			int dy = getOfDate(d, Calendar.DAY_OF_YEAR);
			if(dy == 366)
			{
				System.out.println(i);
				n++;
			}
		}
		
		System.out.println(n);
		*/
	}

	
	/**
	 * -1 ha nem tartozik bele
	 * */
	public static long durationInThisDay(Date base,Date from,Date to)
	{
		long b = absDay(base);
		long f = absDay(from);
		long t = absDay(to);
		
		
		if(f == b && b == t)
		//A héten van, nem lóg sehova át
			return to.getTime() - from.getTime();
		else if(b == f)
		//hamarabb kezdődik mint a hét
			return actionInThisDay(from, to);
		else if(b == t)
		//belecsúszik a következő hétbe
			return duration(from.getTime(), to.getTime()) - actionInThisDay(from, to);
		else
		//nem tartozik ebbe a hétbe
			return -1;
	}

	public static long actionInThisDay(Date t0,Date to)
	{
		Calendar c = cal();
		c.setTime(t0);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		
		return c.getTime().getTime() - t0.getTime();
	}
	
	public static Date dec(Date d)
	{
		return new Date(d.getTime()-1);
	}
	
	public static Date inc(Date d)
	{
		return new Date(d.getTime()+1);
	}
	
	public static long actionInThisWeek(Date t0,Date to)
	{
		Calendar c = cal();
		c.setTime(t0);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		
		return c.getTime().getTime() - t0.getTime();
	}
	
	/**
	 * ha plusWeek == 0 akkor az eheti dátumokat (t0-hoz képest) elfogadja (true)
	 * */
	public static boolean isAfterNthWeek(Date t0,Date now,int plusWeeks)
	{
		return absWeek(t0) < absWeek(now)+plusWeeks;
	}
	
	public static boolean isAfterNthDay(Date t0,Date now,int plusDays)
	{
		return absDay(t0) < absDay(now)+plusDays;
	}
	
	
	public static boolean isNextWeek(Date t0,Date now)
	{
		return isAfterNthWeek(t0, now, 0);
	}
	
	public static boolean isNextDay(Date t0,Date now)
	{
		return isAfterNthDay(t0, now, 0);
	}
	
	public static long duration(long t0,long t1)
	{
		return t1-t0;
	}

	public static Date firstDayOfMonth(Date date)
	{
		Calendar c = cal();

		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTime();
	}
	
	public static Date lastDayOfMonth(Date date)
	{
		Calendar c = cal();

		c.setTime(date);
		/*c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		*/
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		
		return c.getTime();
	}
	
	private static Calendar cal()
	{
		return threadLocalCalendar.get();
	}
	
	public static Date firstDayOfWeek(Date date)
	{
		Calendar c = cal();

		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		return c.getTime();
	}
	
	public static Date lastDayOfWeek(Date date)
	{
		Calendar c = cal();

		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		
		return c.getTime();
	}
	
	public static Date startOfDay(Date date)
	{
		Calendar c = cal();

		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static Date endOfDay(Date date)
	{
		Calendar c = cal();

		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		
		return c.getTime();
	}
	
	public static Date startOfMonth(Date date)
	{
		Calendar c = cal();

		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static Date endOfMonth(Date date)
	{
		date = startOfMonth(date);
		return TimeCalc.addToDate(date, 0, 1, 0, 0, 0, 0, -1);
	}
	
	
	public static boolean isBetween(Date from, Date date, Date to)
	{
		long d = date.getTime();
		return from.getTime() <= d && d <= to.getTime();
	}
	
	public static long getDurationFromDayStartMs(Date time)
	{
		return absHourMinOfDay(time)*minutes_ms;
	}
	
	public static long getDurationBetween(Date in_from, Date in_to, Date subject_from, Date subject_to)
	{
		return union(in_from.getTime(), in_to.getTime(), subject_from.getTime(), subject_to.getTime());
	}

	public static Date max(Date t1, Date t2)
	{
		return t1.getTime() > t2.getTime()?t1:t2;
	}

	public static Date min(Date t1, Date t2)
	{
		return t1.getTime() < t2.getTime()?t1:t2;
	}

	public static long duration(Date start, Date end)
	{
		return duration(start.getTime(), end.getTime());
	}

	public static boolean dateEqualsByUnits(Date nap, int year, int month, int day,	int hour, int min, int sec, int ms)
	{
		return dateEqualsByUnits
		(
			nap,
			TimeCalc.date
			(
				MathTools.clamp(0, year, Integer.MAX_VALUE),
				MathTools.clamp(0, month, Integer.MAX_VALUE),
				MathTools.clamp(0, day, Integer.MAX_VALUE),
				MathTools.clamp(0, hour, Integer.MAX_VALUE),
				MathTools.clamp(0, min, Integer.MAX_VALUE),
				MathTools.clamp(0, sec, Integer.MAX_VALUE),
				MathTools.clamp(0, ms, Integer.MAX_VALUE)
			),
			year > -1,
			month > -1,
			day > -1,
			hour > -1,
			min > -1,
			sec > -1,
			ms > -1
		);
	}
	
	public static <T> Date[] minMaxDateFromTo(Collection<T> fs, SimplePublish2<T, Date[/*2*/]> examiner)
	{
		long min = Long.MAX_VALUE;
		long max = Long.MIN_VALUE;
		
		Date[] ds = new Date[2];
		
		for(T f:fs)
		{
			ds[0] = ds[1] = null;
			
			examiner.publish(f, ds);
			
			long from = Long.MAX_VALUE;
			long to = Long.MIN_VALUE;
			
			if(null != ds[0])
			{
				from = ds[0].getTime();
			}
			
			if(null != ds[1])
			{
				to = ds[1].getTime();
			}
			
			if(from < min)
				min = from;

			if(to > max)
				max = to;
		}
		
		if(min != Long.MAX_VALUE && max != Long.MIN_VALUE)
		{
			return new Date[]{new Date(min), new Date(max)};
		}
		
		return null;
	}
	
	public static <T> Date[] minMaxDateTimePoints(Collection<T> fs, final GetBy1<Date, T> examiner)
	{
		return minMaxDateFromTo(fs, new SimplePublish2<T, Date[]>()
		{
			@Override
			public void publish(T a, Date[] b)
			{
				b[0] = b[1] = examiner.getBy(a);
			}
		});
	}

	public static boolean isLeapYear(int year)
	{
		return
			(((year % 100) == 0) && ((year % 400) ==0))
		|| 
			(((year % 100) >0) && (year % 4) == 0);
	}
	
	//http://wiki.ham.hu/index.php/UTC
/*	public static boolean isDateInSummer(Date d)
	{
		LongInterval in = yearSummerRange.calcuate(getYearOfDate(d));
		return in.isBetween(d.getTime());
	}
	
	private static final SimpleCalculationMemorizer<LongInterval, Integer> yearSummerRange = new SimpleCalculationMemorizer<LongInterval, Integer>(new GetBy1<LongInterval, Integer>()
	{
		@Override
		public LongInterval getBy(Integer a)
		{
			Calendar start = cal();
			start.set(Calendar.YEAR, a);
			start.set(Calendar.MONTH, 2);
			start.set(Calendar.DAY_OF_MONTH, 31);
			start.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			start.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
			start.set(Calendar.HOUR_OF_DAY, 2);
			start.set(Calendar.MINUTE, 0);
			start.set(Calendar.SECOND, 0);
			start.set(Calendar.MILLISECOND, 0);

			long s = start.getTimeInMillis();
			
			Calendar end = start;
			end.set(Calendar.YEAR, a);
			end.set(Calendar.MONTH, 9);
			end.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			end.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
			end.set(Calendar.HOUR_OF_DAY, 3);
			end.set(Calendar.MINUTE, 0);
			end.set(Calendar.SECOND, 0);
			end.set(Calendar.MILLISECOND, 0);
			
			return new LongInterval(s, end.getTimeInMillis());
		}
	});
*/	
	
	
}
