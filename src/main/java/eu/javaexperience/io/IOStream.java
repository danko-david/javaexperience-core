package eu.javaexperience.io;

import java.io.Closeable;
import java.io.Flushable;
import java.io.InputStream;
import java.io.OutputStream;


/**
 
 * */
public interface IOStream extends Closeable, AutoCloseable, Flushable
{
	public InputStream getInputStream();
	public OutputStream getOutputStream();
	public void close();
	public boolean isClosed();
	public String localAddress();
	public String remoteAddress();
}