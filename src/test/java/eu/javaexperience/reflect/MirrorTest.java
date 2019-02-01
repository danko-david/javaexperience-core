package eu.javaexperience.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.Date;

import org.junit.Test;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.reflect.Mirror.ClassData;
import eu.javaexperience.time.TimeCalc;

public class MirrorTest
{
	public static class TestDiscoverMe
	{
		private TestDiscoverMe(){};
		
		public static int props;
		
		private String privData;
		protected Date date;
		public int id;
	}
	
	@Test
	public void testClassData()
	{
		ClassData cd = Mirror.getClassData(TestDiscoverMe.class);
		assertNotNull(cd.getFieldByName("privData"));
		
		Field[] fds = cd.selectFields(FieldSelectTools.SELECT_ALL_PUBLIC_INSTANCE_FIELD);
		
		assertEquals(3, cd.selectFields(FieldSelectTools.SELECT_ALL_INSTANCE_FIELD).length);
		assertEquals(1, fds.length);
		assertEquals(cd.getFieldByName("id"), fds[0]);
	}
	
	public static class TestPrintMe
	{
		private Object secret;
		protected int id;
		public Date date;
	}
	
	@Test
	public void testUsualToString()
	{
		TestPrintMe tdm = new TestPrintMe();
		tdm.id = 10;
		tdm.date = TimeCalc.dateUtc(2017, 3, 12, 10, 13, 14, 123);
		assertEquals("TestPrintMe: {secret: null, id: 10, date: 2017-03-12 10:13:14.123U}", Mirror.usualToString(tdm));
	}

	@Test
	public void testUsualToString2()
	{
		TestPrintMe tdm = new TestPrintMe();
		tdm.secret = MapTools.inlineSmallMap("a", 2, "b", 4);
		tdm.id = 10;
		tdm.date = TimeCalc.dateUtc(2017, 3, 12, 10, 13, 14, 123);
		assertEquals("TestPrintMe: {secret: {\"a\":\"2\", \"b\":\"4\"}, id: 10, date: 2017-03-12 10:13:14.123U}", Mirror.usualToString(tdm));
	}
	
	
	
	
}
