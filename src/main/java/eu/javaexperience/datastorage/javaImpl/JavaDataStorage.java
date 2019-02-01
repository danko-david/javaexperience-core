package eu.javaexperience.datastorage.javaImpl;

import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.datastorage.DataTransaction;
import eu.javaexperience.datastorage.DataStorage;
import eu.javaexperience.interfaces.simple.SimpleGetFactory;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetByTools;

public class JavaDataStorage implements DataStorage
{
	protected final Map<String, Map<String, Object>> root;
	protected final GetBy1<Map<String, Object>, String> newMap;
	
	public JavaDataStorage(Map<String, Map<String, Object>> root, GetBy1<Map<String, Object>, String> creator)
	{
		this.root = root;
		this.newMap = creator;
	}
	
	public static JavaDataStorage createDefault()
	{
		return new JavaDataStorage(new HashMap(), (GetBy1)GetByTools.wrapSimpleGet(SimpleGetFactory.getHashMapFactory()));
	}
	
	public Map<String, Map<String, Object>> getBackendStorage()
	{
		return root;
	}
	
	@Override
	public DataTransaction startTransaction(String key)
	{
		synchronized (this)
		{
			Map<String, Object> ret = root.get(key);
			if(null == ret)
			{
				root.put(key, ret = newMap.getBy(key)); 
			}
			
			return new JavaDataTransaction(ret);
		}
	}
}
