package eu.javaexperience.log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.collection.PublisherCollection;
import eu.javaexperience.collection.enumerations.EnumTools;
import eu.javaexperience.io.IOTools;
import eu.javaexperience.io.LocklessPrintWriter;
import eu.javaexperience.parse.ParsePrimitive;
import eu.javaexperience.resource.ReferenceCounted;
import eu.javaexperience.text.Format;
import eu.javaexperience.text.StringTools;
import eu.javaexperience.time.TimeCalc;

public class JavaExperienceLoggingFacility
{
	protected static Vector<LogOutput> OUTPUTS = new Vector<>();
	
	public static final LogOutput[] emptyLogOutputArrays = new LogOutput[0];
	
	protected static LoggingDetailLevel lookupDefaultLoglevel()
	{
		String ll = System.getenv("JVX_DEFAULT_LOG_LEVEL");
		if(null != ll)
		{
			LogLevel ret = EnumTools.recogniseSymbol(LogLevel.class, ll);
			if(null == ret)
			{
				System.out.println("Env variable `JVX_DEFAULT_LOG_LEVEL` specified as `"+ll+"` which is an unrecognisable loglevel. Available loglevels are: "+ArrayTools.toString(LogLevel.values()));
			}
			else
			{
				return ret;
			}
		}
		
		return LogLevel.MEASURE;
	}
	
	protected static final LoggingDetailLevel DEFAULT_LEVEL = lookupDefaultLoglevel();
	
	public static LoggingDetailLevel getDefaultLogLevel()
	{
		return DEFAULT_LEVEL;
	}
	
	protected static LoggerProvider JEX = new LoggerProvider(new LogOutput()
	{
		@Override
		public ReferenceCounted<PrintWriter> getLogOutput() throws IOException
		{
			PrintWriter lpw = new LocklessPrintWriter(IOTools.nullOutputStream, false)
			{
				@Override
				public void write(char[] csq, int start, int end)
				{
					LogOutput[] los = OUTPUTS.toArray(emptyLogOutputArrays);
					for(LogOutput lo:los)
					{
						try(ReferenceCounted<PrintWriter> rc_pw = lo.getLogOutput())
						{
							PrintWriter pw = rc_pw.getSubject();
							pw.write(csq, start, end);
							pw.flush();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				
				@Override public void flush() {/*noop*/}
				@Override public void close() {/*noop*/}
			};
			
			return new ReferenceCounted<PrintWriter>(lpw, 2)
			{
				@Override
				protected void onFree(){/*noop*/}
			};
		}
	});
	
	public static Logger getLogger(LoggableUnitDescriptor descr)
	{
		{
			Logger l = getLoggingFacilityByName(descr.getUnitShortName());
			if(null != l)
			{
				return l;
			}
		}
		
		{
			Logger l =  JEX.createLoggerFor(descr);
			LoggingDetailLevel lvl = FUTURE_MODULE_LEVEL.get(descr.getUnitShortName());
			if(null != lvl)
			{
				l.setLogLevel(lvl);
			}
			else
			{
				lvl = FUTURE_DEFAUL_LEVEL;
				if(null != lvl)
				{
					l.setLogLevel(lvl);
				}
			}
			return l;
		}
	}
	
	
	static final Logger LOG = JEX.createLoggerFor(new Loggable("JavaExperienceDefaultLogginFacility", getDefaultLogLevel()));
	
	public static final Logger DEFAULT_LOGGER = new Logger()
	{
		protected void refuse()
		{
			throw new RuntimeException("Cannot modify JavaExperienceDefaultLogginFacility");
		}
		
		@Override
		public void setLogLevel(LoggingDetailLevel level)
		{
			refuse();
		}
		
		@Override
		public boolean mayLog(LoggingDetailLevel level)
		{
			return LOG.mayLog(level);
		}
		
		@Override
		public void logFormat(LoggingDetailLevel level, String formatString, Object... params)
		{
			LOG.logFormat(level, formatString, params);
		}
		
		@Override
		public void logExceptionFormat(LoggingDetailLevel level, Throwable t, String format, Object... params)
		{
			LOG.logExceptionFormat(level, t, format, params);
		}
		
		@Override
		public void logException(LoggingDetailLevel level, Throwable t)
		{
			LOG.logException(level, t);
		}
		
		@Override
		public void log(LoggingDetailLevel level, String str)
		{
			LOG.log(level, str);
		}
		
		@Override
		public LoggingDetailLevel getLogLevel()
		{
			return LOG.getLogLevel();
		}
		
		@Override
		public String getFacilityName()
		{
			return LOG.getFacilityName();
		}
	};
	
	public static void addLogOutput(LogOutput out)
	{
		OUTPUTS.add(out);
	}
	
	protected static boolean STD_OUT_LOG_ADDED = false;
	
	public static synchronized void addStdOut()
	{
		if(!STD_OUT_LOG_ADDED)
		{
			STD_OUT_LOG_ADDED = true;
			addLogOutput(LoggingTools.STDOUT_LOG_OUTPUT);
			LoggingTools.tryLogSimple(LOG, getDefaultLogLevel(), "JavaExperienceLoggingFacility.addStdOut() called");
		}
	}
	
	private static boolean THREAD_LOCAL_OUTPUT_ADDED = false; 
	public static synchronized void addThreadLocalHookableLoggerOutput()
	{
		if(!THREAD_LOCAL_OUTPUT_ADDED)
		{
			addLogOutput(ThreadLocalHookableLogFacility.LOG_OUTPUT);
			LoggingTools.tryLogSimple(LOG, getDefaultLogLevel(), "JavaExperienceLoggingFacility.addThreadLocalHookableLoggerOutput() called");
			THREAD_LOCAL_OUTPUT_ADDED = true;
		}
	}
	
	public static void listIssuedLoggers(Collection<Logger> facilities)
	{
		JEX.fillActiveLoggers(facilities);
	}
	
	protected static Map<String, LoggingDetailLevel> FUTURE_MODULE_LEVEL = new ConcurrentHashMap<>();
	
	protected static LoggingDetailLevel FUTURE_DEFAUL_LEVEL = null;
	
	/**
	 * Modifies the loglevel of the requested module even if is loaded already.
	 * */
	public static void setFutureModuleDefaultLoglevel(String module, LoggingDetailLevel lvl)
	{
		if(null != module && null != lvl)
		{
			FUTURE_MODULE_LEVEL.put(module, lvl);
			Logger l = getLoggingFacilityByName(module);
			if(null != l)
			{
				l.setLogLevel(lvl);
			}
		}
	}
	
	public static void setFutureDefaultLoglevel(LoggingDetailLevel lvl)
	{
		FUTURE_DEFAUL_LEVEL = lvl;
	}
	
	public static Logger getLoggingFacilityByName(String name)
	{
		ArrayList<Logger> lfs = new ArrayList<>();
		JEX.fillActiveLoggers(lfs);
		for(Logger lf:lfs)
		{
			if(name.equals(lf.getFacilityName()))
			{
				return lf;
			}
		}
		
		return null;
	}

	public static void loadDefaultFacilityLogLevelsFromPropertyWithPrefix(Properties prop, String prefix)
	{
		loadDefaultFacilityLogLevelsFromPropertyWithPrefix(prop.entrySet(), prefix);
	}
	
	public static void loadDefaultFacilityLogLevelsFromPropertyWithPrefix(Set<Entry<Object, Object>> allPropertySet, String prefix)
	{
		String pre = prefix+".";
		for(Entry<Object, Object> kv:allPropertySet)
		{
			if(null != kv.getKey() && null != kv.getValue())
			{
				String k = kv.getKey().toString();
				if(k.startsWith(pre))
				{
					k = StringTools.getSubstringAfterFirstString(k, pre);
					if(!k.contains("."))
					{
						LogLevel lvl = ParsePrimitive.tryParseEnum(LogLevel.class, kv.getValue().toString());
						if(null != lvl)
						{
							JavaExperienceLoggingFacility.setFutureModuleDefaultLoglevel(k, lvl);
						}
					}
				}
			}
		}
	}
	
	static
	{
		try
		{
			Runtime.getRuntime().addShutdownHook
			(
				new Thread()
				{
					public void run()
					{
						long jvmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
						long now = System.currentTimeMillis();
						LoggingTools.tryLogFormat
						(
							LOG,
							LogLevel.INFO,
							"JavaExperienceLoggingFacility JVM start time: %s, JVM shutdownHook time: %s, full time: %s",
							Format.UTC_SQL_TIMESTAMP_MS.format(new Date(jvmStartTime)),
							Format.UTC_SQL_TIMESTAMP_MS.format(new Date(now)),
							TimeCalc.durationToHourMinSec(now-jvmStartTime)+" h:m:s"
						);
					};
				}
			);
		}
		catch(Throwable t)
		{
			t.printStackTrace();
		}
	}

	public static void setAllFacilityLoglevel(LogLevel lvl)
	{
		JEX.fillActiveLoggers(new PublisherCollection<Logger>()
		{
			@Override
			public boolean add(Logger lf)
			{
				lf.setLogLevel(lvl);
				return false;
			}
		});
	}

	public static void startLoggingIntoDirectory(File directory, String prefix)
	{
		if(!directory.exists())
		{
			directory.mkdirs();
		}
		
		addLogOutput(new DayliLogrotaOutput(directory+"/"+prefix));
	}
}