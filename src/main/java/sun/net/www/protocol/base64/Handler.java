package sun.net.www.protocol.base64;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler
{
	@Override
	protected URLConnection openConnection(URL arg0) throws IOException
	{
		return new Base64Connection(arg0);
	}
	
/*	public static void main(String[] args) throws Throwable
	{
		System.out.println(new String((byte[])new URL("base64://bm9uY2U=").openConnection().getContent()));
	}*/
}