package eu.javaexperience.log;

import java.util.concurrent.atomic.AtomicReference;

import eu.javaexperience.asserts.AssertArgument;

public abstract class AbstractLogger implements Logger
{
	protected final AtomicReference<LoggingDetailLevel> currentLogLevel =
		new AtomicReference<LoggingDetailLevel>(LogLevel.INFO);
	
	protected final String name;
	
	public AbstractLogger(LoggableUnitDescriptor descr)
	{
		this(descr.getUnitShortName(), descr.getDefaultLoggingLevel());
	}
	
	public AbstractLogger(String name, LoggingDetailLevel defaultLoglevel)
	{
		AssertArgument.assertNotNull(this.name = name, "name");
		AssertArgument.assertNotNull(defaultLoglevel, "loglevel");
		currentLogLevel.set(defaultLoglevel);
	}
	
	@Override
	public String getFacilityName()
	{
		return name;
	}
	
	@Override
	public void setLogLevel(LoggingDetailLevel level)
	{
		AssertArgument.assertNotNull(level, "loglevel");
		currentLogLevel.set(level);
	}
	
	@Override
	public LoggingDetailLevel getLogLevel()
	{
		return currentLogLevel.get();
	}

	@Override
	public boolean mayLog(LoggingDetailLevel level)
	{
		return level.getLevel() <= currentLogLevel.get().getLevel();
	}

	@Override
	public void log(LoggingDetailLevel lvl, String str)
	{
		try
		{
			doLog(LoggingTools.createLogLine(this, lvl, str));
		}
		catch(Exception e)
		{
			System.err.println("Exception while logging: ");
			e.printStackTrace();
		}
	}

	@Override
	public void logFormat(LoggingDetailLevel lvl, String formatString, Object... params)
	{
		try
		{
			doLog(LoggingTools.createLogLine(this, lvl, formatString, params));
		}
		catch(Exception e)
		{
			System.err.println("Exception while logging: ");
			e.printStackTrace();
		}
	}

	@Override
	public void logException(LoggingDetailLevel lvl, Throwable t)
	{
		try
		{
			doLog(LoggingTools.createExceptionLogLine(this, lvl, t));
		}
		catch(Exception e)
		{
			System.err.println("Exception while logging: ");
			e.printStackTrace();
		}
	}
	
	protected abstract void doLog(String toLog);

	@Override
	public void logExceptionFormat(LoggingDetailLevel level, Throwable t, String format, Object... params)
	{
		try
		{
			doLog(LoggingTools.createFormattedExceptionLogLine(this, level, t, format, params));
		}
		catch(Exception e)
		{
			System.err.println("Exception while logging: ");
			e.printStackTrace();
		}
	}
}