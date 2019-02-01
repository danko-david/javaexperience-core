package eu.javaexperience.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Map.Entry;

import eu.javaexperience.Global;
import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.collection.enumerations.EnumTools;
import eu.javaexperience.environment.Mode;
import eu.javaexperience.io.file.FileTools;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingTools;
import eu.javaexperience.parse.ParsePrimitive;
import eu.javaexperience.reflect.CastTo;

public class ConfigTools
{
	public static final Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("ConfigTools"));
	
	/**
	 * tries to load the first one found config file from the given list.
	 * If file found it loads everything into the {@link Global} facility,
	 * all options now available there.
	 * 
	 *  Otherwise false returned.
	 *  
	 *  Note: this method logs which file loaded (on {@link LogLevel#INFO})
	 *  with it's canonical path. On {@link LogLevel#DEBUG} it's log every
	 *  attempts (file not exists or Exception raised).
	 *  
	 *  It uses the {@link JavaExperienceLoggingFacility#DEFAULT_LOGGER} for
	 *  logging because usually there's no another logging module loaded at this
	 *  stage of a program. (eg. Loglevels specified in config, used with
	 *  {@link JavaExperienceLoggingFacility#setFutureModuleDefaultLoglevel(String, eu.javaexperience.log.LoggingDetailLevel)})
	 *  
	 *  And there's an automatic function for this:
	 *  {@link JavaExperienceLoggingFacility#loadDefaultFacilityLogLevelsFromPropertyWithPrefix(Properties, String)}
	 *  whith {@link Global#getAllPropertySet()}
	 *  
	 * */
	
	public static Properties loadFirstConfig(String... files)
	{
		if(null == files || 0 == files.length)
		{
			LoggingTools.tryLogSimple
			(
				LOG,
				LogLevel.DEBUG,
				"No config files given, spoofing default files for config loader."
			);
				
			return loadFirstConfig
			(
				new String[]
				{
					"settings.properties",
					"setting.properties",
					"config.properties",
					
					"srv/settings.properties",
					"srv/setting.properties",
					"srv/config.properties",
					
					"bin/settings.properties",
					"bin/setting.properties",
					"bin/config.properties",
				}
			);
		}
		else
		{
			if(LOG.mayLog(LogLevel.DEBUG))
			{
				try
				{
					LoggingTools.tryLogFormat
					(
						LOG,
						LogLevel.DEBUG,
						"Config load stage, current working directory is %s",
						new File(".").getCanonicalFile()
					);
				}
				catch(Exception e)
				{
					LoggingTools.tryLogSimple
					(
						LOG,
						LogLevel.DEBUG,
						"Unable to log Current Working Directory",
						e
					);
				}
			}
			
			for(String file:files)
			{
				File f = new File(file);
				if(!f.exists())
				{
					LoggingTools.tryLogFormat
					(
						LOG,
						LogLevel.DEBUG,
						"Result of config file loading `%s`: file doesn't exists (canonical file is: %s)",
						file,
						FileTools.tryGetCanonicalFile(f)
					);
					
					continue;
				}
				
				try(InputStream is = new FileInputStream(f))
				{
					Properties p = new Properties();
					p.load(new BufferedReader(new InputStreamReader(is)));
					
					LoggingTools.tryLogFormat
					(
						LOG,
						LogLevel.INFO,
						"Config file `%s` successfully loaded  (canonical file is: %s).",
						file,
						FileTools.tryGetCanonicalFile(f)
					);
					
					return p;
				}
				catch(Exception e)
				{
					LoggingTools.tryLogFormat
					(
						LOG,
						LogLevel.DEBUG,
						"Loading config file `%s` (canonical file is: %s) refused by Exception: ",
						file,
						FileTools.tryGetCanonicalFile(f)
					);
					continue;
				}
			}
		}
		
		LoggingTools.tryLogSimple
		(
			LOG,
			LogLevel.DEBUG,
			"No config files are loaded in this stage."
		);
		return null;
	}
	
	public static boolean loadConfig(String... files)
	{
		Properties p = loadFirstConfig(files);
		if(null != p)
		{
			for(Entry<Object, Object> kv:p.entrySet())
			{
				Global.putProperty(kv.getKey(), kv.getValue());
			}
			return true;
		}
		
		return false;
	}
	
	protected static void notNullOrThrow(Object subject, String key)
	{
		if(null == subject)
		{
			throw new RuntimeException("No configuration value specified under key: "+key);
		}
	}
	
	public static <E extends Enum<E>> E getGlobalValuableEnum(Class<? extends E> cls, String key)
	{
		Object prop = Global.getProperty(key);
		notNullOrThrow(prop, key);
		E ret = EnumTools.recogniseSymbol(cls, prop);
		if(null == ret)
		{
			throw new RuntimeException("Can't recognise enum: `"+prop+"` under key: `"+key+"`. Possible values: "+ArrayTools.toString(cls.getEnumConstants()));
		}
		return ret;
	}

	public static String getGlobalValuableString(String key)
	{
		Object prop = Global.getProperty(key);
		notNullOrThrow(prop, key);
		return String.valueOf(prop.toString());
	}


	public static int getGlobalValuableInt(String key)
	{
		Object prop = Global.getProperty(key);
		notNullOrThrow(prop, key);
		Integer ret = ParsePrimitive.tryParseInt(prop.toString());
		if(null == ret)
		{
			throw new RuntimeException("Can't recognise integer number: `"+prop+"` under key: `"+key+"`.");
		}
		return ret;
	}
	
	public static double getGlobalValuableDouble(String key)
	{
		Object prop = Global.getProperty(key);
		notNullOrThrow(prop, key);
		Double ret = ParsePrimitive.tryParseDouble(prop.toString());
		if(null == ret)
		{
			throw new RuntimeException("Can't recognise double float number: `"+prop+"` under key: `"+key+"`.");
		}
		return ret;
	}


	public static boolean getGlobalValuableBoolean(String key)
	{
		Object prop = Global.getProperty(key);
		notNullOrThrow(prop, key);
		Boolean ret = (Boolean) CastTo.Boolean.cast(prop);
		if(null == ret)
		{
			throw new RuntimeException("Can't recognise boolean: `"+prop+"` under key: `"+key+"`.");
		}
		return ret;
	}
}
