package eu.javaexperience.collection.map;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TransactionMapTest// TODO extends MapGeneralTests
{
	public static TransactionMap<String, String> createTest()
	{
		SmallMap<String, String> o = new SmallMap<>();
		TransactionMap<String, String> ret = new TransactionMap<>
		(
			MapTools.inlineFill
			(
				o,
				"key", "value",
				"a", "A",
				"b", "B",
				"c", "C"
			)
		);
		
		assertEquals(o, ret.getOriginalMap());
		return ret;
	}
	
	@Test
	public void testPassTroughtAndAccess()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		assertEquals("A", tr.get("a"));
		assertEquals("B", tr.get("b"));
		assertEquals("C", tr.get("c"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("b"));
		assertTrue(tr.getAccessMap().containsKey("c"));
		
		assertTrue(tr.getDiffMap().isEmpty());
	}
	
	/**
	 * getting any knowledge about variable existence is also an "access"
	 * */
	@Test
	public void testKeysAndAccess()
	{
		TransactionMap<String, String> tr = createTest();
		Set<String> set = tr.keySet();
		assertEquals(4, set.size());
		assertTrue(set.contains("key"));
		assertTrue(set.contains("a"));
		assertTrue(set.contains("b"));
		assertTrue(set.contains("c"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("b"));
		assertTrue(tr.getAccessMap().containsKey("c"));
		
		assertEquals(4, tr.getAccessMap().size());
		assertEquals(4, tr.size());
	}
	
	@Test
	public void testPassContainsAndAccess()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		assertTrue(tr.containsKey("a"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		
		assertTrue(tr.getDiffMap().isEmpty());
		assertEquals(4, tr.size());
	}
	
	@Test
	public void testPassContainsAndAccess2()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		assertTrue(tr.containsKey("a"));
		assertFalse(tr.containsKey("NONE"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("NONE"));
		
		assertTrue(tr.getDiffMap().isEmpty());
		assertEquals(4, tr.size());
	}
	
	@Test
	public void testPassTroughtAndAccessPartial()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		assertEquals("C", tr.get("c"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertFalse(tr.getAccessMap().containsKey("a"));
		assertFalse(tr.getAccessMap().containsKey("b"));
		assertTrue(tr.getAccessMap().containsKey("c"));
		
		assertTrue(tr.getDiffMap().isEmpty());
		assertEquals(4, tr.size());
	}
	
	@Test
	public void testModifyAndAccess()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		assertEquals("A", tr.put("a", "a"));
		assertEquals("B", tr.put("b", "b"));
		assertEquals("C", tr.get("c"));
		
		assertEquals("a", tr.get("a"));
		assertEquals("b", tr.get("b"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("b"));
		assertTrue(tr.getAccessMap().containsKey("c"));
		
		assertTrue(tr.getDiffMap().containsKey("a"));
		assertTrue(tr.getDiffMap().containsKey("b"));
		
		assertEquals("a", tr.getDiffMap().get("a"));
		assertEquals("b", tr.getDiffMap().get("b"));
		assertEquals(4, tr.size());
	}
	
	@Test
	public void testModifyAndAccessExtra()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		assertEquals("A", tr.get("a"));
		assertEquals("B", tr.get("b"));
		assertEquals("A", tr.put("a", "x"));
		assertEquals("B", tr.put("b", "y"));
		assertEquals("C", tr.get("c"));
		
		assertEquals("x", tr.get("a"));
		assertEquals("y", tr.get("b"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("b"));
		assertTrue(tr.getAccessMap().containsKey("c"));
		
		assertTrue(tr.getDiffMap().containsKey("a"));
		assertTrue(tr.getDiffMap().containsKey("b"));
		assertEquals("x", tr.getDiffMap().get("a"));
		assertEquals("y", tr.getDiffMap().get("b"));
		assertEquals(4, tr.size());
	}
	
	@Test
	public void testModifyDoubleTimesAndAccess()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		assertEquals("A", tr.get("a"));
		assertEquals("B", tr.get("b"));
		assertEquals("A", tr.put("a", "a"));
		assertEquals("B", tr.put("b", "b"));
		assertEquals("a", tr.put("a", "a"));
		assertEquals("b", tr.put("b", "b"));
		assertEquals("C", tr.get("c"));
		
		assertEquals("a", tr.get("a"));
		assertEquals("b", tr.get("b"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("b"));
		assertTrue(tr.getAccessMap().containsKey("c"));
		
		assertTrue(tr.getDiffMap().containsKey("a"));
		assertTrue(tr.getDiffMap().containsKey("b"));
		
		assertEquals("a", tr.getDiffMap().get("a"));
		assertEquals("b", tr.getDiffMap().get("b"));
		assertEquals(4, tr.size());
	}
	
	@Test
	public void testModifyAndDeleteAndAccess()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		assertEquals("A", tr.get("a"));
		assertEquals("B", tr.get("b"));
		assertEquals("A", tr.put("a", "a"));
		assertEquals("B", tr.put("b", "b"));
		assertEquals("a", tr.put("a", "a"));
		assertEquals("b", tr.put("b", "b"));
		assertEquals("value", tr.remove("key"));
		
		
		assertEquals("a", tr.get("a"));
		assertEquals("b", tr.get("b"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("b"));
		
		assertTrue(tr.getDiffMap().containsKey("a"));
		assertTrue(tr.getDiffMap().containsKey("b"));
		
		assertEquals(TransactionMap.NULL_VALUE, tr.getDiffMap().get("key"));
		assertEquals("a", tr.getDiffMap().get("a"));
		assertEquals("b", tr.getDiffMap().get("b"));
		
		assertEquals(3, tr.size());
	}
	
	@Test
	public void testClear()
	{
		TransactionMap<String, String> tr = createTest();
		tr.clear();
		
		assertNull(tr.get("key"));
		assertNull(tr.get("a"));
		assertNull(tr.get("b"));
		assertNull(tr.get("c"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("b"));
		assertTrue(tr.getAccessMap().containsKey("c"));
		
		assertNull(tr.get("key"));
		assertNull(tr.get("a"));
		assertNull(tr.get("b"));
		assertNull(tr.get("c"));

		assertEquals(0, tr.size());
	}
	
	@Test
	public void testDeleteThenInsert()
	{
		TransactionMap<String, String> tr = createTest();
		tr.clear();
		
		assertNull(tr.get("key"));
		assertNull(tr.get("a"));
		assertNull(tr.get("b"));
		assertNull(tr.get("c"));
		
		assertNull(tr.put("key", "new"));
		assertNull(tr.put("a", "X"));
		assertNull(tr.put("b", "Y"));
		assertNull(tr.put("c", "Z"));
		
		assertTrue(tr.getAccessMap().containsKey("key"));
		assertTrue(tr.getAccessMap().containsKey("a"));
		assertTrue(tr.getAccessMap().containsKey("b"));
		assertTrue(tr.getAccessMap().containsKey("c"));
		
		assertEquals("value", tr.getAccessMap().get("key"));
		assertEquals("A", tr.getAccessMap().get("a"));
		assertEquals("B", tr.getAccessMap().get("b"));
		assertEquals("C", tr.getAccessMap().get("c"));
		
		assertEquals("new", tr.get("key"));
		assertEquals("X", tr.get("a"));
		assertEquals("Y", tr.get("b"));
		assertEquals("Z", tr.get("c"));

		
		assertEquals(4, tr.size());
	}
	
	@Test
	public void addNewValue()
	{
		TransactionMap<String, String> tr = createTest();
		
		tr.put("x", "X");
		tr.put("y", "Y");
		tr.put("z", "Z");
		
		assertFalse("value", tr.getAccessMap().containsKey("key"));
		assertFalse("A", tr.getAccessMap().containsKey("a"));
		assertFalse("B", tr.getAccessMap().containsKey("b"));
		assertFalse("C", tr.getAccessMap().containsKey("c"));

		assertTrue(tr.getAccessMap().containsKey("x"));
		assertTrue(tr.getAccessMap().containsKey("y"));
		assertTrue(tr.getAccessMap().containsKey("z"));
		
		assertEquals("value", tr.get("key"));
		assertEquals("X", tr.get("x"));
		assertEquals("Y", tr.get("y"));
		assertEquals("Z", tr.get("z"));
		
		assertEquals(7, tr.size());
	}
	
	@Test
	public void addThenDeleteValue()
	{
		TransactionMap<String, String> tr = createTest();
		assertNull(tr.put("new", "asd"));
		assertEquals(5, tr.size());
		
		assertEquals("asd", tr.remove("new"));
		
		assertEquals(4, tr.size());
		
		assertTrue(tr.getDiffMap().containsKey("new"));
		assertEquals(1, tr.getDiffMap().size());
		assertNull(tr.getAccessTimeValue("new"));
		assertNull(tr.get("new"));
	}
	
	
	/**
	 * Well diff must remember the modification of the underlying value
	 * but we can check that it is really changed
	 * */
	public void modifyAndRevert()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals("value", tr.get("key"));
		
		//modify
		assertEquals("A", tr.put("a", "a"));
		assertEquals("B", tr.put("b", "b"));
		
		assertEquals(4, tr.size());
		
		assertEquals("value", tr.remove("key"));
		
		//revert
		assertEquals("a", tr.put("a", "A"));
		assertEquals("b", tr.put("b", "B"));
		
		assertNull(tr.put("key", "value"));
		
		//check
		assertEquals(3, tr.getDiffMap().size());
		
		assertEquals("value", tr.getAccessTimeValue("key"));
		assertEquals("value", tr.get("key"));
		
		assertEquals("A", tr.getAccessTimeValue("a"));
		assertEquals("A", tr.get("a"));
		
		assertEquals("B", tr.getAccessTimeValue("b"));
		assertEquals("B", tr.get("b"));
		
		//nothing changed but accessed and touched
	}
	
	@Test
	public void lazyAccessValue()
	{
		TransactionMap<String, String> tr = createTest();
		tr.get("a");
		assertNull(tr.getAccessTimeValue("key"));
		assertEquals("A", tr.getAccessTimeValue("a"));
	}
	
	@Test
	public void note_SizeTouchesAccess()
	{
		TransactionMap<String, String> tr = createTest();
		assertEquals(4, tr.size());
		
		assertEquals(0, tr.getDiffMap().size());
		assertEquals(4, tr.getAccessMap().size());
	}
}
