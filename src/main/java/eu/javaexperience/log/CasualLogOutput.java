package eu.javaexperience.log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.semantic.references.MayNull;
import eu.javaexperience.time.TimeCalc;

public class CasualLogOutput extends RotaLogOutput
{
	protected final String logPrefix;
	protected final SimpleGet<Logger> newDayAllLoglevelWriter;
	
	public CasualLogOutput(String logPrefix, @MayNull SimpleGet<Logger> newDayAllLoglevelWriter)
	{
		this.logPrefix = logPrefix;
		this.newDayAllLoglevelWriter = newDayAllLoglevelWriter;
	}
	
	@Override
	protected File getNextLogfile()
	{
		String s = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return new File(logPrefix+s+".log");
	}
	
	public long getNextCutDate()
	{
		return TimeCalc.addToDate
		(
			TimeCalc.setToDate
			(
				new Date(),
				-1,
				-1,
				-1,
				0,
				0,
				0,
				0
			),
		0,
		0,
		1,
		0,
		0,
		0,
		0).getTime()+1000;
	}
	
	protected long nextCutTime = getNextCutDate();
	
	@Override
	protected boolean needCut()
	{
		if(System.currentTimeMillis() >= nextCutTime)
		{
			nextCutTime = getNextCutDate();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	protected void afterOpenNewUnit()
	{
		if(null != newDayAllLoglevelWriter)
		{
			Logger LOG = newDayAllLoglevelWriter.get();
			if(null != LOG)
			{
				//kiírunk minden naplózási szintet
				ArrayList<Logger> loggers = new ArrayList<>();
				JavaExperienceLoggingFacility.listIssuedLoggers(loggers);
				LoggingTools.tryLogSimple(LOG, ExtraLogLevel.MANDATORY, "New day, new log file");
				for(Logger l:loggers)
				{
					LoggingTools.tryLogFormat(LOG, ExtraLogLevel.MANDATORY, "Loglevel of %s is %s", l, l.getLogLevel().getLabel());
				}
			}
		}
	}
}
