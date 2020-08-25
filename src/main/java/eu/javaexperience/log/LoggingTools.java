package eu.javaexperience.log;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.io.IOTools;
import eu.javaexperience.io.LocklessPrintWriter;
import eu.javaexperience.parse.ParsePrimitive;
import eu.javaexperience.resource.ReferenceCounted;
import eu.javaexperience.resource.pool.SimplifiedResourcePool;
import eu.javaexperience.text.DontCareFieldPosition;

/**
 * LogFormat:
 * 	Current Date				Module name			Loglevel	INFO
 * 
 * 	[2017-01-29 11:35:12.325]	{Lightning Accept}	(INFO)		GET http://localhost.hu/home?page=1
 * 	[2017-01-29 11:35:12.405]	{JDBC}				(DEBUG)		SELECT * FROM articles WHERE ID=12
 * */
public class LoggingTools
{
	public static final LogOutput STDOUT_LOG_OUTPUT = wrapPrintStream(System.out);
	
	public static final LogOutput STDERR_LOG_OUTPUT = wrapPrintStream(System.err);
	
	public static LogOutput wrapPrintWriter(PrintWriter pw)
	{
		return wrapPrintWriter(pw);
	}
	
	public static LogOutput wrapPrintStream(PrintStream ps)
	{
		return new LogOutput()
		{
			@Override
			public ReferenceCounted<PrintWriter> getLogOutput() throws IOException
			{
				return new ReferenceCounted<PrintWriter>(new PrintWriter(ps, true), 2)
				{
					@Override
					protected void onFree(){/*nop*/}
				};
			}
		};	
	}
	
	protected static final class LogFormattingTools
	{
		StringBuffer sb = new StringBuffer();
		LocklessPrintWriter pw = new LocklessPrintWriter(IOTools.nullOutputStream, false)
		{
			@Override
			public void write(char[] str, int offset, int len)
			{
				sb.append(str, offset, len);
			}
			
			@Override
			public void flush(){/*noop*/}
			
			@Override
			public void close() {/*noop*/}
		};
		
		
		SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS'U'");
		{
			currentDate.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
		
		public void reset()
		{
			sb.delete(0, sb.length());
		}
		
		public String getString()
		{
			return sb.toString();
		}
		
		protected void putCommons
		(
			Logger lf,
			LoggingDetailLevel lvl
		)
		{
			sb.append("[");
			currentDate.format(new Date(), sb, DontCareFieldPosition.INSTANCE);
			sb.append("]\t<");
			sb.append(THIS_PROCESS_PID);
			sb.append("/");
			sb.append(Thread.currentThread().getId());
			sb.append(">\t{");
			sb.append(lf.getFacilityName());
			sb.append("}\t(");
			sb.append(lvl.getLabel());
			sb.append(")\t");
		}
	}
	
	protected static final long THIS_PROCESS_PID = getProcessPid();
	
	protected static final SimplifiedResourcePool<LogFormattingTools> FORMAT_TOOLS = new SimplifiedResourcePool<LogFormattingTools>(new SimpleGet<LogFormattingTools>()
	{
		@Override
		public LogFormattingTools get()
		{
			return new LogFormattingTools();
		}
	});
	
	public static String createLogLine
	(
		Logger lf,
		LoggingDetailLevel lvl,
		String str
	)
	{
		LogFormattingTools tool = FORMAT_TOOLS.acquireResource();
		try
		{
			tool.reset();
			tool.putCommons(lf, lvl);
			tool.sb.append(str);
			return tool.getString();
		}
		finally
		{
			FORMAT_TOOLS.releaseResource(tool);
		}
	}

	private static long getProcessPid()
	{
		String processName = null;
		try
		{
			RuntimeMXBean bean = java.lang.management.ManagementFactory.getRuntimeMXBean();
			if(null != bean)
			{
				processName = bean.getName();
			}
		}
		catch(Throwable e){}
		
		if(null == processName)
		{
			processName = "0@";
		}
		
		return ParsePrimitive.tryParseLong(processName.split("@")[0], 0);
	}

	public static String createLogLine
	(
		Logger lf,
		LoggingDetailLevel lvl,
		String formatString,
		Object[] params
	)
	{
		LogFormattingTools tool = FORMAT_TOOLS.acquireResource();
		try
		{
			tool.reset();
			tool.putCommons(lf, lvl);
			tool.pw.format(formatString, params);
			return tool.getString();
		}
		finally
		{
			FORMAT_TOOLS.releaseResource(tool);
		}
	}

	public static String createExceptionLogLine
	(
		Logger lf,
		LoggingDetailLevel lvl,
		Throwable t
	)
	{
		LogFormattingTools tool = FORMAT_TOOLS.acquireResource();
		try
		{
			tool.reset();
			tool.putCommons(lf, lvl);
			if(null != t)
			{
				t.printStackTrace(tool.pw);
			}
			else
			{
				tool.sb.append("Exception: null");
			}
			return tool.getString();
		}
		finally
		{
			FORMAT_TOOLS.releaseResource(tool);
		}
	}
	
	public static String createFormattedExceptionLogLine
	(
		Logger lf,
		LoggingDetailLevel lvl,
		Throwable t,
		String formatString,
		Object[] params
	)
	{
		LogFormattingTools tool = FORMAT_TOOLS.acquireResource();
		try
		{
			tool.reset();
			tool.putCommons(lf, lvl);
			tool.pw.format(formatString, params);
			if(null != t)
			{
				t.printStackTrace(tool.pw);
			}
			else
			{
				tool.sb.append("Exception: null");
			}
			return tool.getString();
		}
		finally
		{
			FORMAT_TOOLS.releaseResource(tool);
		}
	}


	public static void tryLogSimple(Logger log, LoggingDetailLevel level, Throwable e)
	{
		if(log.mayLog(level))
		{
			log.logException(level, e);
		}
	}
	
	public static void tryLogSimple(Logger log, LoggingDetailLevel level, String msg)
	{
		if(log.mayLog(level))
		{
			log.log(level, msg);
		}
	}
	
	public static void tryLogSimple(Logger log, LoggingDetailLevel level, String msg, Throwable exception)
	{
		if(log.mayLog(level))
		{
			log.logException(level, exception);
		}
	}
	
	
	public static void tryLogFormat(Logger log, LoggingDetailLevel level, String format, Object params)
	{
		if(log.mayLog(level))
		{
			log.logFormat(level, format, params);
		}
	}
	
	public static void tryLogFormat(Logger log, LoggingDetailLevel level, String format, Object p1, Object p2)
	{
		if(log.mayLog(level))
		{
			log.logFormat(level, format, p1, p2);
		}
	}
	
	public static void tryLogFormat(Logger log, LoggingDetailLevel level, String format, Object p1, Object p2, Object p3)
	{
		if(log.mayLog(level))
		{
			log.logFormat(level, format, p1, p2, p3);
		}
	}
	
	public static void tryLogFormat(Logger log, LoggingDetailLevel level, String format, Object... params)
	{
		if(log.mayLog(level))
		{
			log.logFormat(level, format, params);
		}
	}
	
	public static void tryLogFormatException(Logger log, LoggingDetailLevel level, Throwable t, String format, Object... params)
	{
		if(log.mayLog(level))
		{
			log.logExceptionFormat(level, t, format, params);
		}
	}
	
	public static void tryLogFormatException(Logger log, LoggingDetailLevel level, Throwable t, String format)
	{
		if(log.mayLog(level))
		{
			log.logExceptionFormat(level, t, format);
		}
	}
	
	public static void tryLogFormatException(Logger log, LoggingDetailLevel level, Throwable t, String format, Object param)
	{
		if(log.mayLog(level))
		{
			log.logExceptionFormat(level, t, format, param);
		}
	}
	
	public static void tryLogFormatException(Logger log, LoggingDetailLevel level, Throwable t, String format, Object param1, Object param2)
	{
		if(log.mayLog(level))
		{
			log.logExceptionFormat(level, t, format, param1, param2);
		}
	}

}