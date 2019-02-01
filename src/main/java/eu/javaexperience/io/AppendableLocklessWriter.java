package eu.javaexperience.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.Locale;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;

public class AppendableLocklessWriter extends PrintWriter
{
	protected final Appendable app;
	
	public AppendableLocklessWriter(Appendable app)
	{
		super(IOTools.nullWriter,true);
		AssertArgument.assertNotNull(this.app = app, "Appendable");
	}
	
	protected Formatter formatter = null;//new Formatter(this);

	protected boolean autoFlush;
	protected String lineSeparator = String.format("%n");

	private char[] buf = new char[1];

	public void write(int c) {
		buf[0] = ((char) c);
		write(buf, 0, 1);
	}

	public void write(String str)
	{
		try
		{
			app.append(str);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void write(String str, int off, int len)
	{
		try
		{
			app.append(str, off, len-off);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public PrintWriter append(CharSequence csq) {
		if (csq == null)
			write("null");
		else
			write(csq.toString());
		return this;
	}

	public PrintWriter append(CharSequence csq, int start, int end)
	{
		try
		{
			if(csq == null)
				app.append("null");
			else
				app.append(csq,start,end);
			return this;
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public PrintWriter append(char c) {
		write(c);
		return this;
	}

	public void write(char[] paramArrayOfChar, int paramInt1,int paramInt2)
	{
		try
		{
			app.append(new String(paramArrayOfChar,paramInt1,paramInt2));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void write(char[] paramArrayOfChar)
	{
		try
		{
			app.append(new String(paramArrayOfChar));
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void flush()
	{
		
	}

	public void close()
	{
		
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
	
	public static AppendableLocklessWriter asNewLineDispatcher(final SimplePublish1<String> newLine)
	{
		return new AppendableLocklessWriter(new Appendable()
		{
			protected StringBuilder sb = new StringBuilder();
			
			protected void dispatchLines()
			{
				while(true)
				{
					int last = sb.indexOf("\n");
					if(last == -1)
					{
						return;
					}
					
					String pub = sb.substring(0, last);
					sb.delete(0, last+1);
					
					newLine.publish(pub);
				}
			}
			
			@Override
			public Appendable append(CharSequence csq, int start, int end) throws IOException
			{
				sb.append(csq, start, end);
				dispatchLines();
				return this;
			}
			
			@Override
			public Appendable append(char c) throws IOException
			{
				sb.append(c);
				dispatchLines();
				return this;
			}
			
			@Override
			public Appendable append(CharSequence csq) throws IOException
			{
				sb.append(csq);
				dispatchLines();
				return this;
			}
		});
	}	
}