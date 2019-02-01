package eu.javaexperience.log;

import java.io.IOException;
import java.io.PrintWriter;

import eu.javaexperience.io.IOTools;
import eu.javaexperience.resource.ReferenceCounted;

public class ThreadLocalHookableLogFacility
{
	protected static final ReferenceCounted<PrintWriter> NULL_OUTPUT = new ReferenceCounted<PrintWriter>(IOTools.nullPrintWriter, 1024)
	{
		@Override protected void onFree(){}
	};
	
	public static final LogOutput LOG_OUTPUT = new LogOutput()
	{
		@Override
		public ReferenceCounted<PrintWriter> getLogOutput() throws IOException
		{
			ReferenceCounted<PrintWriter> pw = OUTS.get();
			if(null == pw)
			{
				return NULL_OUTPUT;
			}
			
			pw.acquire();
			return pw;
		}
	};
	
	protected static final ThreadLocal<ReferenceCounted<PrintWriter>> OUTS = new ThreadLocal<>(); 
	
	
	public static ReferenceCounted<PrintWriter> getLocalOutput()
	{
		return OUTS.get();
	}
	
	public static void setLocalOutput(ReferenceCounted<PrintWriter> pw)
	{
		OUTS.set(pw);
	}
}
