package eu.javaexperience.io;

import java.io.IOException;
import java.io.InputStream;

public class SwappableInputStream extends InputStream
{
	protected InputStream is;
	
	public void setInputStream(InputStream is)
	{
		this.is = is;
	}
	
	public InputStream getBackendInputStream()
	{
		return is;
	}
	
	@Override
	public int read() throws IOException
	{
		return is.read();
	}
	
	@Override
	public int read(byte b[]) throws IOException
	{
		return is.read(b, 0, b.length);
	}
	
	@Override
	public int read(byte b[], int off, int len) throws IOException
	{
		return is.read(b, off, len);
	}
}
