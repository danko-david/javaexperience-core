package eu.javaexperience.io;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.Locale;

import eu.javaexperience.reflect.Mirror;


public class LocklessPrintWriter extends PrintWriter
{
	protected Formatter formatter = null;//new Formatter(this);
	
	public LocklessPrintWriter(OutputStream os, boolean autoFlush)
	{
		super(os, autoFlush);
	}
	
	public LocklessPrintWriter(OutputStream os)
	{
		super(os);
	}
	
	protected boolean autoFlush;
	
	protected static String DEFAULT_LINE_SEPARATOR = "\n";
	
	static
	{
		try
		{
			DEFAULT_LINE_SEPARATOR = String.format("%n");
		}
		catch(Exception e) {}
	}
	
	protected String lineSeparator = DEFAULT_LINE_SEPARATOR;

	private char[] buf = new char[1];

	@Override
	public void write(int c)
	{
		buf[0] = ((char) c);
		write(buf, 0, 1);
	}

	@Override
	public void write(char[] cbuf)
	{
		write(cbuf, 0, cbuf.length);
	}

	@Override
	public void write(String str)
	{
		write(str, 0, str.length());
	}

	@Override
	public void write(String str, int off, int len)
	{
		if (len > buf.length)
			buf = new char[len];
		str.getChars(off, off + len, buf, 0);
		write(buf, 0, len);
	}

	@Override
	public PrintWriter append(CharSequence csq)
	{
		if (csq == null)
			write("null");
		else
			write(csq.toString());
		return this;
	}

	public PrintWriter append(CharSequence csq, int start, int end)
	{
		CharSequence cs = csq == null ? "null" : csq;
		write(cs.subSequence(start, end).toString());
		return this;
	}

	public PrintWriter append(char c)
	{
		write(c);
		return this;
	}

	private void newLine()
	{
		this.write(this.lineSeparator);
			if (this.autoFlush)
				this.flush();
	}

	public void print(boolean b)
	{
		write(b ? "true" : "false");
	}

	public void print(char c)
	{
		write(c);
	}

	public void print(int i)
	{
		write(String.valueOf(i));
	}

	public void print(long l)
	{
		write(String.valueOf(l));
	}

	public void print(float f)
	{
		write(String.valueOf(f));
	}

	public void print(double d)
	{
		write(String.valueOf(d));
	}

	public void print(char[] s)
	{
		write(s);
	}

	public void print(String s)
	{
		if (s == null)
			s = "null";
		write(s);
	}

	public void print(Object obj)
	{
		write(String.valueOf(obj));
	}

	public void println()
	{
		newLine();
	}

	public void println(boolean x)
	{
		print(x);
		println();
	}

	public void println(char x)
	{
		print(x);
		println();
	}

	public void println(int x)
	{
		print(x);
		println();
	}

	public void println(long x)
	{
		print(x);
		println();
	}

	public void println(float x)
	{
		print(x);
		println();
	}

	public void println(double x)
	{
		print(x);
		println();
	}

	public void println(char[] x)
	{
		print(x);
		println();
	}

	public void println(String x)
	{
		print(x);
		println();
	}

	public void println(Object x)
	{
		print(String.valueOf(x));
		println();
	}

	public PrintWriter printf(String format, Object... args)
	{
		return format(format, args);
	}
	
	public void write(char buf[], int off, int len)
	{
		try
		{
			out.write(buf, off, len);
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
		}
	}
	
	public PrintWriter printf(Locale l, String format, Object... args)
	{
		return format(l, format, args);
	}

	public PrintWriter format(String format, Object... args)
	{
		if(formatter == null)
			formatter = new Formatter(this);
		this.formatter.format(Locale.getDefault(), format, args);
			if (this.autoFlush)
				this.flush();
		return this;
	}

	public PrintWriter format(Locale l, String format, Object... args)
	{
		if(formatter == null)
			formatter = new Formatter(this);
		this.formatter.format(l, format, args);
			if (this.autoFlush)
				this.flush();
		return this;
	}
}