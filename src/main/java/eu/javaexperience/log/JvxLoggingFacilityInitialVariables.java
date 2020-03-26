package eu.javaexperience.log;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.collection.enumerations.EnumTools;
import eu.javaexperience.interfaces.simple.SimpleGet;

public class JvxLoggingFacilityInitialVariables
{
	public static SimpleGet<LoggingDetailLevel> GET_DEFAULT_LOG_LEVEL = ()->
	{
		String ll = System.getenv("JVX_DEFAULT_LOG_LEVEL");
		if(null != ll)
		{
			LogLevel ret = EnumTools.recogniseSymbol(LogLevel.class, ll);
			if(null == ret)
			{
				System.out.println("Env variable `JVX_DEFAULT_LOG_LEVEL` specified as `"+ll+"` which is an unrecognisable loglevel. Available loglevels are: "+ArrayTools.toString(LogLevel.values()));
				return null;
			}
			else
			{
				return ret;
			}
		}
		return null;
	};
}
