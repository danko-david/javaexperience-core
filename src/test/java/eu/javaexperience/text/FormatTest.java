package eu.javaexperience.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;
import java.util.MissingFormatArgumentException;

import org.junit.Test;

import static eu.javaexperience.text.Format.*;

import eu.javaexperience.math.MathTools;
import eu.javaexperience.time.TimeCalc;

public class FormatTest
{
	protected static void testStrToTime(String str, int year, int month, int day, int hour, int min, int sec, int ms)
	{
		Date d = strToTime(str);
		Date cmp = TimeCalc.setToDate(new Date(), year, month-1, day, hour, min, sec, ms);
		
		boolean success = TimeCalc.dateEqualsByUnits(d, cmp, year > -1, month > -1, day> -1, hour > -1, min > -1, sec > -1, ms > -1);
		assertTrue(success);
		//System.err.println((success?"SUCCESS: ":"FAIL: ")+str+" => parse: "+Format.sqlTimestampMilisec(d)+" <=> cmp: "+Format.sqlTimestampMilisec(cmp));
	}
	
	@Test
	public void testStrToTime()
	{
		testStrToTime("2015-02-15 12:20:59.352", 2015, 2, 15, 12, 20, 59, 352);
		testStrToTime("7/17/2016", 2016, 7, 17, -1, -1, -1, -1);
		testStrToTime("7/17/2016 10:02:03 AM", 2016, 7, 17, 10, 2, 3, -1);
		testStrToTime("8/1/2016 5:00:41 PM", 2016, 8, 1, 17, 0, 41, -1);
		testStrToTime("2016. 06. 27.", 2016, 6, 27, -1, -1, -1, -1);
		testStrToTime("2017. 03. 14. 7:21:58", 2017, 3, 14, 7, 21, 58, -1);
		testStrToTime("2018-06-12T19:30", 2018, 6, 12, 19, 30, -1, -1);
		testStrToTime("2018/06/12 19:30", 2018, 6, 12, 19, 30, -1, -1);
		testStrToTime("2018.06.02 03:22", 2018, 6, 2, 3, 22, -1, -1);
		testStrToTime("2018/10/02 12:06:01", 2018, 10, 2, 12, 6, 1, -1);
		testStrToTime("2018.11.08 12:05", 2018, 11, 8, 12, 5, -1, -1);
		
		//this might fail 
		assertEquals(strToTime("now"), new Date());
		
		assertTrue(MathTools.inRange(TimeCalc.addToDate(new Date(), 0, 0, 1, 0, 0, 0, 0).getTime(), strToTime("tomorrow").getTime(), 500));
		
		//testStrToTime("Wednesday, August 23, 1961", 1961, 8, 23, -1, -1, -1, -1);
	}
	
	@Test
	public void testStrToTime2()
	{
		testStrToTime("", -1, -1, -1, -1, -1, -1, -1);
	}
	
	@Test
	public void testStrToTime3()
	{
		//this might fail if there's higher time jitter between parsing and construction of date.
		assertEquals(strToTime("now"), new Date());
	}		
	
	@Test
	public void testSqlTimestampMilisec()
	{
		assertEquals("2018-11-03 10:20:12.345", sqlTimestampMilisec(TimeCalc.date(2018, 11, 3, 10, 20, 12, 345)));
	}
	
	@Test
	public void testSqlTimestamp()
	{
		assertEquals("2018-11-03 10:20:12", sqlTimestamp(TimeCalc.date(2018, 11, 3, 10, 20, 12, 345)));
	}
	
	@Test
	public void testParseSqlTimestamp() throws ParseException
	{
		assertEquals(TimeCalc.date(2018, 11, 3, 10, 20, 12, 0), parseSqlTimestamp("2018-11-03 10:20:12"));
		
		assertEquals(null, tryParseSqlTimestamp("2018-11-03-10:20:12"));
		assertEquals(TimeCalc.date(2018, 11, 3, 10, 20, 12, 0), tryParseSqlTimestamp("2018-11-03 10:20:12"));
	}
	
	@Test
	public void testFormatSqlDate()
	{
		assertEquals("2018-11-03", formatSqlDate(TimeCalc.date(2018, 11, 3, 10, 20, 12, 345)));
	}
	
	@Test
	public void testUTC_FILE_SQL_TIMESTAMP() throws ParseException
	{
		assertEquals("yyyy-MM-dd_HH-mm-ss_SSS'U'", UTC_FILE_SQL_TIMESTAMP.getFormat());
		assertEquals(1541240412345l, UTC_FILE_SQL_TIMESTAMP.parse("2018-11-03_10-20-12_345U").getTime());
		assertEquals("2018-11-03_10-20-12_345U", UTC_FILE_SQL_TIMESTAMP.format(new Date(1541240412345l)));
	}
	
	@Test
	public void testBase64EncodeDecode()
	{
		String b64 = Format.base64Encode("this is a beginning of a beautiful friendship".getBytes());
		assertEquals("dGhpcyBpcyBhIGJlZ2lubmluZyBvZiBhIGJlYXV0aWZ1bCBmcmllbmRzaGlw", b64);
		assertEquals("this is a beginning of a beautiful friendship", new String(Format.base64Decode(b64)));
	}
	
	@Test
	public void testBase64EncodeDecode2()
	{
		String b64 = null;
		{
			byte[] data = new byte[256];
			for(int i=0;i<256;++i)
			{
				data[i] = (byte) (i-128);
			}
			
			b64 = Format.base64Encode(data);
		}
		
				
		assertEquals("gIGCg4SFhoeIiYqLjI2Oj5CRkpOUlZaXmJmam5ydnp+goaKjpKWmp6ipqqusra6vsLGys7S1tre4ubq7vL2+v8DBwsPExcbHyMnKy8zNzs/Q0dLT1NXW19jZ2tvc3d7f4OHi4+Tl5ufo6err7O3u7/Dx8vP09fb3+Pn6+/z9/v8AAQIDBAUGBwgJCgsMDQ4PEBESExQVFhcYGRobHB0eHyAhIiMkJSYnKCkqKywtLi8wMTIzNDU2Nzg5Ojs8PT4/QEFCQ0RFRkdISUpLTE1OT1BRUlNUVVZXWFlaW1xdXl9gYWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXp7fH1+fw==", b64);
		
		{
			byte[] dec = Format.base64Decode(b64);
			
			for(int i=0;i<256;++i)
			{
				assertEquals((byte) (i-128), dec[i]);
			}
		}
	}
	
	@Test
	public void testBase64Decode_2()
	{
		//skipps illegal characters
		assertEquals("__", new String(Format.base64Decode("X18=")));
		assertEquals("__", new String(Format.base64Decode(",X18=")));
		//FAILS assertEquals("__", new String(Format.base64Decode("X,18=")));
		//FAILS assertEquals("__", new String(Format.base64Decode("X1,8=")));
		assertEquals("__", new String(Format.base64Decode("X18,=")));
		assertEquals("__", new String(Format.base64Decode("X18=,")));
		assertEquals("__", new String(Format.base64Decode(",X18=,")));
	}
	

	@Test
	public void testShortenString()
	{
		//well this is a really naive function
		assertEquals("this...", Format.shortenStringIfLongerThan("this is a beginning of a", 4));
		assertEquals("this ...", Format.shortenStringIfLongerThan("this is a beginning of a", 5));
		
		assertEquals("this is a beginning of ...", Format.shortenStringIfLongerThan("this is a beginning of a", 23));
		assertEquals("this is a beginning of a", Format.shortenStringIfLongerThan("this is a beginning of a", 24));
	}
	
	@Test
	public void testFormat1()
	{
		assertEquals("plain text", Format.format("plain text"));
		assertEquals("I'm nullproof", Format.format("I'm %sproof", null));
		
		assertEquals
		(
			"99 bottles of beer on the wall, 99 bottles of beer.",
			Format.format
			(
				"%d bottles of %s on the wall, %d bottles of %s.",
				99, "beer", 99, "beer"
			)
		);
		
		assertEquals
		(
			"98 bottles of wine on the wall, 98 bottles of wine.",
			Format.format
			(
				"%d bottles of %s on the wall, %d bottles of %s.",
				98, "wine", 98, "wine"
			)
		);
	}
	
	@Test(expected = MissingFormatArgumentException.class)
	public void testFormat2()
	{
		Format.format("I'm %sproof");
	}
	
	@Test
	public void testCutFloatDecimals()
	{
		assertEquals("100", Format.cutFloatDecimals("100.002", 1));
		assertEquals("100", Format.cutFloatDecimals("100.002", 2));
		assertEquals("100.002", Format.cutFloatDecimals("100.002", 3));
		
		assertEquals("0123", Format.cutFloatDecimals("0123.2345", 0));
		assertEquals("0123.2", Format.cutFloatDecimals("0123.2345", 1));
		assertEquals("0123.23", Format.cutFloatDecimals("0123.2345", 2));
		assertEquals("0123.234", Format.cutFloatDecimals("0123.2345", 3));
		assertEquals("0123.2345", Format.cutFloatDecimals("0123.2345", 4));
		assertEquals("0123.2345", Format.cutFloatDecimals("0123.2345", 5));
		assertEquals("0123.2345", Format.cutFloatDecimals("0123.2345", 6));
		
		assertEquals("100", Format.cutFloatDecimals("100", 1));
		assertEquals("100", Format.cutFloatDecimals("100", 2));
		assertEquals("100", Format.cutFloatDecimals("100", 3));
		
		assertEquals("0", Format.cutFloatDecimals("0.0", 1));
		assertEquals("0", Format.cutFloatDecimals("0.0", 2));
		assertEquals("0", Format.cutFloatDecimals("0.0", 3));
		
		assertEquals("100", Format.cutFloatDecimals("100.", 1));
		assertEquals("100", Format.cutFloatDecimals("100.", 2));
		assertEquals("100", Format.cutFloatDecimals("100.", 3));
	}
	
	@Test
	public void testFormatDouble()
	{
		assertEquals("100.94", Format.formatDouble(100.944432));
		assertEquals("100.95", Format.formatDouble(100.94765));
		assertEquals("100", Format.formatDouble(100d));
	}
	
	@Test
	public void testFromToHex()
	{
		String hex = null;
		{
			byte[] data = new byte[256];
			for(int i=0;i<256;++i)
			{
				data[i] = (byte) (i-128);
			}
			
			hex = Format.toHex(data);
		}
		
		assertEquals("808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9fa0a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebfc0c1c2c3c4c5c6c7c8c9cacbcccdcecfd0d1d2d3d4d5d6d7d8d9dadbdcdddedfe0e1e2e3e4e5e6e7e8e9eaebecedeeeff0f1f2f3f4f5f6f7f8f9fafbfcfdfeff000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f", hex);
		
		{
			byte[] dec = Format.fromHex(hex);
			
			for(int i=0;i<256;++i)
			{
				assertEquals((byte) (i-128), dec[i]);
			}
		}
	}
	
	@Test
	public void testFromHex()
	{
		assertEquals("Danko David", new String(Format.fromHex("44616e6b6f204461766964")));
	}
	
	@Test
	public void testFromHexUpperCase()
	{
		assertEquals("Danko David", new String(Format.fromHex("44616E6B6F204461766964")));
	}
	
	@Test
	public void testFromHexMixedCase()
	{
		assertEquals("Danko David", new String(Format.fromHex("44616E6b6F204461766964")));
	}
	
}
