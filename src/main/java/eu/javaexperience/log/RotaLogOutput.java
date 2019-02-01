package eu.javaexperience.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import eu.javaexperience.io.IOTools;
import eu.javaexperience.resource.ReferenceCounted;

public abstract class RotaLogOutput implements LogOutput
{
	protected abstract File getNextLogfile();
	
	protected abstract boolean needCut();
	
	protected void afterOpenNewUnit(){};
	
	protected ReferenceCounted<PrintWriter> currentLogOutput = null;
	
	protected ReferenceCounted<PrintWriter> openNextLogUnit() throws IOException
	{
		File f = getNextLogfile();
		PrintWriter ret = null;
		if(f.exists())
		{
			ret = new PrintWriter(new FileOutputStream(f, true), true);
		}
		else
		{
			ret = new PrintWriter(f);
		}
		
		return new ReferenceCounted<PrintWriter>(ret, 1)
		{
			@Override
			protected void onFree()
			{
				IOTools.silentFlush(subject);
				IOTools.silentClose(subject);
			}
		};
	}
	
	@Override
	public ReferenceCounted<PrintWriter> getLogOutput() throws IOException
	{
		synchronized (this)
		{
			if(null == currentLogOutput)
			{
				currentLogOutput = openNextLogUnit();
				afterOpenNewUnit();
			}
			else if(needCut())
			{
				currentLogOutput.release();
				currentLogOutput = openNextLogUnit();
				afterOpenNewUnit();
			}
			
			currentLogOutput.acquire();
			
			return currentLogOutput;
		}
	}
}
