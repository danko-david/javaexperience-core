package eu.javaexperience.reflect;

public class UnsafeMirror
{
	protected static ReflectInterface unsafe = new ReflectUnsafe();
	
	public static boolean isObjectMonitorAcquired(Object o)
	{
		boolean ret = unsafe.tryMonitorEnter(o);
		if(ret)
		{
			unsafe.monitorExit(o);
			return true;
		}
		
		return false;
	}
	
	public static boolean tryMonitorEnter(Object o)
	{
		return unsafe.tryMonitorEnter(o);
	}

	public static void monitorEnter(Object o)
	{
		unsafe.monitorEnter(o);
	}

	public static void monitorExit(Object o)
	{
		unsafe.monitorExit(o);
	}
	
	public static <T> T allocObject(Class<T> cls) throws InstantiationException
	{
		return unsafe.allocObject(cls);
	}
}
