package eu.javaexperience.database.accessModel;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import eu.javaexperience.collection.map.MultiMap;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.collection.set.NullSet;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public abstract class AccessRequest<M, ENV> 
{
	protected DataAccessModel<M, ENV> model;
	
	protected MultiMap<OptionGroup, OptionEntry> selectedOptions = new MultiMap<>(new SmallMap<OptionGroup, List<OptionEntry>>());
	protected MultiMap<OptionGroup, String> unboundValue = new MultiMap<>(new SmallMap<OptionGroup, List<String>>());
	
	public void examineRequest(GetBy1<Object, String> src)
	{
		for(OptionGroup g:model.queryOptions.values())
		{
			Object in = src.getBy(g.systemName);
			typeCastOptionPut(g, in);
		}
	}
	
	protected void typeCastOptionPut(OptionGroup g, Object in)
	{
		if(null == in)
		{
			return;
		}
		
		if(in instanceof String)
		{
			examineAndPutSelectedOption(g, (String) in);
		}
		else if(in.getClass().isArray())
		{
			int len = Array.getLength(in);
			for(int i=0;i<len;++i)
			{
				Object at = Array.get(in, i);
				typeCastOptionPut(g, at);
			}
		}
		else if(in instanceof Collection)
		{
			Collection<Object> coll = (Collection<Object>) in;
			for(Object o:coll)
			{
				typeCastOptionPut(g, o);
			}
		}
	}
	
	protected void examineAndPutSelectedOption(OptionGroup grp, String value)
	{
		for(OptionEntry e:grp.options)
		{
			if(e.optionAddress.equals(value))
			{
				selectedOptions.put(grp, e);
				return;
			}
		}
		
		unboundValue.put(grp, value);
	}
	
	public OptionGroup getOptionGroupByName(String name)
	{
		for(OptionGroup g:model.queryOptions.values())
		{
			if(g.systemName.equals(name))
			{
				return g;
			}
		}
		
		return null;
	}
	
	public Iterable<OptionEntry> getBoundedValues(String optionName)
	{
		OptionGroup grp = getOptionGroupByName(optionName);
		if(null != grp)
		{
			List<OptionEntry> bound = selectedOptions.getList(grp);
			if(null != bound)
			{
				return bound;
			}
		}
		
		return (Iterable<OptionEntry>) NullSet.instance;
	}
	
	public OptionEntry getFirstBoundedValue(String grp)
	{
		Iterable<OptionEntry> re = getBoundedValues(grp);
		for(OptionEntry e:re)
		{
			return e;
		}
		
		return null;
	}
	
	public String getFirstBoundedValueAsString(String grp)
	{
		Iterable<OptionEntry> re = getBoundedValues(grp);
		for(OptionEntry e:re)
		{
			return e.optionAddress;
		}
		
		return null;
	}
	
	public Iterable<String> getUnboundedValue(String optionName)
	{
		OptionGroup grp = getOptionGroupByName(optionName);
		if(null != grp)
		{
			List<String> bound = unboundValue.getList(grp);
			if(null != bound)
			{
				return bound;
			}
		}
		
		return (Iterable<String>) NullSet.instance;
	}
	
	public String getFirstUnboundValue(String grp)
	{
		Iterable<String> re = getUnboundedValue(grp);
		for(String e:re)
		{
			return e;
		}
		
		return null;
	}
	
	public abstract void executeRequest(ENV env, Collection<M> ret) throws SQLException, IOException;

	public void getParams(Map<String, String[]> opts, boolean bound, boolean unbound)
	{
		ArrayList<String> all = new ArrayList<>();
		for(OptionGroup og:model.queryOptions.values())
		{
			all.clear();
			
			if(bound)
			{
				List<OptionEntry> lst = selectedOptions.getList(og);
				if(null != lst)
				{
					for(OptionEntry e:lst)
					{
						all.add(e.optionAddress);
					}
				}
			}
			
			if(unbound)
			{
				List<String> lst = unboundValue.getList(og);
				if(null != lst)
				{
					for(String e:lst)
					{
						all.add(e);
					}
				}
			}
			
			if(all.size() > 0)
			{
				opts.put(og.systemName, all.toArray(Mirror.emptyStringArray));
			}
		}
	}
}
