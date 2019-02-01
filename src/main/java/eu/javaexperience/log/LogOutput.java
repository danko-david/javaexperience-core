package eu.javaexperience.log;

import java.io.IOException;
import java.io.PrintWriter;

import eu.javaexperience.resource.ReferenceCounted;

public interface LogOutput
{
	public ReferenceCounted<PrintWriter> getLogOutput() throws IOException;
}
