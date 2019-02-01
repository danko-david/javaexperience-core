package eu.javaexperience.log;

/**
 * Simple but straightforward interface of a log handler component.
 * It's guess that every unit in an application has it's own LoggingFacility.
 * 
 * */
public interface Logger
{
	/**
	 * A good descriptive and short name about the facility work with.
	 * For example: "MailSend", "HttpDispatch",
	 * */
	public String getFacilityName();
	
	public LoggingDetailLevel getLogLevel();
	
	public void setLogLevel(LoggingDetailLevel level);
	
	public boolean mayLog(LoggingDetailLevel level);
	
	public void log(LoggingDetailLevel level, String str);
	
	public void logFormat(LoggingDetailLevel level, String formatString, Object... params);
	
	public void logException(LoggingDetailLevel level, Throwable t);
	
	public void logExceptionFormat(LoggingDetailLevel level, Throwable t, String format, Object... params);
	
}