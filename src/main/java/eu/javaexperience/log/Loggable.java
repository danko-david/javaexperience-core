package eu.javaexperience.log;

import eu.javaexperience.asserts.AssertArgument;

public class Loggable implements LoggableUnitDescriptor
{
	protected final String modulName;
	protected final LoggingDetailLevel defaultLevel;
	
	public Loggable(String name, LoggingDetailLevel lvl)
	{
		AssertArgument.assertNotNull(this.modulName = name, "module name");
		AssertArgument.assertNotNull(this.defaultLevel = lvl, "defaultLoglevel");
	}

	public Loggable(String name)
	{
		this(name, JavaExperienceLoggingFacility.getDefaultLogLevel());
	}
	
	@Override
	public String getUnitShortName()
	{
		return modulName;
	}

	@Override
	public LoggingDetailLevel getDefaultLoggingLevel()
	{
		return defaultLevel;
	}
}
