package eu.javaexperience.io;

import java.io.IOException;

public interface ToAppendable
{
	public void append(Appendable app) throws IOException;
}
