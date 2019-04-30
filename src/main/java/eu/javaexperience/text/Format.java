package eu.javaexperience.text;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.resource.pool.SimplifiedResourcePool;

public class Format
{
	private Format() {}
	
	public static class DateFormatParseUnit
	{
		protected String format;
		protected TimeZone tz;
		
		protected SimpleDateFormat create(String format)
		{
			SimpleDateFormat ret = new SimpleDateFormat(format);
			if(null != tz)
			{
				ret.setTimeZone(tz);
			}
			return ret;
		}
		
		protected final SimplifiedResourcePool<SimpleDateFormat> pool = new SimplifiedResourcePool<>(new SimpleGet<SimpleDateFormat>()
		{
			@Override
			public SimpleDateFormat get()
			{
				return create(format);
			}
		});
		
		public DateFormatParseUnit(String format)
		{
			this.format = format;
		}
		
		public DateFormatParseUnit(String format, TimeZone tz)
		{
			this.format = format;
			this.tz = tz;
		}
		
		
		public String format(Date d)
		{
			SimpleDateFormat format = pool.acquireResource();
			try
			{
				return format.format(d);
			}
			finally
			{
				pool.releaseResource(format);
			}
		}
		
		public Date parse(String str) throws ParseException
		{
			SimpleDateFormat format = pool.acquireResource();
			try
			{
				return format.parse(str);
			}
			finally
			{
				pool.releaseResource(format);
			}
		}
		
		public Date tryParse(String date)
		{
			try
			{
				return parse(date);
			}
			catch(Exception e)
			{
				return null;
			}
		}

		public String getFormat()
		{
			return format;
		}
	}
	
	public static final DateFormatParseUnit SQL_TIMESTAMP_MS = new DateFormatParseUnit("yyyy-MM-dd HH:mm:ss.SSS");
	
	public static String sqlTimestampMilisec(Date d)
	{
		return SQL_TIMESTAMP_MS.format(d);
	}
	
	public static final DateFormatParseUnit UTC_SQL_TIMESTAMP_MS = new DateFormatParseUnit("yyyy-MM-dd HH:mm:ss.SSS'U'", TimeZone.getTimeZone("UTC"));
	
	public static final DateFormatParseUnit SQL_TIMESTAMP = new DateFormatParseUnit("yyyy-MM-dd HH:mm:ss");
	
	public static String sqlTimestamp(Date d)
	{
		return SQL_TIMESTAMP.format(d);
	}
	
	public static Date parseSqlTimestamp(String date) throws ParseException
	{
		return SQL_TIMESTAMP.parse(date);
	}
	
	public static final DateFormatParseUnit SQL_DATE = new DateFormatParseUnit("yyyy-MM-dd");
	
	public static Date tryParseSqlTimestamp(String date)
	{
		return SQL_TIMESTAMP.tryParse(date);
	}
	
	public static String formatSqlDate(Date d)
	{
		return SQL_DATE.format(d);
	}
	
	
	public static final DateFormatParseUnit UTC_FILE_SQL_TIMESTAMP = new DateFormatParseUnit("yyyy-MM-dd_HH-mm-ss_SSS'U'", TimeZone.getTimeZone("UTC"));
	
	public static final DateFormatParseUnit FILE_SQL_TIMESTAMP = new DateFormatParseUnit("yyyy-MM-dd_HH-mm-ss_SSS");
	
	public static final DateFormatParseUnit BROWSER_DATETIMESTAMP = new DateFormatParseUnit("yyyy-MM-dd'T'HH:mm");
	
	public static String shortenStringIfLongerThan(String str, int len)
	{
		if(str.length() > len)
		{
			return str.substring(0, len)+"...";
		}
		else
		{
			return str;
		}
	}
	
	private static class ReuseableFormatter
	{
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
	}
	
	private static final ThreadLocal<ReuseableFormatter> reuseableFormatter =  new ThreadLocal<ReuseableFormatter>()
	{
		@Override
		protected ReuseableFormatter initialValue()
		{
			return new ReuseableFormatter();
		}
	};
	
	public static String format(String format, Object... args)
	{
		ReuseableFormatter f = reuseableFormatter.get();
		f.f.format(format, args);
		String ret = f.sb.toString();
		f.sb.delete(0, f.sb.length());
		return ret;
	}
	
	public static String getPrintedStackTrace(Throwable t)
	{
		if(t == null)
			return null;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		t.printStackTrace(ps);
		return new String(baos.toByteArray());
	}
	
	public static String stackTraceToString(StackTraceElement[] ele)
	{
		StringBuilder sb = new StringBuilder();
		for(StackTraceElement e:ele)
		{
			sb.append("\n\t");
			sb.append(e);
		}
		
		return sb.toString();
	}
	
	public static String cutFloatDecimals(String num, int decimals)
	{
		StringBuilder sb = new StringBuilder();
		boolean bp = true;
		int n = 0;
		for(int i=0;i<num.length();i++)
		{
			char c = num.charAt(i);
			if(c == '.')
			{
				bp = false;
				
				//lookahead
				int max = Math.min(decimals, num.length()-i-1);
				boolean need = false;
				for(int j=0;j < max;++j)
				{
					if(num.charAt(i+j+1) != '0')
					{
						need = true;
						break;
					}
				}
				
				if(!need)
				{
					break;
				}
				
				sb.append('.');
				
				continue;
			}
			
			if(bp)
			{
				sb.append(c);
			}
			else if(++n > decimals)
			{
				break;
			}
			else
			{
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
	
	private static final ThreadLocal<DecimalFormat> DoubleFormat =  new ThreadLocal<DecimalFormat>()
	{
		@Override
		protected DecimalFormat initialValue()
		{
			return new DecimalFormat("#.##");
		}
	};
	
	public static String formatDouble(double n)
	{
		DecimalFormat df = DoubleFormat.get();
		return df.format(n).replace(',', '.');
	}

	public static String toHex(byte[] data)
	{
		byte[] re = new byte[data.length*2];
		for(int i=0;i<data.length;++i)
		{
			byte b = data[i];
			re[i*2] =  hexaTransform((0xf0 & b) >>> 4);
			re[i*2+1] = hexaTransform(0x0f & b);
		}
		return new String(re);
	}
	
	public static byte hexaTransform(int val)
	{
		if(val < 10)
		{
			return (byte) ('0'+ val);
		}
		else
		{
			return (byte) ('a'+ (val-10));
		}
	}
	
	public static int fromHexaTransform(char val)
	{
		if(val > '9')
		{
			return (byte) (10+(val-'a'));
		}
		return (byte) (val-'0');
	}
	
	public static byte[] fromHex(String hex)
	{
		hex = hex.toLowerCase();
		byte[] ret = new byte[hex.length()/2];
		
		for(int i=0;i<ret.length;++i)
		{
			ret[i] =  (byte) ( (fromHexaTransform(hex.charAt(i*2)) << 4)
					|
						fromHexaTransform(hex.charAt(i*2+1)) );
		}
		return ret;
	}
	
	private static final char[] CHAR_TABLE = {
		'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P',
		'Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f',
		'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v',
		'w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/' };
	
	
	public static String base64Encode(byte[] data)
	{
		StringBuilder buffer = new StringBuilder();
		int pad = 0;
		for (int i = 0; i < data.length; i += 3) {

			int b = ((data[i] & 0xFF) << 16) & 0xFFFFFF;
			if (i + 1 < data.length) {
				b |= (data[i+1] & 0xFF) << 8;
			} else {
				pad++;
			}
			if (i + 2 < data.length) {
				b |= (data[i+2] & 0xFF);
			} else {
				pad++;
			}

			for (int j = 0; j < 4 - pad; j++) {
				int c = (b & 0xFC0000) >> 18;
				buffer.append(CHAR_TABLE[c]);
				b <<= 6;
			}
		}
		for (int j = 0; j < pad; j++) {
			buffer.append("=");
		}

		return buffer.toString();
	}

	private static final int[] BASE64_TABLE = {
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54,
		55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2,
		3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
		20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30,
		31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
		48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
		-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	
	public static byte[] base64Decode(String data)
	{
		byte[] bytes = data.getBytes();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		for (int i = 0; i < bytes.length; ) {
			int b = 0;
			if (BASE64_TABLE[bytes[i]] != -1) {
				b = (BASE64_TABLE[bytes[i]] & 0xFF) << 18;
			}
			// skip unknown characters
			else {
				i++;
				continue;
			}

			int num = 0;
			if (i + 1 < bytes.length && BASE64_TABLE[bytes[i+1]] != -1) {
				b = b | ((BASE64_TABLE[bytes[i+1]] & 0xFF) << 12);
				num++;
			}
			if (i + 2 < bytes.length && BASE64_TABLE[bytes[i+2]] != -1) {
				b = b | ((BASE64_TABLE[bytes[i+2]] & 0xFF) << 6);
				num++;
			}
			if (i + 3 < bytes.length && BASE64_TABLE[bytes[i+3]] != -1) {
				b = b | (BASE64_TABLE[bytes[i+3]] & 0xFF);
				num++;
			}

			while (num > 0) {
				int c = (b & 0xFF0000) >> 16;
				buffer.write((char)c);
				b <<= 8;
				num--;
			}
			i += 4;
		}
		return buffer.toByteArray();
	}
	
	
	//https://stackoverflow.com/questions/1236678/phps-strtotime-in-java
	public static final class strtotime
	{
		private strtotime(){}
		
		private static final List<Matcher> matchers;

		static
		{
			matchers = new LinkedList<>();
			matchers.add(new NowMatcher());
			matchers.add(new TomorrowMatcher());
			
			ArrayList<String> stringFormats = new ArrayList<>(); 
			
			//to milisec
			stringFormats.add("yyyy-MM-dd HH:mm:ss.SSS");
			stringFormats.add("yyyy.MM.dd HH:mm:ss.SSS");
			stringFormats.add("yyyy.MM.dd. HH:mm:ss.SSS");
			stringFormats.add("yyyy-MM-dd_HH-mm-ss_SSS");
			stringFormats.add("yyyy. MM. dd. HH:mm:ss.SSS");
			
			//to sec with timezone
			
			stringFormats.add("EEE dd-MMM-yyyy HH:mm:ss z");
			stringFormats.add("EEE dd MMM yyyy HH:mm:ss zzz");
			stringFormats.add("yyyy.MM.dd G 'at' HH:mm:ss z");
			stringFormats.add("EEE d MMM yyyy HH:mm:ss Z");
			
			//000 => 111
			stringFormats.add("MM/dd/yyyy hh:mm:ss aaa");
			stringFormats.add("MM/dd/yyyy hh:mm:s aaa");
			stringFormats.add("MM/dd/yyyy hh:m:ss aaa");
			stringFormats.add("MM/dd/yyyy hh:m:s aaa");
			
			stringFormats.add("MM/dd/yyyy h:mm:ss aaa");
			stringFormats.add("MM/dd/yyyy h:mm:s aaa");
			stringFormats.add("MM/dd/yyyy h:m:ss aaa");
			stringFormats.add("MM/dd/yyyy h:m:s aaa");
			
			
			//to sec
			stringFormats.add("yyyy/MM/dd HH:mm:ss");
			stringFormats.add("yyyy. MM. dd HH:mm:ss");
			stringFormats.add("yyyy. MM. dd. HH:mm:ss");
			stringFormats.add("yyyy-MM-dd HH:mm:ss");
			stringFormats.add("yyyy.MM.dd HH:mm:ss");
			stringFormats.add("yyyy.MM.dd. HH:mm:ss");
			
			stringFormats.add("yyyy-MM-dd'T'HH:mm:ss");

			//to minutes
			stringFormats.add("yyyy-MM-dd'T'HH:mm");
			stringFormats.add("yyyy/MM/dd HH:mm");
			stringFormats.add("yyyy.MM.dd HH:mm");
			
			
			
			//YMD
			stringFormats.add("MM/dd/yyyy");
			stringFormats.add("yyyy/MM/dd");
			stringFormats.add("yyyy MM dd");
			stringFormats.add("yyyy. MM. dd");
			stringFormats.add("yyyy. MM. dd.");
			stringFormats.add("yyyy-MM-dd");
			stringFormats.add("yyyy.MM.dd");
			stringFormats.add("yyyy.MM.dd.");
			
			
			//DayofWeek, Month, day, year hour:min:sec			
			stringFormats.add("EEEE MMMM d yyyy hh:mm:ss");
			stringFormats.add("EEEE MMMM d yyyy hh:mm:s");
			stringFormats.add("EEEE MMMM d yyyy hh:m:ss");
			stringFormats.add("EEEE MMMM d yyyy hh:m:s");
			
			//Tuesday 30, April 2019
			stringFormats.add("EEEE d MMMM yyyy");
			
			
			
			for(String s:stringFormats)
			{
				registerMatcher(new DateFormatMatcher(new SimpleDateFormat(s)));
				registerMatcher(new DateFormatMatcher(new SimpleDateFormat(s, Locale.US)));
			}
			
			// add as many format as you want 
		}

		// not thread-safe
		public static void registerMatcher(Matcher matcher)
		{
			matchers.add(matcher);
		}

		public static interface Matcher
		{
			public Date tryConvert(String input);
		}

		private static class DateFormatMatcher implements Matcher
		{
			private final DateFormat dateFormat;

			public DateFormatMatcher(DateFormat dateFormat)
			{
				this.dateFormat = dateFormat;
			}

			public Date tryConvert(String input)
			{
				try
				{
					synchronized(dateFormat)
					{
						return dateFormat.parse(input);
					}
				}
				catch (ParseException ex)
				{
					return null;
				}
			}
		}

		private static class NowMatcher implements Matcher
		{
			private final Pattern now = Pattern.compile("now");

			public Date tryConvert(String input)
			{
				if(now.matcher(input).matches())
				{
					return new Date();
				}
				else
				{
					return null;
				}
			}
		}

		private static class TomorrowMatcher implements Matcher
		{
			private final Pattern tomorrow = Pattern.compile("tomorrow");

			public Date tryConvert(String input)
			{
				if(tomorrow.matcher(input).matches())
				{
					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_YEAR, +1);
					return calendar.getTime();
				}
				else
				{
					return null;
				}
			}
		}

		public static Date strtotime(String input)
		{
			if(null != input)
			{
				input = StringTools.replaceAllStrings(input, ",", "");
			}
			
			for(Matcher matcher:matchers)
			{
				Date date = matcher.tryConvert(input);
				if (date != null)
				{
					return date;
				}
			}

			return null;
		}
	}

	public static Date strToTime(String date)
	{
		return strtotime.strtotime(date);
	}
}