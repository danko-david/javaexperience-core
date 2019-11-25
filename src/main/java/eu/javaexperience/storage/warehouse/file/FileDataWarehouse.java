package eu.javaexperience.storage.warehouse.file;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import eu.javaexperience.binary.FramedPacketCutter;
import eu.javaexperience.binary.PacketFramingTools;
import eu.javaexperience.storage.warehouse.DataWarehouse;
import eu.javaexperience.storage.warehouse.DataWarehouseInput;
import eu.javaexperience.storage.warehouse.DataWarehouseOutput;

public class FileDataWarehouse implements DataWarehouse<byte[]>
{
	protected final File file;
	
	protected FileDataWarehouseOutput fout;
	
	protected static final byte[] PACKET_END_SEQUENCE = new byte[] {(byte) 0x0, (byte)0xff};
		
	protected class FileDataWarehouseOutput implements DataWarehouseOutput<byte[]>
	{
		protected FileOutputStream out;
		
		protected FileDataWarehouseOutput(FileOutputStream out)
		{
			this.out = out;
		}
		
		@Override
		public synchronized void close() throws IOException
		{
			out.close();
			out = null;
		}
		
		@Override
		public synchronized void write(byte[] elem) throws IOException
		{
			byte[] w = PacketFramingTools.optEscapeBytes(elem, (byte) 0x00, 0);
			
			out.write(w);
			out.write(PACKET_END_SEQUENCE);
			out.flush();
		}
	}
	
	
	//TODO add locks
	public FileDataWarehouse(File f) throws IOException
	{
		this.file = f;
		if(!f.exists())
		{
			f.createNewFile();
		}
		fout = new FileDataWarehouseOutput(new FileOutputStream(file, true));
	}
	
	@Override
	public DataWarehouseOutput<byte[]> openOutput()
	{
		return new DataWarehouseOutput<byte[]>()
		{
			boolean opened = true;
			
			protected void assertNotClosed()
			{
				if(!opened && null != fout.out)
				{
					throw new RuntimeException("This DataWarehouseOutput is already closed");
				}
			}
			
			@Override
			public synchronized void close() throws IOException
			{
				assertNotClosed();
				opened = false;
			}

			@Override
			public synchronized void write(byte[] elem) throws IOException
			{
				assertNotClosed();
				fout.write(elem);
			}
		};
	}

	@Override
	public DataWarehouseInput<byte[]> openInput() throws IOException
	{
		return new DataWarehouseInput<byte[]>()
		{
			int entry = 0;
			
			FileInputStream fis = new FileInputStream(file);
			boolean opened = true;
			
			byte[] readBuff = new byte[4096];
			protected Deque<byte[]> packets = new LinkedList<>();
			protected FramedPacketCutter cutter = new FramedPacketCutter((byte) 0x0, p->packets.add(p));
			
			protected void assertNotClosed()
			{
				if(!opened)
				{
					throw new RuntimeException("This DataWarehouseInput is already closed");
				}
			}
			
			@Override
			public void close() throws IOException
			{
				assertNotClosed();
				fis.close();
			}
			
			@Override
			public synchronized byte[] read() throws EOFException, IOException
			{
				assertNotClosed();
				
				do
				{
					byte[] ret = packets.pollLast();
					if(null != ret)
					{
						++entry;
						return ret;
					}
					
					int read = 0;
					if((read = fis.read(ret)) < 1)
					{
						//TODO or eof exception?
						return null;
					}
					
					cutter.feedBytes(ret, read);
				}
				while(true);
			}
			
			@Override
			public synchronized boolean isSeekSupported()
			{
				assertNotClosed();
				return false;
			}
			
			@Override
			public synchronized long getPosition()
			{
				assertNotClosed();
				return entry;
			}
		};
	}

	@Override
	public void close() throws IOException
	{
		fout.close();
	}
}
