package sun.net.www.protocol.fd;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler
{
	@Override
	protected URLConnection openConnection(URL arg0) throws IOException
	{
		return new FDConnection(arg0);
	}
}
