package eu.javaexperience.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;

import eu.javaexperience.io.IOTools;
import eu.javaexperience.reflect.Mirror;

public class CompressTools
{
	public static byte[] compressLZ77(byte[] data) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gz = new GZIPOutputStream(out);
		gz.write(data);
		gz.finish();
		return out.toByteArray();
	}
	
	public static byte[] decompressLZ77(byte[] data) throws IOException
	{
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		GZIPInputStream gz = new GZIPInputStream(in);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IOTools.copyStream(gz, out);
		return out.toByteArray();
	}
	
	public static byte[] compressZlib(byte[] data, int level)
	{
		Deflater zipDeflater = new Deflater(level);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		zipDeflater.setInput(data);
		zipDeflater.finish();
		byte[] buffer = new byte[4096];
		int count = 0;
		while (!zipDeflater.finished())
		{
			count = zipDeflater.deflate(buffer);
			out.write(buffer, 0, count);
		}
		return out.toByteArray();
	}
	
	public static byte[] decompressZlib(byte[] data) throws IOException
	{
		Inflater inf = new Inflater();
		inf.reset();
		inf.setInput(data);
		if (inf.needsInput())
		{
			return Mirror.emptyByteArray;
		}
		
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[4096];
		try
		{
			while(!inf.finished())
			{
				int read = inf.inflate(buff);
				out.write(buff, 0, read);
			}
		}
		catch(Exception e)
		{
			throw new IOException(e);
		}
		return out.toByteArray();
	}
	
}
