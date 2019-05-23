package eu.javaexperience.query;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.reflect.Mirror;

public class TestQueryEvaluator
{
	public static class EvalTestObject
	{
		public Integer id;
		public String str;
		
		public EvalTestObject(Integer id, String str)
		{
			this.id = id;
			this.str = str;
		}
		
		@Override
		public String toString()
		{
			return Mirror.usualToString(this);
		}
	}
	
	public static List<EvalTestObject> createTestCollection()
	{
		List<EvalTestObject> ret = new ArrayList<>();
		ret.add(new EvalTestObject(1, "Test"));
		ret.add(new EvalTestObject(2, "test"));
		ret.add(new EvalTestObject(3, "Hello World"));
		ret.add(new EvalTestObject(4, "asdg"));
		ret.add(new EvalTestObject(5, "Hello_World"));
		
		return ret;
	}
	
	public static QueryEvaluator<EvalTestObject> createEvaluator()
	{
		return new QueryEvaluator<>(new QueryEvaluatorBuilder<EvalTestObject>().build());
	}
	
	@Test
	public void testEq()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.eq.is("str", "Test"));
		
		assertEquals(1, to.size());
		
		assertEquals("Test", to.get(0).str);
		assertEquals((Integer) 1, to.get(0).id);
	}
	

	@Test
	public void testCmp()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.lte.is("id", 3));
		
		assertEquals(3, to.size());
		
		assertEquals("Test", to.get(0).str);
		assertEquals((Integer) 1, to.get(0).id);
		
		assertEquals("test", to.get(1).str);
		assertEquals((Integer) 2, to.get(1).id);
		
		assertEquals("Hello World", to.get(2).str);
		assertEquals((Integer) 3, to.get(2).id);
		
		to.clear();
		qe.select(to, from, F.gte.is("id", 3));
		assertEquals(3, to.size());
		
		assertEquals("Hello World", to.get(0).str);
		assertEquals((Integer) 3, to.get(0).id);
		
		assertEquals("asdg", to.get(1).str);
		assertEquals((Integer) 4, to.get(1).id);
		
		assertEquals("Hello_World", to.get(2).str);
		assertEquals((Integer) 5, to.get(2).id);
		
		
		to.clear();
		qe.select(to, from, F.eq.is("id", 3));
		assertEquals(1, to.size());
		
		assertEquals("Hello World", to.get(0).str);
		assertEquals((Integer) 3, to.get(0).id);
	}

	@Test
	public void testIn1()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.in.is("id", CollectionTools.inlineAdd(new ArrayList<>(), 2, 3, 5, 10)));
		
		assertEquals(3, to.size());
		
		assertEquals("test", to.get(0).str);
		assertEquals((Integer) 2, to.get(0).id);
		
		assertEquals("Hello World", to.get(1).str);
		assertEquals((Integer) 3, to.get(1).id);
		
		assertEquals("Hello_World", to.get(2).str);
		assertEquals((Integer) 5, to.get(2).id);
		
		to.clear();
		qe.select(to, from, F.in.is("id", new ArrayList<>()));
		assertEquals(0, to.size());
	}
	
	@Test
	public void testIn2()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.in.is("str", CollectionTools.inlineAdd(new ArrayList<>(), "test", "asdg", "Hello World", "random")));
		
		assertEquals(3, to.size());
		
		assertEquals("test", to.get(0).str);
		assertEquals((Integer) 2, to.get(0).id);
		
		assertEquals("Hello World", to.get(1).str);
		assertEquals((Integer) 3, to.get(1).id);
		
		assertEquals("asdg", to.get(2).str);
		assertEquals((Integer) 4, to.get(2).id);
		
		
		to.clear();
		qe.select(to, from, F.in.is("id", new ArrayList<>()));
		assertEquals(0, to.size());
	}
	
	@Test
	public void testMatches()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.match.is("str", "^H.*_.*$"));
		
		assertEquals(1, to.size());
		
		assertEquals("Hello_World", to.get(0).str);
		assertEquals((Integer) 5, to.get(0).id);
		
		
		to.clear();
		qe.select(to, from, F.match.is("str", "^H.*"));
		
		assertEquals(2, to.size());
		
		assertEquals("Hello World", to.get(0).str);
		assertEquals((Integer) 3, to.get(0).id);
		
		assertEquals("Hello_World", to.get(1).str);
		assertEquals((Integer) 5, to.get(1).id);
	}
	
	@Test
	public void testNullNoResult()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		List<EvalTestObject> to = new ArrayList<>();
		
		qe.select(to, from, F.contains.is("str", null));
		assertEquals(0, to.size());
		
		//OK this may result object but there's no value with null in the set  
		qe.select(to, from, F.eq.is("str", null));
		assertEquals(0, to.size());
		
		qe.select(to, from, F.gt.is("str", null));
		assertEquals(0, to.size());
		
		qe.select(to, from, F.gte.is("str", null));
		assertEquals(0, to.size());
		
		qe.select(to, from, F.in.is("str", null));
		assertEquals(0, to.size());
		
		qe.select(to, from, F.lt.is("str", null));
		assertEquals(0, to.size());
		
		qe.select(to, from, F.lte.is("str", null));
		assertEquals(0, to.size());
		
		qe.select(to, from, F.match.is("str", null));
		assertEquals(0, to.size());
	}
	
/*	@Test
	public void test_()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.eq.is("do", null));
		
		assertEquals(3, to.size());
		
		assertEquals("Test", to.get(0).str);
		assertEquals((Integer) 1, to.get(0).id);
		
		assertEquals("test", to.get(1).str);
		assertEquals((Integer) 2, to.get(1).id);
		
		assertEquals("Hello World", to.get(2).str);
		assertEquals((Integer) 3, to.get(2).id);
		
		to.clear();
		qe.select(to, from, F.gte.is("id", 3));
		assertEquals(3, to.size());
		
		assertEquals("Hello World", to.get(0).str);
		assertEquals((Integer) 3, to.get(0).id);
		
		assertEquals("asdg", to.get(1).str);
		assertEquals((Integer) 4, to.get(1).id);
		
		assertEquals("Hello_World", to.get(2).str);
		assertEquals((Integer) 5, to.get(2).id);
		
		
		to.clear();
		qe.select(to, from, F.eq.is("id", 3));
		assertEquals(1, to.size());
		
		assertEquals("Hello World", to.get(0).str);
		assertEquals((Integer) 3, to.get(0).id);
	}
*/
	@Test
	public void testMatchCollection()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.match.is("str", CollectionTools.inlineArrayList("^H.*", ".*rld$")));
		
		assertEquals(2, to.size());
		
		assertEquals("Hello World", to.get(0).str);
		assertEquals((Integer) 3, to.get(0).id);
		
		assertEquals("Hello_World", to.get(1).str);
		assertEquals((Integer) 5, to.get(1).id);
	}
	
}
