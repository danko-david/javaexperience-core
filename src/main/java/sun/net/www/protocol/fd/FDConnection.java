package sun.net.www.protocol.fd;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import eu.javaexperience.reflect.FdMirror;
import eu.javaexperience.reflect.Mirror;

public class FDConnection extends URLConnection
{
	private InputStream is = null;
	private OutputStream os = null;
	int fd = -1;


	public FDConnection(URL u) throws MalformedURLException
	{
		super(u);
		try
		{
			String url = u.toString();
			FileDescriptor fds = new FileDescriptor();
			FdMirror.setFd(fds, fd = Integer.parseInt(url.replace("\\s", "").substring(5)));
			is = new FileInputStream(fds);
			os = new FileOutputStream(fds);
		}
		catch(Exception e)
		{
			throw new MalformedURLException("");
		}

	}

	@Override
	public void connect() throws IOException
	{
	}

	@Override
	public Object getContent()
	{
		return null;
	}

	@Override
	public OutputStream getOutputStream()
	{
		return os;
	}

	@Override
	public InputStream getInputStream()
	{
		return is;
	}
}
