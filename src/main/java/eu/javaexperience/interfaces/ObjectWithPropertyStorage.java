package eu.javaexperience.interfaces;

import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public class ObjectWithPropertyStorage<T>
{
	protected Map<String, GetBy1<Object, T>> examiners = new HashMap<>();
	
	public void addExaminer(String key, GetBy1<Object, T> ex)
	{
		examiners.put(key, ex);
	}
	
	public String[] keys()
	{
		return examiners.keySet().toArray(Mirror.emptyStringArray);
	}
	
	public Object get(T object, String key)
	{
		GetBy1<Object, T> ex = examiners.get(key);
		if(null != ex)
		{
			return ex.getBy(object);
		}
		
		return null;
	}
}
