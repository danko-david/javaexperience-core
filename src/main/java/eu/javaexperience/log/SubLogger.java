package eu.javaexperience.log;

public class SubLogger
{
	protected Logger log;
	protected String module;
	public SubLogger(Logger facility, String name)
	{
		this.log = facility;
		this.module = name;
	}
	
	public void tryLogSmallModule(LoggingDetailLevel lvl, String msg)
	{
		LoggingTools.tryLogFormat(log, lvl, "Module: %s, Msg: %s", module, msg);
	}
	
	public void tryLogSmallModule(LoggingDetailLevel lvl, Throwable exception)
	{
		LoggingTools.tryLogFormatException(log, lvl, exception, "Module: %s, Exception: ", module);
	}
	
	public void tryLogSmallModule(LoggingDetailLevel lvl, Throwable exception, String msg)
	{
		LoggingTools.tryLogFormatException(log, lvl, exception, "Module: %s, Msg: %s Exception: ", module, msg);
	}
}
