package eu.javaexperience.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.SmallMap;

public class LogicalGroup// implements ObjectLike
{
	public static LogicalGroup[] emptyLogicalGroupArray = new LogicalGroup[0]; 
	
	private final LogicalRelation lr;
	private final LogicalGroup[] components;
	private final AtomicCondition ac;
	
	public LogicalGroup(AtomicCondition ac)
	{
		this.ac = ac;
		components = null;
		lr = LogicalRelation.unit;
	}

	public LogicalGroup(LogicalRelation lr, LogicalGroup[] grp)
	{
		this.ac = null;
		components = grp;
		this.lr = lr;
	}
	
	public LogicalRelation getLogicalRelation()
	{
		return lr;
	}
	
	public AtomicCondition getAtomicCondition()
	{
		return ac;
	}
	
	public LogicalGroup[] getLogicalGroups()
	{
		return components;
	}
	
	@Override
	public String toString()
	{
		if(null != ac)
		{
			return ac.toString();
		}
		
		StringBuilder sb = new StringBuilder();
		
		for(LogicalGroup c:components)
		{
			if(sb.length() > 0)
			{
				sb.append(" ");
				sb.append(lr.name());
				sb.append(" ");
			}
			sb.append(c.toString());
		}
		
		return sb.toString();
	}
	
	public static LogicalGroup parse(Map<String, Object> obj)
	{
		String relation = (String) obj.get("r");
		Map<String, Object> atomicCondidition = (Map<String, Object>) obj.get("a");
		List<Map<String,Object>> comp = CollectionTools.tryWrapToList(obj.get("c"), "components");
		
		if(null != atomicCondidition)
		{
			return new LogicalGroup(AtomicCondition.parse(atomicCondidition));
		}
		else
		{
			LogicalGroup[] lrs = new LogicalGroup[comp.size()];
			for(int i=0;i<lrs.length;++i)
			{
				lrs[i] = parse(comp.get(i));
			}
			
			return new LogicalGroup(LogicalRelation.valueOf(relation), lrs);
		}
	}
	
	public void write(Map<String, Object> to)
	{
		to.put("r", lr.name());
		if(null != ac)
		{
			Map<String, Object> map = new SmallMap<>();
			ac.write(map);
			to.put("a", map);
		}
		else
		{
			ArrayList<Map<String,Object>> lst = new ArrayList<>();
			for(LogicalGroup c:components)
			{
				Map<String, Object> map = new SmallMap<>();
				c.write(map);
				lst.add(map);
			}
			to.put("c", lst);
		}
	}
	
	/*
	protected static final HashSet<String> fields = new HashSet<>();
	static
	{
		fields.add("r");
		fields.add("c");
		fields.add("a");
	} 
	 
	@Override
	public Object get(String key)
	{
		if(null == key)
		{
			return null;
		}
		
		switch (key)
		{
			case "r":	return lr.name();
			case "g":	return components;
			case "a":	return ac;
			default:	return null;
		}
	}
	
	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.OBJECT;
	}

	@Override
	public boolean has(String key)
	{
		return fields.contains(key);
	}

	@Override
	public String[] keys()
	{
		return fields.toArray(Mirror.emptyStringArray);
	}

	@Override
	public int size()
	{
		return 3;
	}
	
	public static LogicalGroup parse(DataObject obj)
	{
		String relation = obj.optString("r");
		DataObject atomicCondidition = obj.optObject("a");
		DataArray comp = obj.optArray("c");
		
		if(null != atomicCondidition)
		{
			return new LogicalGroup(AtomicCondition.parse(atomicCondidition));
		}
		else
		{
			LogicalGroup[] lrs = new LogicalGroup[comp.size()];
			for(int i=0;i<lrs.length;++i)
			{
				lrs[i] = parse(comp.getObject(i));
			}
			
			return new LogicalGroup(LogicalRelation.valueOf(relation), lrs);
		}
	}*/
}