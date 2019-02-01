package eu.javaexperience.log;

import java.io.File;
import java.util.Date;

import eu.javaexperience.text.Format;
import eu.javaexperience.time.TimeCalc;

public class DayliLogrotaOutput extends RotaLogOutput
{
	protected String prefix;
	public DayliLogrotaOutput(String prefix)
	{
		this.prefix = prefix;
	}
	
	@Override
	protected File getNextLogfile()
	{
		return new File(prefix+Format.formatSqlDate(new Date())+".log");
	}
	
	protected long nextCutTime = getNextCutTime();
	
	protected static long getNextCutTime()
	{
		return TimeCalc.setToDate
		(
			new Date(), 0, 0, 0, 24, 0, 0, 0
		).getTime();
	}
	
	@Override
	protected synchronized boolean needCut()
	{
		if(System.currentTimeMillis() >= nextCutTime)
		{
			nextCutTime = getNextCutTime();
			return true;
		}
		
		return false;
	}
}
