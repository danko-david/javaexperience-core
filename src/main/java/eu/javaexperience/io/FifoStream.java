package eu.javaexperience.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import eu.javaexperience.reflect.Mirror;

public class FifoStream
{
	protected BlockingQueue<byte[]> data;
	
	public FifoStream(int cap)
	{
		data = new LinkedBlockingQueue<byte[]>(cap);
	}
	
	protected OutputStream os = new OutputStream()
	{
		@Override
		public void write(int b) throws IOException
		{
			data.add(new byte[]{(byte)b});
		}
		
		public void write(byte[] b, int off, int len) throws IOException
		{
			data.add(Arrays.copyOfRange(b, off, off+len));
		};
		
		public void write(byte[] b) throws IOException
		{
			data.add(Arrays.copyOf(b, b.length));
		};
	};
	
	public OutputStream getOutputStream()
	{
		return os;
	}
	
	public byte[] get() throws InterruptedException
	{
		return data.take();
	}
	
	protected InputStream is = new InputStream()
	{
		SwappableInputStream sis = new SwappableInputStream();
		{
			sis.is = IOTools.nullInputStream;
		}
		
		protected int doProperRead(byte[] dst, int off, int len) throws IOException
		{
			int ret = sis.read();
			while(ret < 0)
			{
				try
				{
					sis.setInputStream(new ByteArrayInputStream(get()));
				}
				catch (InterruptedException e)
				{
					Mirror.propagateAnyway(e);
				}
				if(null == dst)
				{
					ret = sis.read();
				}
				else if(off < 0)
				{
					ret = sis.read(dst);
				}
				else
				{
					ret = sis.read(dst, off, len);
				}
			}
			
			return ret;
		}
		
		@Override
		public int read() throws IOException
		{
			return doProperRead(null, -1, -1);
		}
		
		public int read(byte[] b) throws IOException
		{
			return doProperRead(b, -1, -1);
		};
		
		public int read(byte[] b, int off, int len) throws IOException
		{
			return doProperRead(b, off, len);
		};
	};
	
	public InputStream getInputStream()
	{
		return is;
	}

	public void put(byte[] d)
	{
		data.add(d);
	}
}
