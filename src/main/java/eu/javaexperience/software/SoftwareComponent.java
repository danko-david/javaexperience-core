package eu.javaexperience.software;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.interfaces.ObjectWithProperty;
import eu.javaexperience.reflect.Mirror;

public class SoftwareComponent implements ObjectWithProperty
{
	protected static final SoftwareComponent SOFTWARE_COMPONENT_ROOT = new SoftwareComponent();
	
	public static SoftwareComponent getRoot()
	{
		return SOFTWARE_COMPONENT_ROOT;
	}
	
	protected final ConcurrentMap<String, Object> root = new ConcurrentHashMap<>();
	
	public Object getComponent(String key)
	{
		return root.get(key);
	}
	
	public void registerComponent(String key, Object object)
	{
		if(null != root.putIfAbsent(key, object))
		{
			throw new RuntimeException("Software component already registered: `"+key+"`");
		}
	}
	
	public void unregisterComponent(String key)
	{
		if(null == root.remove(key))
		{
			throw new RuntimeException("Software component doesn't exists: `"+key+"`");
		}
	}

	@Override
	public Object get(String key)
	{
		return root.get(key);
	}

	@Override
	public String[] keys()
	{
		return root.keySet().toArray(Mirror.emptyStringArray);
	}
}
