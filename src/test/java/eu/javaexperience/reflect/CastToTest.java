package eu.javaexperience.reflect;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import eu.javaexperience.time.TimeCalc;

public class CastToTest
{
	@Test
	public void testCastBoolean()
	{
		assertEquals(true, CastTo.Boolean.cast(true));
		assertEquals(true, CastTo.Boolean.cast("true"));
		assertEquals(true, CastTo.Boolean.cast(" true   "));
		assertEquals(true, CastTo.Boolean.cast(" TRUE  "));
		assertEquals(true, CastTo.Boolean.cast(" TRUEz"));
		assertEquals(true, CastTo.Boolean.cast("1"));
		assertEquals(true, CastTo.Boolean.cast("  1  "));
		assertEquals(true, CastTo.Boolean.cast("  1.012432858623458986  "));
		assertEquals(true, CastTo.Boolean.cast("  1.012432858623458986e-20  "));
		assertEquals(true, CastTo.Boolean.cast(12));
		assertEquals(true, CastTo.Boolean.cast("t"));
		assertEquals(true, CastTo.Boolean.cast('t'));
		
		assertEquals(false, CastTo.Boolean.cast(false));
		assertEquals(false, CastTo.Boolean.cast("false"));
		assertEquals(false, CastTo.Boolean.cast(" false "));
		assertEquals(false, CastTo.Boolean.cast(" FALSEEE"));
		assertEquals(false, CastTo.Boolean.cast("0"));
		assertEquals(false, CastTo.Boolean.cast(0));
		assertEquals(false, CastTo.Boolean.cast("  0.0 "));
		assertEquals(false, CastTo.Boolean.cast("f"));

		
		assertEquals(null, CastTo.Boolean.cast(null));
		assertEquals(null, CastTo.Boolean.cast("treu"));
		assertEquals(null, CastTo.Boolean.cast("flase"));
		assertEquals(null, CastTo.Boolean.cast(" 12l "));
		assertEquals(null, CastTo.Boolean.cast(" l12 "));
		assertEquals(null, CastTo.Boolean.cast(" null "));
		assertEquals(null, CastTo.Boolean.cast('\n'));
	}

	@Test
	public void testCastByte()
	{
		byte zero = 0;
		byte ten = 10;
		assertEquals(null, CastTo.Byte.cast(null));
		assertEquals(zero, CastTo.Byte.cast("0.0"));
		assertEquals(zero, CastTo.Byte.cast("0"));
		assertEquals(zero, CastTo.Byte.cast(" 0  "));
		assertEquals((byte)-ten, CastTo.Byte.cast(" -1e1 "));
		assertEquals(ten, CastTo.Byte.cast('\n'));
		assertEquals((byte)13, CastTo.Byte.cast('\r'));
		assertEquals(ten, CastTo.Byte.cast(10));
		assertEquals(zero, CastTo.Byte.cast(false));
		assertEquals((byte)1, CastTo.Byte.cast(true));
		assertEquals(null, CastTo.Byte.cast("1ee3"));
	}


	@Test
	public void testCastChar()
	{
		assertEquals(null, CastTo.Char.cast(null));
		assertEquals('\0', CastTo.Char.cast(0));
		assertEquals('\n', CastTo.Char.cast(10));
		assertEquals('	', CastTo.Char.cast("\t"));
		assertEquals('\n', CastTo.Char.cast(" 10 "));
		assertEquals('t', CastTo.Char.cast(true));
		assertEquals('\0', CastTo.Char.cast(false));
		assertEquals('\n', CastTo.Char.cast("  1e1  "));
		assertEquals(null, CastTo.Char.cast("  1ee1  "));
		assertEquals('￼', CastTo.Char.cast("￼"));
		assertEquals('a', CastTo.Char.cast("a"));
		assertEquals('a', CastTo.Char.cast(" a"));
	}
	
	@Test
	public void testCastShort()
	{
		short zero = 0;
		short ten = 10;
		assertEquals(null, CastTo.Short.cast(null));
		assertEquals(null, CastTo.Short.cast("0.e"));
		assertEquals(zero, CastTo.Short.cast("0.0"));
		assertEquals(zero, CastTo.Short.cast(" 0.0 "));
		assertEquals(zero, CastTo.Short.cast("0"));
		assertEquals(zero, CastTo.Short.cast(" 0  "));
		assertEquals((short)-ten, CastTo.Short.cast(" -1e1 "));
		assertEquals(ten, CastTo.Short.cast('\n'));
		assertEquals((short)13, CastTo.Short.cast('\r'));
		assertEquals(ten, CastTo.Short.cast(10));
		assertEquals(zero, CastTo.Short.cast(false));
		assertEquals((short)1, CastTo.Short.cast(true));
	}
	
	@Test
	public void testCastInteger()
	{
		assertEquals(null, CastTo.Int.cast(null));
		assertEquals(null, CastTo.Int.cast("0.e"));
		assertEquals(0, CastTo.Int.cast("0.0"));
		assertEquals(0, CastTo.Int.cast("0"));
		assertEquals(0, CastTo.Int.cast(" 0  "));
		assertEquals(10, CastTo.Int.cast(" 1e1 "));
		assertEquals(10, CastTo.Int.cast('\n'));
		assertEquals(13, CastTo.Int.cast('\r'));
		assertEquals(10, CastTo.Int.cast(10));
		assertEquals(1, CastTo.Int.cast(true));
		assertEquals(0, CastTo.Int.cast(false));
	}
	
	@Test
	public void testCastLong()
	{
		assertEquals(null, CastTo.Long.cast(null));
		assertEquals(null, CastTo.Long.cast("0.e"));
		assertEquals(0l, CastTo.Long.cast("0.0"));
		assertEquals(0l, CastTo.Long.cast("0"));
		assertEquals(0l, CastTo.Long.cast(" 0  "));
		assertEquals(10l, CastTo.Long.cast(" 1e1 "));
		assertEquals(10l, CastTo.Long.cast('\n'));
		assertEquals(13l, CastTo.Long.cast('\r'));
		assertEquals(10l, CastTo.Long.cast(10));
		assertEquals(0l, CastTo.Long.cast(false));
		assertEquals(1l, CastTo.Long.cast(true));
	}
	
	@Test
	public void testCastFloat()
	{
		assertEquals(null, CastTo.Float.cast(null));
		assertEquals(null, CastTo.Float.cast("0.e"));
		assertEquals(0.0f, CastTo.Float.cast("0.0"));
		assertEquals(0f, CastTo.Float.cast("0"));
		assertEquals(0f, CastTo.Float.cast(" 0  "));
		assertEquals(10f, CastTo.Float.cast(" 1e1 "));
		assertEquals(10f, CastTo.Float.cast('\n'));
		assertEquals(13f, CastTo.Float.cast('\r'));
		assertEquals(10f, CastTo.Float.cast(10));
		assertEquals(0f, CastTo.Float.cast(false));
		assertEquals(1f, CastTo.Float.cast(true));
	}
	
	@Test
	public void testCastDouble()
	{
		assertEquals(null, CastTo.Double.cast(null));
		assertEquals(null, CastTo.Double.cast("0.e"));
		assertEquals(0.0, CastTo.Double.cast("0.0"));
		assertEquals(0.0, CastTo.Double.cast("0"));
		assertEquals(0.0, CastTo.Double.cast(" 0  "));
		assertEquals(10.0, CastTo.Double.cast(" 1e1 "));
		assertEquals(10.0, CastTo.Double.cast('\n'));
		assertEquals(13.0, CastTo.Double.cast('\r'));
		assertEquals(10.0, CastTo.Double.cast(10));
		assertEquals(0.0, CastTo.Double.cast(false));
		assertEquals(1.0, CastTo.Double.cast(true));
		assertEquals(1e24, CastTo.Double.cast(" 1e24 "));
		assertEquals(1.1, CastTo.Double.cast(" 1.1 "));
		assertEquals(1.1, CastTo.Double.cast(" 1,1 "));
	}
	
	
	@Test
	public void testCastString()
	{
		assertEquals(null, CastTo.String.cast(null));
		assertEquals("", CastTo.String.cast(""));
		assertEquals("asdf", CastTo.String.cast("asdf"));
		assertEquals("asdf", CastTo.String.cast("asdf".getBytes()));
	}	
	
	
	@Test
	public void testCastDate()
	{
		assertEquals(null, CastTo.Date.cast(null));
		Date d = new Date();
		assertEquals(d, CastTo.Date.cast(d));
		assertEquals(null , CastTo.Date.cast("safdg"));
		assertEquals(TimeCalc.date(2015, 3, 14, 0, 0, 0, 0), CastTo.Date.cast("2015-03-14"));
		assertEquals(TimeCalc.dateUtc(2012, 1, 14, 15, 15, 44, 0), CastTo.Date.cast(1326554144000l));
		
		assertEquals(TimeCalc.dateUtc(1970, 1, 1, 0, 0, 1, 24), CastTo.Date.cast(1024));
	}
	
	@Test
	public void testCastObject()
	{
		assertEquals(null, CastTo.Object.cast(null));
		Object o = new Object();
		assertEquals(o, CastTo.Object.cast(o));
	}
	
	@Test
	public void testTypeCast()
	{
		assertEquals(CastTo.Boolean, CastTo.getCasterRestrictlyForTargetClass(boolean.class));
		assertEquals(CastTo.Boolean, CastTo.getCasterRestrictlyForTargetClass(Boolean.class));
		
		assertEquals(CastTo.Byte, CastTo.getCasterRestrictlyForTargetClass(byte.class));
		assertEquals(CastTo.Byte, CastTo.getCasterRestrictlyForTargetClass(Byte.class));
		
		assertEquals(CastTo.Char, CastTo.getCasterRestrictlyForTargetClass(char.class));
		assertEquals(CastTo.Char, CastTo.getCasterRestrictlyForTargetClass(Character.class));
		
		
		assertEquals(CastTo.Short, CastTo.getCasterRestrictlyForTargetClass(short.class));
		assertEquals(CastTo.Short, CastTo.getCasterRestrictlyForTargetClass(Short.class));
		
		
		assertEquals(CastTo.Int, CastTo.getCasterRestrictlyForTargetClass(int.class));
		assertEquals(CastTo.Int, CastTo.getCasterRestrictlyForTargetClass(Integer.class));
		
		
		assertEquals(CastTo.Long, CastTo.getCasterRestrictlyForTargetClass(long.class));
		assertEquals(CastTo.Long, CastTo.getCasterRestrictlyForTargetClass(Long.class));
		
		assertEquals(CastTo.Float, CastTo.getCasterRestrictlyForTargetClass(float.class));
		assertEquals(CastTo.Float, CastTo.getCasterRestrictlyForTargetClass(Float.class));
		
		assertEquals(CastTo.Double, CastTo.getCasterRestrictlyForTargetClass(double.class));
		assertEquals(CastTo.Double, CastTo.getCasterRestrictlyForTargetClass(Double.class));
		
		assertEquals(CastTo.String, CastTo.getCasterRestrictlyForTargetClass(String.class));
		assertEquals(CastTo.String, CastTo.getCasterRestrictlyForTargetClass(CharSequence.class));
		
		assertEquals(CastTo.Date, CastTo.getCasterRestrictlyForTargetClass(Date.class));
		assertEquals(CastTo.Date, CastTo.getCasterRestrictlyForTargetClass(java.sql.Date.class));
		
		assertEquals(CastTo.Object, CastTo.getCasterRestrictlyForTargetClass(Object.class));
	}
}
