package eu.javaexperience.io;

import java.io.IOException;

public abstract class AbstractAppendable implements Appendable
{
	@Override
	public abstract Appendable append(CharSequence csq) throws IOException;

	@Override
	public Appendable append(char c) throws IOException
	{
		return append(String.valueOf(c));
	}

	@Override
	public Appendable append(CharSequence csq, int start, int end) throws IOException
	{
		return append(csq.subSequence(start, end));
	}
}
