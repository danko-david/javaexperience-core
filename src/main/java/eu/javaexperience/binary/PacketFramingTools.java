package eu.javaexperience.binary;

public class PacketFramingTools
{
	public static byte[] optEscapeBytes(byte[] data, byte terminator, int extraEndPadding)
	{
		int w = 0;
		for(int i=0;i<data.length;++i)
		{
			if(data[i] == terminator)
			{
				++w;
			}
		}
		
		if(0 == w && 0 == extraEndPadding)
		{
			return data;
		}
		
		byte[] ret = new byte[data.length+w+extraEndPadding];
		
		w = 0;
		
		for(int i=0;i<data.length;++i)
		{
			ret[w++] = data[i];
			if(terminator == data[i])
			{
				ret[w++] = terminator;
			}
		}
		
		return ret;
	}
	
	public static byte[] frameBytes(byte[] data, byte terminator)
	{
		byte[] ret = optEscapeBytes(data, terminator, 2);
		ret[data.length] = terminator;
		ret[data.length+1] = (byte) ~terminator;
		return ret;
	}
}
