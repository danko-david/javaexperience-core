package eu.javaexperience.log;

import java.io.IOException;
import java.io.PrintWriter;

import eu.javaexperience.resource.ReferenceCounted;

//TODO this class should related to write a single entry out (like a SimplePublish<String>)
//and no to a resource like stuff
public interface LogOutput
{
	public ReferenceCounted<PrintWriter> getLogOutput() throws IOException;
}
