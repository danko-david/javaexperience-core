package eu.javaexperience.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;

public class CallPublic1ArgStaticOrGivenMethod<T>
{
	private final Map<String,Method> meths = new SmallMap<>();
	private final SimplePublish1<T> def;
	
	protected final Class<?> cls;
	
	public Class<?> getTargetClass()
	{
		return cls;
	}
	
	public CallPublic1ArgStaticOrGivenMethod(Class<?> cls,SimplePublish1<T> orDefault)
	{
		this.cls = cls;
		def = orDefault;
		for(Method m:cls.getMethods())
			if(Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()) && m.getParameterTypes().length == 1)
				meths.put(m.getName(), m);
	}

	public boolean invoke(String name,T arg) throws Throwable
	{
		try
		{
			Method m = meths.get(name);
			if(m != null)
			{
				m.invoke(null,arg);
				return true;
			}
			else if(def != null)
			{
				def.publish(arg);
			}
			return false;
		}
		catch(InvocationTargetException tar)
		{
			throw tar.getCause();
		}
	}
	
	public Method getMethodByName(String name)
	{
		return meths.get(name);
	}
	
	public String[] getMethods()
	{
		return meths.keySet().toArray(Mirror.emptyStringArray);
	}
	
	public SimplePublish1<T> getDefaultCall()
	{
		return def;
	}
}