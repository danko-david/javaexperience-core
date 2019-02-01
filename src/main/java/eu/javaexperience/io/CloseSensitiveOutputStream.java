package eu.javaexperience.io;

import java.io.IOException;
import java.io.OutputStream;

public class CloseSensitiveOutputStream extends OutputStream
{
	private final OutputStream origin;
	private boolean isClosed = false;
	
	public CloseSensitiveOutputStream(OutputStream out)
	{
		origin = out;
	}
	
	@Override
	public void write(int arg0) throws IOException
	{
		try
		{
			origin.write(arg0);
		}
		catch(IOException ex)
		{
			isClosed = true;
			throw ex;
		}
	}

	@Override
	public void close() throws IOException
	{
		origin.close();
	}
	
	@Override
	public void flush() throws IOException
	{
		origin.flush();
	}
	
	@Override
	public void write(byte[] b) throws IOException
	{
		try
		{
			origin.write(b);
		}
		catch(IOException ex)
		{
			isClosed = true;
			throw ex;
		}
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		try
		{
			origin.write(b,off,len);
		}
		catch(IOException ex)
		{
			isClosed = true;
			throw ex;
		}
	}
	
	public OutputStream getOrigin()
	{
		return origin;
	}
	
	public boolean isClosed()
	{
		return isClosed;
	}
}