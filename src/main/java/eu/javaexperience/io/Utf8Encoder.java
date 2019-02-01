package eu.javaexperience.io;

public abstract class Utf8Encoder
{
	public Utf8Encoder(int size)
	{
		buf = new byte[size];
	}
	
	public Utf8Encoder(byte[] buffer)
	{
		buf = buffer;
	}
	
	protected byte[] buf;
	protected int ep = -1;
	
	public void encode(char[] sa,int sp,int len)
	{
		int end = sp+len;
		for(int i= sp;i<end;i++)
		{
			if(ep + 4 >= buf.length)
				flushBuffer();
			
			char c = sa[i];
			if(c < 0x80)
				buf[++ep] = (byte)c;
			else if(c <= 0x7FF)
			{
				buf[++ep] = (byte) ((c >> 6) + 0xC0);
				buf[++ep] = (byte) ((c & 0x3F) + 0x80);
			}
			else if(c <= 0xFFFF)
			{
				 buf[++ep] = (byte)((c >> 12) + 0xE0);
				 buf[++ep] = (byte)(((c >> 6) & 0x3F) + 0x80);
				 buf[++ep] = (byte)((c & 0x3F) + 0x80);
			}
			else if(c <= 0x10FFFF)
			{
				buf[++ep] = (byte)((c >> 18) + 0xF0);
				buf[++ep] = (byte)(((c >> 12) & 0x3F) + 0x80);
				buf[++ep] = (byte)(((c >> 6) & 0x3F) + 0x80);
				buf[++ep] = (byte)((c & 0x3F) + 0x80);
			}
		}
	}
	
	protected void flushBuffer()
	{
		if(ep > 0)
		{
			putOutput(buf,0,ep+1);
			ep = -1;
		}
	}
	
	protected abstract void putOutput(byte[] data, int from, int to);
}
