package eu.javaexperience.retry;

import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingTools;

public class RetryTools
{
	protected static final Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("RetryTools"));
	
	protected static final int[] RECONNECT_TIMES = {100, 200, 500, 1_000, 2_000, 5_000, 10_000};
	
	public static int[] getDefaultReconnectTimeMillisecs()
	{
		return RECONNECT_TIMES;
	}
	
	public static <T> SimpleGet<T> waitReconnect
	(
		SimpleGet<T> connect,
		String entity,
		Logger log,
		LogLevel lvl,
		int... reconnectWaitTimes)
	{
		return ()->
		{
			for(int i=0;i<reconnectWaitTimes.length;++i)
			{
				try
				{
					return connect.get();
				}
				catch(Exception e)
				{
					if(reconnectWaitTimes[i] < 0)
					{
						throw e;
					}
					
					LoggingTools.tryLogFormatException(log, lvl, e, "Can't open `%s`, waiting `%s` millisec before trying reconnect again. ", entity, reconnectWaitTimes[i]);
					try
					{
						Thread.sleep(reconnectWaitTimes[i]);
					}
					catch (InterruptedException e1)
					{
						return null;
					}
					
					if(i >= reconnectWaitTimes.length-1)
					{
						i = reconnectWaitTimes.length-2;
					}
				}
			}
			return null;
		};
	}
	
	public static <T> SimpleGet<T> waitReconnect(SimpleGet<T> connect, String entity, int... reconnectWaitTimes)
	{
		return waitReconnect(connect, entity, LOG, LogLevel.WARNING, reconnectWaitTimes);
	}
	
	public static <T> SimpleGet<T> waitReconnect(SimpleGet<T> connect, String entity)
	{
		return waitReconnect(connect, entity, LOG, LogLevel.WARNING, RECONNECT_TIMES);
	}
}
