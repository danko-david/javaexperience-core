package eu.javaexperience.io;

import java.io.IOException;
import java.io.InputStream;

public class CloseSensitiveInputStream extends InputStream
{
	protected final InputStream origin;
	protected boolean isClosed = false;
	
	protected void closed()
	{
		isClosed = true;
	}
	
	public CloseSensitiveInputStream(InputStream is)
	{
		origin = is;
	}
	
	@Override
	public int read() throws IOException
	{
		try
		{
			return origin.read();
		}
		catch(IOException ex)
		{
			closed();
			throw ex;
		}
	}

	@Override
	public int read(byte[] bytes) throws IOException
	{
		try
		{
			return origin.read(bytes);
		}
		catch(IOException ex)
		{
			closed();
			throw ex;
		}
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		try
		{
			return origin.read(b,off,len);
		}
		catch(IOException ex)
		{
			closed();
			throw ex;
		}
	}
	
	public InputStream getOrigin()
	{
		return origin;
	}
	
	@Override
	public int available() throws IOException
	{
		return origin.available();
	}
	
	@Override
	public void close() throws IOException
	{
		closed();
		origin.close();
	}
	
	@Override
	public long skip(long skip) throws IOException
	{
		return origin.skip(skip);
	}
	
	public boolean isClosed()
	{
		return isClosed;
	}
	
	@Override
	public synchronized void mark(int readlimit)
	{
		origin.mark(readlimit);
	}
	
	@Override
	public synchronized void reset() throws IOException
	{
		origin.reset();
	}
}