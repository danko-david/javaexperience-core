package eu.javaexperience.log;

public enum LogLevel implements LoggingDetailLevel
{
	FATAL(""),
	ERROR(""),
	WARNING(""),
	NOTICE(""),
	INFO("Default loglevel for most usage."),
	MEASURE("For performance measurements: If the production code contains operation time measurements this is the recommended level instead of INFO"),
	//TODO DETAILED
	DEBUG("Provides mode details about operation. Useful to examine information about an unexcepted error for testing and failure recreating porpuse"),
	TRACE("Very verbose output, provides all information can be logged.");
	
	private LogLevel(String description)
	{
		this.description = description;
	}
	
	protected final String description;
	
	@Override
	public String getLoggingLevelDescription()
	{
		return description;
	}

	@Override
	public int getLevel()
	{
		return ordinal()*1000;
	}

	@Override
	public String getLabel()
	{
		return name();
	}

	public boolean mayPropagateAtThisLevel(LoggingDetailLevel weight)
	{
		if(null == weight)
		{
			return true;
		}
		
		return this.getLevel() >= weight.getLevel();
	}
}
