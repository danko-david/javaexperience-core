package eu.javaexperience.log;

public enum ExtraLogLevel implements LoggingDetailLevel
{
	MANDATORY("The log enty will written out anyway to the output.", Integer.MIN_VALUE),
	
	USER("The log enty will written out anyway to the output.", Integer.MIN_VALUE)
	
	;
	

	private ExtraLogLevel(String description, int level)
	{
		this.description = description;
		this.level = level;
	}
	
	protected final int level;
	protected final String description;
	
	@Override
	public String getLoggingLevelDescription()
	{
		return description;
	}

	@Override
	public int getLevel()
	{
		return level;
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
