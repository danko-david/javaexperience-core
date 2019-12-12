package eu.javaexperience;

import java.io.FileReader;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.interfaces.simple.SimpleCall;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.multithread.MultithreadingTools;
import eu.javaexperience.multithreading.pools.TimeoutJobPool;
import eu.javaexperience.parse.ParsePrimitive;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.StringTools;

public class Global
{
	private Global(){}
	
	private static final Properties prop = new Properties();
	
	public static Object putProperty(Object str,Object obj)
	{
		return prop.put(str, obj);
	}
	
	public static Object getProperty(Object str)
	{
		return prop.get(str);
	}
	
	public static Set<Entry<Object,Object>> getAllPropertySet()
	{
		return prop.entrySet();
	}
	
	/**
	 * TODO Refactor: rename and move out this class
	 * */
	public static class LazySingletons
	{
		private LazySingletons(){}
		
		
		private static TimeoutJobPool timeouts = new TimeoutJobPool();
		
		public static TimeoutJobPool timeoutJobPool()
		{
			return timeouts;
		}
		
		/**
		 * A megadott feladatot a megadott időközönként végrehajtja (ms-ben megadva).
		 * Onnantól számoljuk a késleltetést miután a megadott hívás vezérlése visszatért (így vagy úgy).
		 * Ha az ignoreException <code>true</code> akkor a feladat ismétlése kivételdobás esetén se áll le,
		 * különben csak ezzel lehet megállítani.
		 * //Dev.: a kivételt továbbengedi így a RerunnableThread#stopCallerThread() működni fog.
		 * */
		public static void repeatJob(final SimpleCall c, final int delayNextRun, final boolean ignoreException, int firstRunDelay)
		{
			LazySingletons.timeoutJobPool().putTimoutJob
			(
				firstRunDelay >= 0?firstRunDelay:delayNextRun,
				new SimpleCall()
				{
					@Override
					public void call()
					{
						try
						{
							c.call();
						}
						//Catch throwable instead of Error | RuntimeException | Exception to make this handle bullet proof
						catch(Throwable t) 
						{
							if(t == MultithreadingTools.THREAD_SHUTDOWN_POISON)
								throw t;
							
							if(ignoreException)
								LazySingletons.timeoutJobPool().putTimoutJob(delayNextRun, this);
							return;
						}
					
						LazySingletons.timeoutJobPool().putTimoutJob(delayNextRun, this);
					}
				}
			);
		}
	}
	
	protected static final String PROP_NOT_SPECIFIED_ERR = "Property not specified: ";
	
	public static boolean tryParseGlobalBoolean(String key)
	{
		Object val = Global.getProperty(key);
		try
		{
			if(null == val)
			{
				throw new IllegalArgumentException(PROP_NOT_SPECIFIED_ERR+key);
			}
			return Boolean.parseBoolean(val.toString());
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Invalid boolean property: key: "+key+", value: "+val, e);
		}
	}
	
	public static int tryParseGlobalInt(String key)
	{
		Object val = Global.getProperty(key);
		try
		{
			if(null == val)
			{
				throw new IllegalArgumentException(PROP_NOT_SPECIFIED_ERR+key);
			}
			return Integer.parseInt(val.toString());
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Invalid int property: key: "+key+", value: "+val, e);
		}
	}
	
	public static String tryParseGlobalString(String key)
	{
		Object val = Global.getProperty(key);
		if(null == val)
		{
			throw new IllegalArgumentException(PROP_NOT_SPECIFIED_ERR+key);
		}
		return val.toString();
	}
	
	public static LogLevel tryParseGlobalLoglevel(String key)
	{
		String str = tryParseGlobalString(key);
		
		LogLevel ret = null;
		
		str = str.toUpperCase();
		
		try
		{
			ret = LogLevel.valueOf(str);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Bad Loglevel specification (only "+Arrays.toString(LogLevel.values())+" are recognised)", e);
		}
		
		return ret;
	}

	public static <E extends Enum<E>> E tryParseGlobalEnum(Class<E> clss, String key)
	{
		String str = tryParseGlobalString(key);
		
		E ret = null;
		
			ret = ParsePrimitive.tryParseEnum(clss, str);
			
		if(null == ret)
		{
			throw new IllegalArgumentException("Bad enum specification for `"+key+"` (only "+Arrays.toString(clss.getEnumConstants())+" are recognised)");
		}
		
		return ret;
	}

	public static Map<String, Object> getAsMap()
	{
		Map<String, Object> ret = new SmallMap<>();
		for(Entry<Object, Object> p:prop.entrySet())
		{
			ret.put(StringTools.toString(p.getKey()), p.getValue());
		}
		
		return ret;
	}

	public static void loadFromFile(String file)
	{
		try
		{	
			try(FileReader fr = new FileReader(file))
			{
				Properties props = new Properties();
				props.load(fr);
				for(Entry<Object, Object> kv:props.entrySet())
				{
					putProperty(kv.getKey(), kv.getValue());
				}
			}
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
		}
	}

}