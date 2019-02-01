package eu.javaexperience.reflect;

import java.util.List;
import java.util.Map;

public class DataPath
{
	/**
	 * o-ból ami lehet list vagy Map kiveszi a megfelelő 
	 * */
	public static Object get(Object o,Object... path)
	{
		return recursiveGet(o, path, 0);
	}
	
	private static Object recursiveGet(Object o,Object[] path, int index)
	{
		Object ret = get(o,path[index++]);
		if(ret == null)
			return null;

		if(index == path.length)
			return ret;
		
		return recursiveGet(ret, path, index);
	}
	
	private static Object get(Object c,Object k)
	{
		if(k instanceof Number && c instanceof List)
		{
			List<?> l = (List<?>) c;
			int i = ((Number)k).intValue();
			if(l.size() > i)
				return l.get(i);
		}
		else if(k instanceof String && c instanceof Map)
		{
			Map<String,?> m = (Map<String,?>) c;
			return m.get(k);
		}
		
		return null;
	}
}