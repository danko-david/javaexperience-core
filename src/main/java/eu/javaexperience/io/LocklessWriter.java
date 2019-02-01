package eu.javaexperience.io;

import java.io.IOException;
import java.io.Writer;

public abstract class LocklessWriter extends Writer
{
	private char[] buf = new char[1];

	public void write(int c) throws IOException
	{
		buf[0] = ((char) c);
		write(buf, 0, 1);
	}

	public void write(char[] cbuf) throws IOException
	{
		write(cbuf, 0, cbuf.length);
	}

	public void write(String str) throws IOException
	{
		write(str, 0, str.length());
	}

	public void write(String str, int off, int len) throws IOException
	{
		if(len > buf.length)
			buf = new char[len];
		str.getChars(off, off + len, buf, 0);
		write(buf, 0, len);
	}

	public Writer append(CharSequence csq) throws IOException
	{
		if (csq == null)
			write("null");
		else
			write(csq.toString());
		return this;
	}

	public Writer append(CharSequence csq, int start, int end) throws IOException
	{
		CharSequence cs = csq == null ? "null" : csq;
		write(cs.subSequence(start, end).toString());
		return this;
	}

	public Writer append(char c) throws IOException
	{
		write(c);
		return this;
	}
	
	public abstract void write(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException;

	public abstract void flush() throws IOException;

	public abstract void close() throws IOException;
}
