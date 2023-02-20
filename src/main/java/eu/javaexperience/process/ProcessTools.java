package eu.javaexperience.process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import eu.javaexperience.io.IOTools;
import eu.javaexperience.multithread.Job;

public class ProcessTools
{
	public static byte[] processStdoutResult(String... prog) throws IOException, InterruptedException
	{
		ProcessBuilder pb = new ProcessBuilder(prog);
		Process p = pb.start();
		byte[] data = IOTools.loadAllFromInputStream(p.getInputStream());
		p.waitFor();
		return data;
	}
	
	public static byte[] processStderrResult(String... prog) throws IOException, InterruptedException
	{
		ProcessBuilder pb = new ProcessBuilder(prog);
		Process p = pb.start();
		byte[] data = IOTools.loadAllFromInputStream(p.getErrorStream());
		p.waitFor();
		return data;
	}
	
	public static int processExitStatus(String... prog) throws IOException, InterruptedException
	{
		ProcessBuilder pb = new ProcessBuilder(prog);
		Process p = pb.start();
		return p.waitFor();
	}
	
	public static Process processRun(String... prog) throws IOException, InterruptedException
	{
		ProcessBuilder pb = new ProcessBuilder(prog);
		return pb.start();
	}
	
	protected static final Job<InputStream> READ_ALL = new Job<InputStream>()
	{
		@Override
		public void exec(InputStream param) throws Throwable
		{
			IOTools.copyStream(param, IOTools.nullOutputStream);
		}
	};
	
	//XXX: Oh boy... use the PATH enverinment variable like real `which`. TODO Test
	@Deprecated
	protected static final String[] PATHS = new String[]
	{
		"/usr/local/bin/",
		"/usr/bin/",
		"/bin/",
		"/usr/local/games/",
		"/usr/games/",
		"/usr/lib/jvm/java-7-oracle/bin/",
		"/usr/lib/jvm/java-7-oracle/db/bin/",
		"/usr/lib/jvm/java-7-oracle/jre/bin/"
	};
	
	public static String which(String prog)
	{
		return which(prog, PATHS);
	}
	
	public static String which(String prog, String... paths)
	{
		for(String path:paths)
		{
			String file = path+prog;
			if(new File(file).exists())
			{
				return file;
			}
		}
		
		return null;
	}

	public static void assertedProcessExitStatus(String... prog) throws IOException, InterruptedException
	{
		int exit = processExitStatus(prog);
		if(0 != exit)
		{
			throw new RuntimeException("Process Exit status not zero: "+exit+" "+Arrays.toString(prog));
		}
	}

	public static void assertedProcessExitStatusWithWorkingDirectory(File wd, String... prog) throws InterruptedException, IOException
	{
		ProcessBuilder pb = new ProcessBuilder(prog);
		pb.directory(wd);
		int exit = pb.start().waitFor();
		if(0 != exit)
		{
			throw new RuntimeException("Process Exit status not zero: "+exit+" "+Arrays.toString(prog));
		}
	}

	public static int processWithWorkingDirectory(File wd, String... prog) throws InterruptedException, IOException
	{
		ProcessBuilder pb = new ProcessBuilder(prog);
		pb.directory(wd);
		return pb.start().waitFor();
	}
}