package eu.javaexperience.io.fd;


import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.io.CloseSensitiveInputStream;
import eu.javaexperience.io.CloseSensitiveOutputStream;
import eu.javaexperience.io.IOStream;
import eu.javaexperience.io.IOStreamServer;
import eu.javaexperience.io.IOTools;
import eu.javaexperience.multithread.notify.WaitForSingleEvent;
import eu.javaexperience.reflect.Mirror;


public final class IOStreamFactory
{
	public static final IOStreamServer NO_OP_SERVER_SOCKET = new IOStreamServer<IOStream>()
	{
		WaitForSingleEvent wait = new WaitForSingleEvent();
		@Override
		public void close() throws IOException
		{
			wait.evenOcurred();
		}

		@Override
		public IOStream accept() throws IOException
		{
			try
			{
				wait.waitForEvent();
			}
			catch (InterruptedException e)
			{
				Mirror.propagateAnyway(e);
			}
			return null;
		}
	};

	public static IOStream fromSocket(final Socket s) throws IOException
	{
		return new IOStream()
		{
			//CloseSensitiveInputStream is = new CloseSensitiveInputStream(s.getInputStream());
			//CloseSensitiveOutputStream os = new CloseSensitiveOutputStream(s.getOutputStream());
			InputStream is = s.getInputStream();
			OutputStream os = s.getOutputStream();
			
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

			@Override
			public void close()
			{
				try
				{
					s.close();
				}
				catch (IOException e)
				{
				}
			}

			@Override
			public boolean isClosed()
			{
				return false;
				//throw new UnsupportedOperationException();
				//return is.isClosed() || os.isClosed();
			}

			@Override
			public void flush() throws IOException
			{
				os.flush();
			}

			@Override
			public String localAddress()
			{
				return s.getLocalAddress().toString();
			}

			@Override
			public String remoteAddress()
			{
				return s.getRemoteSocketAddress().toString();
			}
		};
	}


	public static IOStream fromFD(final FileDescriptor fd)
	{
		return new IOStream()
		{
			//CloseSensitiveInputStream is =  new CloseSensitiveInputStream(new FileInputStream(fd));
			//CloseSensitiveOutputStream os  = new CloseSensitiveOutputStream(new FileOutputStream(fd));
			
			InputStream is =  new FileInputStream(fd);
			OutputStream os  = new FileOutputStream(fd);
			
			@Override
			public InputStream getInputStream()
			{
				return is;
			}

			@Override
			public OutputStream getOutputStream()
			{
				return os;
			}

			@Override
			public void close()
			{
				try{os.close();}catch(Exception e){}
				try{is.close();}catch(Exception e){}
			}

			@Override
			public boolean isClosed()
			{
				return false;
				//return is.isClosed() || os.isClosed();
			}

			@Override
			public void flush() throws IOException
			{
				os.flush();
			}

			@Override
			public String localAddress()
			{
				return fd.toString();
			}

			@Override
			public String remoteAddress()
			{
				return fd.toString();
			}
		};
	}

	public static IOStream fromFile(final String s) throws FileNotFoundException
	{
		return new IOStream()
		{
			CloseSensitiveInputStream is =  new CloseSensitiveInputStream(new FileInputStream(s));
			CloseSensitiveOutputStream os  = new CloseSensitiveOutputStream(new FileOutputStream(s));

			@Override
			public InputStream getInputStream()
			{
				return is;
			}

			@Override
			public OutputStream getOutputStream()
			{
				return os;
			}

			@Override
			public void close()
			{
				try{os.close();}catch(Exception e){}
				try{is.close();}catch(Exception e){}
			}

			@Override
			public boolean isClosed()
			{
				return is.isClosed() || os.isClosed();
			}
			
			@Override
			public void flush() throws IOException
			{
				os.flush();
			}

			@Override
			public String localAddress()
			{
				return s;
			}

			@Override
			public String remoteAddress()
			{
				return s;
			}
		};

	}

	public static IOStream fromInAndOutputStream(final InputStream ins,final OutputStream outs)
	{
		return new IOStream()
		{
			//CloseSensitiveInputStream is = new CloseSensitiveInputStream(s.getInputStream());
			//CloseSensitiveOutputStream os = new CloseSensitiveOutputStream(s.getOutputStream());
			InputStream is = ins;
			OutputStream os = outs;
			
			@Override
			public InputStream getInputStream()
			{
				return is;
			}

			@Override
			public OutputStream getOutputStream()
			{
				return os;
			}

			@Override
			public void close()
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{}

				try
				{
					os.close();
				}
				catch (IOException e)
				{}
			}

			@Override
			public boolean isClosed()
			{
				throw new UnsupportedOperationException();
				//return is.isClosed() || os.isClosed();
			}
			
			@Override
			public void flush() throws IOException
			{
				os.flush();
			}

			@Override
			public String localAddress()
			{
				return "";
			}

			@Override
			public String remoteAddress()
			{
				return "";
			}
		};
	}

	public static IOStreamServer<IOStream> fromServerSocket(final ServerSocket ss)
	{
		return new IOStreamServer<IOStream>()
		{
			@Override
			public void close()
			{
				try
				{
					ss.close();
				}
				catch (IOException e)
				{}
			}
			
			protected Socket realAccept() throws IOException
			{
				return ss.accept();
			}
			
			@Override
			public IOStream accept() throws IOException
			{
				return fromSocket(realAccept());
			}
		};
	}
	
	public static IOStreamServer<IOStream> fromServerSocket(final ServerSocket ss, final SimplePublish1<Socket> beforeReturn)
	{
		return new IOStreamServer<IOStream>()
		{
			@Override
			public void close()
			{
				try
				{
					ss.close();
				}
				catch (IOException e)
				{}
			}
			
			protected Socket realAccept() throws IOException
			{
				return ss.accept();
			}

			@Override
			public IOStream accept() throws IOException
			{
				Socket ret = realAccept();
				if(null != beforeReturn)
				{
					beforeReturn.publish(ret);
				}
				return fromSocket(ret);
			}
		};
	}
	
	public static IOStreamServer<IOStream> fromServerSocket(final ServerSocket ss, final GetBy1<IOStream, Socket> wrap)
	{
		return new IOStreamServer<IOStream>()
		{
			@Override
			public void close()
			{
				try
				{
					ss.close();
				}
				catch (IOException e)
				{}
			}
			
			protected Socket realAccept() throws IOException
			{
				return ss.accept();
			}

			@Override
			public IOStream accept() throws IOException
			{
				return wrap.getBy(realAccept());
			}
		};
	}
	
	public static IOStream gzipize(final IOStream sock) throws IOException
	{
		return new IOStream()
		{
			GZIPInputStream in = null;
			GZIPOutputStream out = null;

			{
				in = new GZIPInputStream(sock.getInputStream());
				out = new GZIPOutputStream(sock.getOutputStream());
			}
			
			@Override
			public InputStream getInputStream()
			{
				return in;
			}

			@Override
			public OutputStream getOutputStream()
			{
				return out;
			}

			@Override
			public void close()
			{
				sock.close();
			}

			@Override
			public boolean isClosed()
			{
				return sock.isClosed();
			}
			
			@Override
			public void flush() throws IOException
			{
				out.flush();
			}

			@Override
			public String localAddress()
			{
				return "gz:"+sock.localAddress();
			}

			@Override
			public String remoteAddress()
			{
				return "gz:"+sock.remoteAddress();
			}
		};
	}
	
	public static IOStreamServer<IOStream> gzipize(final IOStreamServer<IOStream> srv)
	{
		return new IOStreamServer<IOStream>()
		{
			@Override
			public void close()
			{
				IOTools.silentClose(srv);
			}

			@Override
			public IOStream accept() throws IOException
			{
				return gzipize(srv.accept());
			}
		};
	}
	
	public static IOStream gzipize(final IOStream sock,final int Deflater_level) throws IOException
	{
		return new IOStream()
		{
			GZIPInputStream in = null;
			GZIPOutputStream out = null;
			{
				out = new GZIPOutputStream(sock.getOutputStream())
				{
					{
						def.setLevel(Deflater_level);
					}
				};

				in = new GZIPInputStream(sock.getInputStream());
			}
			
			@Override
			public InputStream getInputStream()
			{
				return in;
			}

			@Override
			public OutputStream getOutputStream()
			{
				return out;
			}

			@Override
			public void close()
			{
				sock.close();
			}

			@Override
			public boolean isClosed()
			{
				return sock.isClosed();
			}
			
			@Override
			public void flush() throws IOException
			{
				out.flush();
			}

			@Override
			public String localAddress()
			{
				return "gz:"+sock.localAddress();
			}

			@Override
			public String remoteAddress()
			{
				return "gz:"+sock.remoteAddress();
			}
		};
	}
	
	public static IOStreamServer<IOStream> gzipize(final IOStreamServer<IOStream> srv,final int Deflater_level)
	{
		return new IOStreamServer<IOStream>()
		{
			@Override
			public void close()
			{
				IOTools.silentClose(srv);
			}

			@Override
			public IOStream accept() throws IOException
			{
				return gzipize(srv.accept(),Deflater_level);
			}
		};
	}


/*	public static IOStream bzipize(final IOStream sock) throws IOException
	{
		return new IOStream()
		{
			InputStream in = null;
			OutputStream out = null;

			{
				out = new CBZip2OutputStream(sock.getOutputStream());
				in = new CBZip2InputStream(sock.getInputStream());
			}
			
			@Override
			public InputStream getInputStream()
			{
				return in;
			}

			@Override
			public OutputStream getOutputStream()
			{
				return out;
			}

			public String getSocketInfo()
			{
				return "gz"+sock.toString();
			}

			@Override
			public void close()
			{
				sock.close();
			}

			@Override
			public boolean isClosed()
			{
				return sock.isClosed();
			}
			
			@Override
			public void flush() throws IOException
			{
				out.flush();
			}
		};
	}
	
	public static IOStreamServer bzipize(final IOStreamServer srv)
	{
		return new IOStreamServer()
				{
					@Override
					public void close()
					{
						srv.close();						
					}

					@Override
					public IOStream accept() throws IOException
					{
						return bzipize(srv.accept());
					}

					public String getSocketServerInfo()
					{
						return "gz"+srv.toString();
					}
				};
	}
	
	
	public static WakeLock crossConnectSocketsOnThreads(final IOStream sock1,final IOStream sock2)
	{
		final WakeLock lock = new WakeLock();

		final Thread[] t = new Thread[2];
		t[0] =
		new Thread()
		{
			
			InputStream is = sock1.getInputStream();
			OutputStream os = sock2.getOutputStream();
			byte[] buf = new byte[1000];
			public void run()
			{
				while(!sock1.isClosed())
				{
					int n;
					try {
						n = is.read(buf);
						if(n == -1)
							throw new IOException("A kapcsolat bezárult");
						os.write(buf,0,n);
						os.flush();
					} catch (Throwable e)
					{
						lock.setReady(true);
						synchronized (lock.getWakerObject())
						{
							lock.getWakerObject().notifyAll();
							t[1].interrupt();
							interrupt();
							break;
						}
					}
				}
			}
		};
		
		t[1] =
		new Thread()
		{
			
			InputStream is = sock2.getInputStream();
			OutputStream os = sock1.getOutputStream();
			byte[] buf = new byte[1000];
			public void run()
			{
				while(!sock2.isClosed())
				{
					int n;
					try {
						n = is.read(buf);
						if(n == -1)
							throw new IOException("A kapcsolat bezárult");
						os.write(buf,0,n);
						os.flush();
					} catch (Throwable e)
					{
						lock.setReady(true);
						synchronized (lock.getWakerObject())
						{
							lock.getWakerObject().notifyAll();
							t[0].interrupt();
							interrupt();
							break;
						}
					}
				}
			}
		};
		
		t[0].start();
		t[1].start();
		
		return lock;
	}
	*/
}