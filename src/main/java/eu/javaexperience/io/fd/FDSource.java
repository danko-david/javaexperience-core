package eu.javaexperience.io.fd;

import java.io.FileDescriptor;

public interface FDSource
{
	public FileDescriptor getNewFD();
}