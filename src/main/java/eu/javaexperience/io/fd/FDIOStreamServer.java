package eu.javaexperience.io.fd;


import java.io.FileDescriptor;
import java.io.IOException;

import eu.javaexperience.io.IOStreamServer;

public interface FDIOStreamServer extends IOStreamServer<FDIOStream>
{
	public void close();
	public FDIOStream accept() throws IOException;
	public int getFD();
	public FileDescriptor getFileDescriptor();
}
