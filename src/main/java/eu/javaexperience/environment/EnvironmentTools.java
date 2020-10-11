package eu.javaexperience.environment;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

import eu.javaexperience.text.StringTools;

public class EnvironmentTools
{
	public static String getHostname()
	{
		try
		{
			return InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException e)
		{
			return "localhost";
		}
	}
	
	//TODO that's a platform specific stuff test on other operation system eg.: on windows
	public static int getCurrentProcessPid()
	{
		//this assumes that we are under linux
		try
		{
			String pp = new File("/proc/self").getCanonicalFile().toString();
			pp = StringTools.getSubstringAfterLastString(pp, "/", null);
			if(null != pp)
			{
				return Integer.valueOf(pp);
			}
		}
		catch(Exception e)
		{}
		
		try
		{
			String jvmName = ManagementFactory.getRuntimeMXBean().getName();
			String spid = StringTools.getSubstringBeforeFirstString(jvmName, "@", null);
			
			if(null != spid)
			{
				Integer.valueOf(spid);
			}
		}
		catch(Exception e){}
		
		throw new RuntimeException("Can't determine current process pid");
	}
	
	public static boolean isJvmInDebugMode()
	{
		return ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}

	public static String getOperationSystemType()
	{
		return System.getProperty("os.name");
	}
}
