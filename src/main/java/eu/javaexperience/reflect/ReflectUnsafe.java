package eu.javaexperience.reflect;

import java.lang.reflect.Method;

public class ReflectUnsafe implements ReflectInterface
{
	private static final Object instance = Mirror.getObjectFieldByClassOrNull("sun.misc.Unsafe", null, "theUnsafe");//sun.misc.Unsafe.getUnsafe();
	
	private static final Method allocateInstance = Mirror.getClassMethodOrNull("sun.misc.Unsafe", "allocateInstance", Class.class);
	
	private static final Method tryMonitorEnter = Mirror.getClassMethodOrNull("sun.misc.Unsafe", "tryMonitorEnter", Object.class);
	
	private static final Method monitorExit = Mirror.getClassMethodOrNull("sun.misc.Unsafe", "monitorExit", Object.class);
	
	private static final Method monitorEnter = Mirror.getClassMethodOrNull("sun.misc.Unsafe", "monitorEnter", Object.class);
	
	
	@Override
	public <T> T allocObject(Class<T> cls) throws InstantiationException
	{
		try
		{
			return (T) allocateInstance.invoke(instance, cls);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean tryMonitorEnter(Object o)
	{
		try
		{
			return (boolean) tryMonitorEnter.invoke(instance, o);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void monitorExit(Object o)
	{
		try
		{
			monitorExit.invoke(instance, o);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void monitorEnter(Object obj)
	{
		try
		{
			monitorEnter.invoke(instance, obj);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}