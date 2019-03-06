package eu.javaexperience.datastorage;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Test;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.KeyVal;

public abstract class DataStorageTest
{
	protected DataStorage ds;
	
	public DataStorageTest(DataStorage ds)
	{
		this.ds = ds;
	}
	
	public static <E extends Entry<String, String>> void matchEntrySet(Set<Entry<String, Object>> ents, E... req)
	{
		ArrayList<E> rem = new ArrayList<>();
		HashSet<Entry<String, Object>> test = new HashSet<>();
		test.addAll(ents);
		for(E e:req)
		{
			if(!test.remove(e))
			{
				rem.add(e);
			}
		}
		
		if(!test.isEmpty() || !rem.isEmpty())
		{
			StringBuilder sb = new StringBuilder();
			if(!test.isEmpty())
			{
				sb.append("Unwanted elements in result:\n");
				sb.append(CollectionTools.toStringMultiline(test));
				sb.append("\n");
			}
			
			if(!rem.isEmpty())
			{
				sb.append("Expected but not found elements:\n");
				sb.append(CollectionTools.toStringMultiline(rem));
			}
			
			throw new RuntimeException("EntrySetNot Matches: "+sb.toString());
		}
	}
	
	@Test
	public void test() throws Throwable
	{
		try
		{
			try(DataTransaction tr = ds.startTransaction("t1"))
			{
				tr.put("a", 1);
				tr.put("b", 2);
				tr.put("b.a", 3);
				tr.put("b.b", 4);
				tr.put("b.c", 5);
				tr.commit();
			}
			
			try(DataTransaction tr = ds.startTransaction("t1"))
			{
				matchEntrySet
				(
					tr.entrySet(),
					new KeyVal<>("a", "1"),
					new KeyVal<>("b", "2"),
					new KeyVal<>("b.a", "3"),
					new KeyVal<>("b.b", "4"),
					new KeyVal<>("b.c", "5")
				);
			}
			
			try(DataTransaction tr = ds.startTransaction("t1.b"))
			{
				matchEntrySet
				(
					tr.entrySet(),
					new KeyVal<>("a", "3"),
					new KeyVal<>("b", "4"),
					new KeyVal<>("c", "5")
				);
			}
			
			try(DataTransaction tr = ds.startTransaction(""))
			{
				matchEntrySet
				(
					tr.entrySet(),
					new KeyVal<>("t1.a", "1"),
					new KeyVal<>("t1.b", "2"),
					new KeyVal<>("t1.b.a", "3"),
					new KeyVal<>("t1.b.b", "4"),
					new KeyVal<>("t1.b.c", "5")
				);
			}
		}
		finally
		{
			try(DataTransaction tr = ds.startTransaction("t1"))
			{
				tr.clear();
				tr.commit();
			}
			
			try(DataTransaction tr = ds.startTransaction("t1"))
			{
				assertTrue(tr.keySet().isEmpty());
			}	
		}
	}
}
