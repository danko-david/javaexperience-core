package eu.javaexperience.io.fd;

import java.io.FileDescriptor;

import eu.javaexperience.io.IOStream;


public interface FDIOStream extends IOStream
{
	public FileDescriptor getFileDescriptor();
	public int getFD();
}
