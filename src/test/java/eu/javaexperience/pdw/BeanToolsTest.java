package eu.javaexperience.pdw;

import static org.junit.Assert.*;

import org.junit.Test;

public class BeanToolsTest
{
	/**
	 * getAsdf => ["get", "Asdf"]
	 * isAsdf => ["is", "Asdf"]
	 * setAsdf => ["set", "Asdf"]
	 * get =>["get"]
	 * "" => [""]
	 * "GetValue" => ["GetValue"]
	 * "nextValue" => ["next", "Value"]
	 * 
	 * "CompareAndSet"
	 * "casMyValue" => ["cas", "MyValue"]
	 * */
	@Test
	public void testDecomposeCommand()
	{
		assertArrayEquals(new String[]{"get", "Asdf"}, BeanTools.decomposeCommand("getAsdf"));
		assertArrayEquals(new String[]{"is", "Asdf"}, BeanTools.decomposeCommand("isAsdf"));
		assertArrayEquals(new String[]{"set"}, BeanTools.decomposeCommand("set"));
		assertArrayEquals(new String[]{""}, BeanTools.decomposeCommand(""));
		assertArrayEquals(new String[]{"GetValue"}, BeanTools.decomposeCommand("GetValue"));
		assertArrayEquals(new String[]{"next", "Value"}, BeanTools.decomposeCommand("nextValue"));
		assertArrayEquals(new String[]{"cas", "MyValue"}, BeanTools.decomposeCommand("casMyValue"));
	}
	
	@Test
	public void testCBeanNames()
	{
		assertEquals("value", BeanTools.getCLikeBeanNameFromMethodName("getValue"));
		assertEquals("method_name", BeanTools.getCLikeBeanNameFromMethodName("getMethodName"));
		assertEquals(null, BeanTools.getCLikeBeanNameFromMethodName("get"));
		assertEquals(null, BeanTools.getCLikeBeanNameFromMethodName("set"));
		assertEquals("value_on_reset", BeanTools.getCLikeBeanNameFromMethodName("getValueOnReset"));
		
		assertEquals("value_on_reset", BeanTools.getCLikeBeanNameFromMethodName("setValueOnReset"));
		
		assertEquals("value_on_reset", BeanTools.getCLikeBeanNameFromMethodName("jumpValueOnReset"));
		
	}
	
	
}
