package eu.javaexperience.query;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.time.TimeCalc;

public class TestQueryEvaluator
{
	public static class EvalTestObject
	{
		public Integer id;
		public String str;
		
		protected List<String> state = new ArrayList<>();
		
		public EvalTestObject(Integer id, String str, String... states)
		{
			this.id = id;
			this.str = str;
			CollectionTools.inlineAdd(state, states);
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
		ret.add(new EvalTestObject(1, "Test", "ACTIVE", "PENDING"));
		ret.add(new EvalTestObject(2, "test", "ACTIVE"));
		ret.add(new EvalTestObject(3, "Hello World", "CLOSED"));
		ret.add(new EvalTestObject(4, "asdg", "COLSED"));
		ret.add(new EvalTestObject(5, "Hello_World", "CLOSED", "IRRELEVANT"));
		
		return ret;
	}
	
	public static <T> QueryEvaluator<T> createEvaluator()
	{
		return new QueryEvaluator<>(new QueryEvaluatorBuilder<T>().build());
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
	
	@Test
	public void testInCollection()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.in.is("state", CollectionTools.inlineArrayList("ACTIVE")));
		
		assertEquals(2, to.size());

		assertEquals("Test", to.get(0).str);
		assertEquals((Integer) 1, to.get(0).id);
		
		assertEquals("test", to.get(1).str);
		assertEquals((Integer) 2, to.get(1).id);
		
		
		to.clear();
		qe.select(to, from, F.in.is("state", CollectionTools.inlineArrayList("ACTIVE", "IRRELEVANT")));
		
		assertEquals(3, to.size());
		
		assertEquals("Test", to.get(0).str);
		assertEquals((Integer) 1, to.get(0).id);
		
		assertEquals("test", to.get(1).str);
		assertEquals((Integer) 2, to.get(1).id);
		
		assertEquals("Hello_World", to.get(2).str);
		assertEquals((Integer) 5, to.get(2).id);
	}
	
	@Test
	public void testInCollectionNonArray()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.in.is("state", "ACTIVE"));
		
		assertEquals(2, to.size());

		assertEquals("Test", to.get(0).str);
		assertEquals((Integer) 1, to.get(0).id);
		
		assertEquals("test", to.get(1).str);
		assertEquals((Integer) 2, to.get(1).id);		
	}

	@Test
	public void testInCollectionNoParamNoMatch()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.in.is("state", CollectionTools.inlineArrayList()));
		
		assertEquals(0, to.size());
	}
	
	@Test
	public void testIndirectContainsAll()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select(to, from, F.eq.is("state", CollectionTools.inlineArrayList("ACTIVE", "PENDING")));
		
		assertEquals(1, to.size());
		
		assertEquals("Test", to.get(0).str);
		assertEquals((Integer) 1, to.get(0).id);
	}
	
	@Test
	public void testAnd()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator(); 
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select
		(
			to,
			from,
			L.and
			(
				F.in.is("state", "ACTIVE"),
				F.gt.is("id", 1)
			)
		);
		
		assertEquals(1, to.size());
		
		assertEquals("test", to.get(0).str);
		assertEquals((Integer) 2, to.get(0).id);
	}

	@Test
	public void testOr()
	{
		List<EvalTestObject> from = createTestCollection();
		
		QueryEvaluator<EvalTestObject> qe = createEvaluator();
		
		List<EvalTestObject> to = new ArrayList<>();
		qe.select
		(
			to,
			from,
			L.or
			(
				F.in.is("state", "IRRELEVANT"),
				F.lt.is("id", 2)
			)
		);
		
		assertEquals(2, to.size());
		
		assertEquals("Test", to.get(0).str);
		assertEquals((Integer) 1, to.get(0).id);
		
		assertEquals("Hello_World", to.get(1).str);
		assertEquals((Integer) 5, to.get(1).id);
	}

	@Test
	public void testFindDinamic()
	{
		List<Object> from = new ArrayList<>();
		
		from.add(new Object()
		{
			public String field1 = "value";
		});
		
		from.add(new Object()
		{
			public Date createDate = TimeCalc.dateUtc(2019, 5, 10, 10, 23, 0, 0);
		});
		
		from.add(new Object()
		{
			public Date createDate = TimeCalc.dateUtc(2018, 10, 10, 10, 23, 0, 0);
		});
		
		from.add(new Object()
		{
			public Integer id = 10;
		});
		
		QueryEvaluator<Object> qe = createEvaluator();
		
		List<Object> to = new ArrayList<>();
		
		qe.select(to, from, F.gt.is("createDate", TimeCalc.dateUtc(2019, 0, 0, 0, 0, 0, 0)));
		assertEquals(1, to.size());
		to.clear();
		
		qe.select(to, from, F.lt.is("createDate", TimeCalc.dateUtc(2020, 0, 0, 0, 0, 0, 0)));
		assertEquals(2, to.size());
		to.clear();
		
		qe.select(to, from, F.eq.is("id", 10));
		assertEquals(1, to.size());
		to.clear();
		
		qe.select(to, from, F.eq.not("createDate", null));
		assertEquals(2, to.size());
		to.clear();
	}

}
