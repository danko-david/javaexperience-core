package eu.javaexperience.binary;

import java.util.Arrays;

import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingTools;

public class FramedPacketCutter
{
	protected static final Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("FramedPacketReader"));

	protected byte packetEscape;
	protected SimplePublish1<byte[]> packet;
	
	public FramedPacketCutter(byte escapeByte, SimplePublish1<byte[]> packet)
	{
		packetEscape = escapeByte;
		this.packet = packet;
	}
	
	byte[] buffer = new byte[1024];
	boolean mayCut = false;
	int ep = 0;
	
	public void clear()
	{
		ep = 0;
	}
	
	public synchronized void feedBytes(byte[] data, int length)
	{
		boolean trace = LOG.mayLog(LogLevel.TRACE);
		for(int i=0;i<length;++i)
		{
			byte b = data[i];
			if(trace)
			{
				LoggingTools.tryLogFormat(LOG, LogLevel.TRACE, "Feeding byte: %s, mayCut: %s, ep: %s, ", 0xff & b, mayCut, ep, Arrays.toString(Arrays.copyOf(buffer, ep)));
			}
			
			if(mayCut)
			{
				if(b == packetEscape)
				{
					buffer[ep++] = packetEscape;
				}
				else
				{
					int cut = ep;
					ep = 0;
					packet.publish(Arrays.copyOf(buffer, cut));
				}
				mayCut = false;
			}
			else
			{
				if(b == packetEscape)
				{
					mayCut = true;
				}
				else
				{
					buffer[ep++] = b;
				}
			}
			
			if(ep == buffer.length)
			{
				buffer = Arrays.copyOf(buffer, buffer.length*2);
			}
		}
	}
}
