package eu.javaexperience.log;

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import eu.javaexperience.collection.map.ConcurrentMapTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.resource.ReferenceCounted;
import static eu.javaexperience.log.JavaExperienceLoggingFacility.LOG;

public class LoggerProvider implements LoggerFactory
{
	public static LoggerProvider logIntoDirectory(File dir, String logfilePerfix)
	{
		return new LoggerProvider
		(
			new DayliLogrotaOutput(dir+"/"+logfilePerfix)
		);
	}
	
	protected final LogOutput out;
	
	public LoggerProvider(LogOutput lo)
	{
		this.out = lo;
	}
	
	protected final ConcurrentHashMap<LoggableUnitDescriptor, Logger> LFS =
		new ConcurrentHashMap<>();
	
	@Override
	public Logger createLoggerFor(LoggableUnitDescriptor unit)
	{
		return ConcurrentMapTools.getOrCreate
		(
			LFS,
			unit,
			new GetBy1<Logger, LoggableUnitDescriptor>()
			{
				@Override
				public Logger getBy(LoggableUnitDescriptor a)
				{
					return new AbstractLogger(a)
					{
						@Override
						protected void doLog(String toLog)
						{
							try
							{
								try(ReferenceCounted<PrintWriter> rc_pw = out.getLogOutput())
								{
									rc_pw.getSubject().println(toLog);
								}
							}
							catch(Exception e)
							{
								LoggingTools.tryLogSimple(LOG, LogLevel.WARNING, e);
							}
						}
					};
				}
			}
		);
	}

	@Override
	public void fillActiveLoggers(Collection<Logger> facilities)
	{
		for(Logger lf:LFS.values())
		{
			facilities.add(lf);
		}
	}
}
