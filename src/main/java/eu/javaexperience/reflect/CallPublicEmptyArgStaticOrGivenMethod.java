package eu.javaexperience.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.interfaces.simple.SimpleCall;

public class CallPublicEmptyArgStaticOrGivenMethod
{
	private final Map<String,Method> meths = new SmallMap<>();
	private final SimpleCall def;
	
	public CallPublicEmptyArgStaticOrGivenMethod(Class<?> cls,SimpleCall orDefault)
	{
		def = orDefault;
		for(Method m:cls.getMethods())
			if(Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()) && m.getParameterTypes().length == 0)
				meths.put(m.getName(), m);
	}

	public void invoke(String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Method m = meths.get(name);
		if(m != null)
			m.invoke(null);
		else if(def != null)
			def.call();
	}

	public SimpleCall getDefaultCall()
	{
		return def;
	}
}