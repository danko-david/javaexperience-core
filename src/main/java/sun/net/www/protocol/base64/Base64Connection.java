package sun.net.www.protocol.base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import eu.javaexperience.io.IOTools;
import eu.javaexperience.text.Format;

public class Base64Connection extends URLConnection
{
	public Base64Connection(URL u) throws MalformedURLException
	{
		super(u);
	}

	@Override
	public void connect() throws IOException
	{}

	protected byte[] decode()
	{
		return Format.base64Decode(getURL().getHost());
	}
	
	@Override
	public Object getContent()
	{
		return decode();
	}

	@Override
	public OutputStream getOutputStream()
	{
		return IOTools.nullOutputStream;
	}
	
	@Override
	public InputStream getInputStream()
	{
		return new ByteArrayInputStream(decode());
	}
}
