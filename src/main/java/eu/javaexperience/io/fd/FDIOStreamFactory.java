package eu.javaexperience.io.fd;


import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import eu.javaexperience.io.CloseSensitiveInputStream;
import eu.javaexperience.io.CloseSensitiveOutputStream;
import eu.javaexperience.reflect.FdMirror;
import eu.javaexperience.reflect.Mirror;

public class FDIOStreamFactory
{
	public static FDIOStream fromSocket(final Socket s) throws IOException
	{
		return new CloseRegFDIO()
		{
			private FileDescriptor fd = FdMirror.getFileDescriptorFromSocket(s);

			@Override
			public OutputStream getOutputStream()
			{
				try
				{
					return s.getOutputStream();
				}
				catch (IOException e)
				{
				}
				return null;
			}

			@Override
			public InputStream getInputStream()
			{
				try
				{
					return s.getInputStream();
				}
				catch (IOException e)
				{
				}
				return null;
			}

			@Override
			public void close()
			{
				try
				{
					s.close();
				}
				catch (IOException e)
				{}
			}

			@Override
			public int getFD()
			{
				return FdMirror.getFD(fd);
			}

			private boolean closed = false;
			@Override
			public boolean isClosed()
			{
				return closed;
			}

			@Override
			public void setClosed(boolean b)
			{
				closed = b;
			}

			@Override
			public FileDescriptor getFileDescriptor()
			{
				return fd;
			}

			@Override
			public void flush() throws IOException
			{
				s.getOutputStream().flush();
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
	
	public static FDIOStream closeOnFinalize(final FDIOStream sock)
	{
		return new FDIOStream()
		{
			@Override
			public boolean isClosed()
			{
				return sock.isClosed();
			}
			
			@Override
			public OutputStream getOutputStream()
			{
				return sock.getOutputStream();
			}
			
			@Override
			public InputStream getInputStream()
			{
				return sock.getInputStream();
			}
			
			@Override
			public void close()
			{
				sock.close();
			}
			
			@Override
			public int getFD()
			{
				return sock.getFD();
			}
			
			protected void finalize()
			{
				close();
			}

			@Override
			public FileDescriptor getFileDescriptor()
			{
				return sock.getFileDescriptor();
			}

			@Override
			public void flush() throws IOException
			{
				sock.flush();
			}

			@Override
			public String localAddress()
			{
				return sock.localAddress();
			}

			@Override
			public String remoteAddress()
			{
				return sock.remoteAddress();
			}
		};
		
	}

	public static interface CloseRegFDIO extends FDIOStream
	{
		public void setClosed(boolean b);
	}

	public static FDIOStream fromFD(final FileDescriptor _fd)
	{
		return fromFD(FdMirror.getFD(_fd));
	}

	public static FDIOStream fromFD(final int _fd)
	{
		return new FDIOStream()
		{
			private final int fd = _fd;
			private final FileDescriptor ofd = new FileDescriptor();

			{
				FdMirror.setFd(ofd, _fd);
			}
			
			CloseSensitiveInputStream is =  new CloseSensitiveInputStream( new FileInputStream(ofd));
			CloseSensitiveOutputStream os  = new CloseSensitiveOutputStream( new FileOutputStream(ofd));

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
			public int getFD()
			{
				return fd;
			}

			@Override
			public boolean isClosed()
			{
				return is.isClosed() || os.isClosed();
			}

			@Override
			public FileDescriptor getFileDescriptor()
			{
				return ofd;
			}

			@Override
			public void flush() throws IOException
			{
				os.flush();
			}

			@Override
			public String localAddress()
			{
				return String.valueOf(fd);
			}

			@Override
			public String remoteAddress()
			{
				return String.valueOf(fd);
			}
		};
	}

	public static FDIOStreamServer fromServerSocket(final ServerSocket ss)
	{
		return new FDIOStreamServer()
		{
			@Override
			public void close()
			{
				try
				{
					ss.close();
				}
				catch (IOException e)
				{
				}
			}

			@Override
			public FDIOStream accept() throws IOException
			{
				try
				{
					return fromSocket(ss.accept());
				}
				catch(Exception e)
				{
					throw new IOException(e);
				}
			}

			@Override
			public int getFD()
			{
				return 0;
			}

			@Override
			public FileDescriptor getFileDescriptor()
			{
				return null;
			}
		};
	}
}