package eu.javaexperience.log;

import java.util.Collection;

public interface LoggerFactory
{
	public Logger createLoggerFor(LoggableUnitDescriptor unit);
	
	public void fillActiveLoggers(Collection<Logger> facilities);
}