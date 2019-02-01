package eu.javaexperience.io;

import java.io.Closeable;
import java.io.IOException;

public interface IOStreamServer<S extends IOStream> extends Closeable
{
	public S accept() throws IOException;
}