package eu.javaexperience.text.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public class SlidingProcessor
{
	public static class SlidingProcessorExaminer
	{
		public String name;
		public GetBy1<String, String> examiner;
		
		public SlidingProcessorExaminer(String name, GetBy1<String, String> examiner)
		{
			this.name = name;
			this.examiner = examiner;
		}
		
		public void tryExamineAndPlace(SlidingProcessor sp, String unit)
		{
			String s = examiner.getBy(unit);
			if(null != s)
			{
				sp.current.put(name, s);
			}
		}
	}

	protected Map<String, String> current = new HashMap<>();
	protected Map<String, SlidingProcessorExaminer> examiners = new HashMap<>();
	
	public SlidingProcessor(SlidingProcessorExaminer... examiners)
	{
		for(SlidingProcessorExaminer e:examiners)
		{
			if(this.examiners.containsKey(e.name))
			{
				throw new RuntimeException("duplicated name: "+e.name);
			}
			
			this.examiners.put(e.name, e);
		}
	}
	
	public Map<String, String> getCurrentValues()
	{
		return current;
	}
	
	public void reset()
	{
		current.clear();
	}
	
	public boolean isAllFieldReady()
	{
		for(Entry<String, SlidingProcessorExaminer> kv:examiners.entrySet())
		{
			if(!current.containsKey(kv.getKey()))
			{
				return false;
			}
		}
		return true;
	}

	public void feed(String a)
	{
		for(Entry<String, SlidingProcessorExaminer> kv:examiners.entrySet())
		{
			kv.getValue().tryExamineAndPlace(this, a);
		}
	}

	public void fillFields(Object result)
	{
		Mirror.tryFillMapIntoObjectCast((Map)current, result);
	}
}
